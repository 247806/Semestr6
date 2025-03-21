from reportlab.lib.pagesizes import letter, A4
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import Paragraph

def create_pdf(headers, data, output_file, add_page, column_widths, align):
    # Tworzenie dokumentu PDF
    pdf = SimpleDocTemplate(output_file, pagesize=A4)
    elements = []
    width, height = letter  # Szerokość i wysokość w punktach
    print(f"Szerokość strony: {width} punktów ({width / 72:.2f} cali)")

    # Dodanie nagłówka
    styles = getSampleStyleSheet()
    elements.append(Paragraph("Tabela danych", styles['Title']))

    page_width_cm = 16.51
    current_width = 0
    start_col = 0  # Indeks pierwszej kolumny w bieżącej tabeli

    for i in range(len(column_widths)):
        # Jeśli dodanie kolejnej kolumny przekroczy dostępną szerokość strony, utwórz nową tabelę
        if current_width + column_widths[i] > page_width_cm:
            # Utwórz tabelę dla kolumn od start_col do i-1
            elements.append(create_table_pdf(headers[start_col:i], [row[start_col:i] for row in data], column_widths[start_col:i], align))
            start_col = i
            current_width = 0

        current_width += column_widths[i]

    # Utwórz tabelę dla pozostałych kolumn
    if start_col < len(column_widths):
        elements.append( create_table_pdf(headers[start_col:], [row[start_col:] for row in data], column_widths[start_col:], align))

    # Zapisanie dokumentu PDF
    if add_page:
        pdf.build(elements, onFirstPage=add_page_number if add_page else None, onLaterPages=add_page_number if add_page else None)
    else:
        pdf.build(elements)
    print(f"Plik PDF zapisany: {output_file}")


def add_page_number(canvas, doc):
    """
    Dodaje numer strony do stopki dokumentu.
    """
    page_num = canvas.getPageNumber()  # Pobierz numer strony
    text = f"Strona {page_num}"

    # Ustaw styl tekstu
    canvas.setFont("Helvetica", 10)

    # Umieść tekst na dole strony, wyśrodkowany
    canvas.drawCentredString(
        x=doc.pagesize[0] / 2,  # Środek strony w poziomie
        y=20,  # 20 punktów od dołu strony
        text=text
    )

def wrap_text(text, max_width):
    """
    Zawija tekst na podstawie maksymalnej szerokości kolumny.
    :param text: Tekst do zawinięcia.
    :param max_width: Maksymalna szerokość kolumny w punktach.
    :param char_width: Przybliżona szerokość jednego znaku w punktach.
    :return: Lista zawiniętych linii tekstu.
    """
    words = text.split()
    lines = []
    current_line = ""

    cm_to_points = lambda cm: cm * 28.35
    char_width = cm_to_points(0.381)

    for word in words:
        # Sprawdź, czy dodanie kolejnego słowa przekroczy maksymalną szerokość
        if len(current_line) * char_width + len(word) * char_width <= max_width:
            current_line += word + " "
        else:
            lines.append(current_line.strip())
            current_line = word + " "

    # Dodaj ostatnią linię
    if current_line:
        lines.append(current_line.strip())

    return lines

def create_table_pdf(headers, data, column_widths, align):
    align_map = {
        "Do lewej": 'LEFT',
        "Do środka": 'CENTER',
        "Do prawej": 'RIGHT',
    }

    # Przygotowanie danych do tabeli (nagłówki + dane)
    table_data = [headers]

    cm_to_points = lambda cm: cm * 28.35
    for i, size in enumerate(column_widths):
        column_widths[i] = cm_to_points(size)

    for row in data:
        wrapped_row = []
        for i, cell in enumerate(row):
            # Zawijanie tekstu dla każdej komórki
            wrapped_text = wrap_text(str(cell), column_widths[i])
            wrapped_row.append("\n".join(wrapped_text))  # Łączymy linie za pomocą \n
        table_data.append(wrapped_row)

    # Tworzenie tabeli
    table = Table(table_data, colWidths=column_widths)

    # Styl tabeli
    table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.lightblue),  # Kolor tła dla nagłówków
        ('ALIGN', (0, 0), (-1, -1), align_map[align]),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),# Wyśrodkowanie tekstu
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),  # Czcionka dla nagłówków
        ('GRID', (0, 0), (-1, -1), 1, colors.black),  # Obramowanie tabeli
    ]))

    # Dodanie tabeli do dokumentu
    return table

# from docx2pdf import convert
#
# # Ścieżka do pliku .docx
# input_docx = "file.docx"
#
# # Ścieżka do pliku .pdf (opcjonalnie)
# output_pdf = "output.pdf"
#
# # Konwersja
# convert(input_docx, output_pdf)
#
# print(f"Plik {input_docx} został przekonwertowany na {output_pdf}")
