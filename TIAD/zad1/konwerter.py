import tkinter as tk
from tkinter import filedialog, messagebox

import numpy as np
import pandas as pd
from docx import Document
from docx.shared import Cm
from docx.shared import Pt
from docx.enum.text import WD_ALIGN_PARAGRAPH
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
import math

def select_file():
    file_path = filedialog.askopenfilename(filetypes=[("Excel files", "*.xlsx")])
    if file_path:
        entry_file_path.delete(0, tk.END)
        entry_file_path.insert(0, file_path)

def convert():
    headers, data = read_excel(entry_file_path.get())
    title = entry_title.get()
    add_page = page.get()

    if title == "":
        title = "file"
    create_docx(headers, data, f"{title}.docx", add_page)
    # create_pdf(headers, data, f"{title}.pdf")

def read_excel(file_path):
    df = pd.read_excel(file_path, engine="openpyxl").fillna("")  # Zamiana NaN na ""

    df.columns = [f"Kolumna_{i}" if col.startswith("Unnamed") else col for i, col in enumerate(df.columns)]

    headers = df.columns.tolist()
    data = df.values.tolist()
    return headers, data


def create_docx(headers, data, output_file, add_page):
    doc = Document()
    doc.add_heading('Tabela danych', level=1)
    section = doc.sections[0]
    width = section.page_width.mm - section.left_margin.mm - section.right_margin.mm
    print(f"Szerokość strony: {width} punktów")

    print(data)

    sizes = []
    for i in range (len(data[0])):
        while len(sizes) <= i:  # Upewniamy się, że istnieje odpowiedni wiersz
            sizes.append([])

        for j, row in enumerate(data):
            while len(sizes[i]) <= j:  # Upewniamy się, że istnieje odpowiednia kolumna
                sizes[i].append(0)
            sizes[i][j] = len(str(data[j][i])) * 0.381 #Średni rozmiar litery w cm
    print(sizes)

    max_col_size = 7.62
    min_col_size = 1.5
    page_width_cm = 2 * max_col_size

    column_widths = []
    for i in range(len(sizes)):
        max_width = max(sizes[i])
        # Ogranicz szerokość kolumny do zakresu [min_col_size, max_col_size]
        column_width = min(max_col_size, max(min_col_size, max_width))
        column_widths.append(column_width)

    print("Szerokości kolumn:", column_widths)


    current_width = 0
    start_col = 0  # Indeks pierwszej kolumny w bieżącej tabeli

    for i in range(len(column_widths)):
        # Jeśli dodanie kolejnej kolumny przekroczy dostępną szerokość strony, utwórz nową tabelę
        if current_width + column_widths[i] > page_width_cm:
            # Utwórz tabelę dla kolumn od start_col do i-1
            create_table(doc, headers[start_col:i], [row[start_col:i] for row in data], column_widths[start_col:i])
            start_col = i
            current_width = 0

        current_width += column_widths[i]

    # Utwórz tabelę dla pozostałych kolumn
    if start_col < len(column_widths):
        create_table(doc, headers[start_col:], [row[start_col:] for row in data], column_widths[start_col:])

    if add_page:
        for i, section in enumerate(doc.sections):
            i+=1
            footer = section.footer
            paragraph = footer.paragraphs[0] if footer.paragraphs else footer.add_paragraph()
            run = paragraph.add_run()
            run.text = "Strona "
            field_code = f'{i}'  # Pole numerowania stron
            paragraph.add_run(f' {field_code}').bold = True

            # Wyrównanie numeru strony do prawej
            paragraph.alignment = WD_ALIGN_PARAGRAPH.RIGHT

    doc.save(output_file)
    print(f"Plik DOCX zapisany: {output_file}")

def create_table(doc, headers, data, column_widths):
    doc.add_paragraph()
    table = doc.add_table(rows=1, cols=len(headers), style='Table Grid')

    # Ustaw szerokość kolumn
    for i, width in enumerate(column_widths):
        table.columns[i].width = Cm(width)

    # Nagłówki
    hdr_cells = table.rows[0].cells
    for i, header in enumerate(headers):
        hdr_cells[i].text = header

    # Dane
    for row in data:
        row_cells = table.add_row().cells
        for i, cell in enumerate(row):
            row_cells[i].text = str(cell) if cell else ""

# Tworzenie GUI
root = tk.Tk()
root.title("Konwerter XLSX do DOCX/PDF")

frame = tk.Frame(root, padx=10, pady=10)
frame.pack(padx=10, pady=10)

tk.Label(frame, text="Wybierz plik .xlsx:").grid(row=0, column=0, sticky="w")
entry_file_path = tk.Entry(frame, width=50)
entry_file_path.grid(row=0, column=1)
tk.Button(frame, text="Wybierz", command=select_file).grid(row=0, column=2, padx=10)

tk.Label(frame, text="Tytuł dokumentu:").grid(row=1, column=0, sticky="w", pady=10)
entry_title = tk.Entry(frame, width=50)
entry_title.grid(row=1, column=1, columnspan=2, sticky="w")

page = tk.BooleanVar()
check_page = tk.Checkbutton(frame, text="Numeruj strony", variable=page)
check_page.grid(row=2, column=0, pady=5)

# Przycisk konwersji
tk.Button(frame, text="Konwertuj", command=convert).grid(row=3, column=1, pady=10)

root.mainloop()


