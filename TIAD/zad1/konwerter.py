import tkinter as tk
from tkinter import filedialog, messagebox
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

    max_col = 4

    header_parts = [headers[i:i + max_col] for i in range(0, len(headers), max_col)]
    data_parts = [[row[i:i + max_col] for row in data] for i in range(0, len(headers), max_col)]
    print(header_parts)
    print(data_parts)

    for i, element in enumerate(header_parts):
        print("Nowa tabela")
        if i > 0:
            doc.add_paragraph()
        table = doc.add_table(rows=1, cols=len(element), style='Table Grid')
        # # Nagłówki
        hdr_cells = table.rows[0].cells
        for a, header in enumerate(element):
            hdr_cells[a].text = header

        print(len(data_parts))
        # Dane
        for j, row in enumerate(data_parts[i]):
            print(data_parts[i])
            row_cells = table.add_row().cells
            for k, cell in enumerate(row):
                row_cells[k].text = str(cell) if cell else ""

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


