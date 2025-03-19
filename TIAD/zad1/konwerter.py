import tkinter as tk
from tkinter import filedialog, messagebox
import pandas as pd
from docx import Document
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas

def select_file():
    file_path = filedialog.askopenfilename(filetypes=[("Excel files", "*.xlsx")])
    if file_path:
        entry_file_path.delete(0, tk.END)
        entry_file_path.insert(0, file_path)

def convert():
    headers, data = read_excel(entry_file_path.get())

    create_docx(headers, data, "wynik.docx")
    create_pdf(headers, data, "wynik.pdf")

def read_excel(file_path):
    df = pd.read_excel(file_path, engine="openpyxl").fillna("")  # Zamiana NaN na ""

    df.columns = [f"Kolumna_{i}" if col.startswith("Unnamed") else col for i, col in enumerate(df.columns)]

    headers = df.columns.tolist()
    data = df.values.tolist()
    return headers, data


def create_docx(headers, data, output_file):
    doc = Document()
    doc.add_heading('Tabela danych', level=1)

    table = doc.add_table(rows=1, cols=len(headers))
    table.style = 'Table Grid'

    # Nagłówki
    hdr_cells = table.rows[0].cells
    for i, header in enumerate(headers):
        hdr_cells[i].text = header

    # Dane
    for row in data:
        row_cells = table.add_row().cells
        for i, cell in enumerate(row):
            row_cells[i].text = str(cell) if cell else ""  # Jeśli wartość pusta, pozostaw pustą komórkę

    doc.save(output_file)
    print(f"Plik DOCX zapisany: {output_file}")


def create_pdf(headers, data, output_file):
    c = canvas.Canvas(output_file, pagesize=letter)
    width, height = letter

    c.setFont("Helvetica", 10)
    x_offset = 50
    y_offset = height - 50
    row_height = 20

    # Nagłówki
    for i, header in enumerate(headers):
        c.drawString(x_offset + i * 100, y_offset, header)

    # Dane
    for row in data:
        y_offset -= row_height
        for i, cell in enumerate(row):
            c.drawString(x_offset + i * 100, y_offset, str(cell) if cell else "")

    c.save()
    print(f"Plik PDF zapisany: {output_file}")

# Tworzenie GUI
root = tk.Tk()
root.title("Konwerter XLSX do DOCX/PDF")

frame = tk.Frame(root, padx=10, pady=10)
frame.pack(padx=10, pady=10)

tk.Label(frame, text="Wybierz plik .xlsx:").grid(row=0, column=0, sticky="w")
entry_file_path = tk.Entry(frame, width=50)
entry_file_path.grid(row=0, column=1)
tk.Button(frame, text="Wybierz", command=select_file).grid(row=0, column=2)

tk.Label(frame, text="Tytuł dokumentu:").grid(row=1, column=0, sticky="w")
entry_title = tk.Entry(frame, width=50)
entry_title.grid(row=1, column=1, columnspan=2)

tk.Button(frame, text="Konwertuj", command=convert).grid(row=2, column=1, pady=10)

root.mainloop()


