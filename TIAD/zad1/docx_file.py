from docx import Document
from docx.shared import Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

def create_docx(headers, data, output_file, add_page, column_widths, align):
    doc = Document()
    doc.add_heading('Tabela danych', level=1)
    section = doc.sections[0]
    width = section.page_width.mm - section.left_margin.mm - section.right_margin.mm
    print(f"Szerokość strony: {width} punktów")

    page_width_cm = 15.24
    current_width = 0
    start_col = 0  # Indeks pierwszej kolumny w bieżącej tabeli

    for i in range(len(column_widths)):
        # Jeśli dodanie kolejnej kolumny przekroczy dostępną szerokość strony, utwórz nową tabelę
        if current_width + column_widths[i] > page_width_cm:
            # Utwórz tabelę dla kolumn od start_col do i-1
            create_table(doc, headers[start_col:i], [row[start_col:i] for row in data], column_widths[start_col:i], align)
            start_col = i
            current_width = 0

        current_width += column_widths[i]

    # Utwórz tabelę dla pozostałych kolumn
    if start_col < len(column_widths):
        create_table(doc, headers[start_col:], [row[start_col:] for row in data], column_widths[start_col:], align)

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
            paragraph.alignment = WD_ALIGN_PARAGRAPH.CENTER

    doc.save(output_file)
    print(f"Plik DOCX zapisany: {output_file}")

def create_table(doc, headers, data, column_widths, align):
    print(align)
    align_map = {
        "Do lewej": WD_ALIGN_PARAGRAPH.LEFT,
        "Do środka": WD_ALIGN_PARAGRAPH.CENTER,
        "Do prawej": WD_ALIGN_PARAGRAPH.RIGHT,
    }

    doc.add_paragraph()
    table = doc.add_table(rows=1, cols=len(headers), style='Table Grid')
    print(column_widths)
    # Ustaw szerokość kolumn
    for i, width in enumerate(column_widths):
        print(width)
        # table.columns[i].alignment = align_map[align]
        table.columns[i].width = Cm(width)

    # Nagłówki
    hdr_cells = table.rows[0].cells
    for i, header in enumerate(headers):
        hdr_cells[i].text = header
        paragraph = hdr_cells[i].paragraphs[0]
        paragraph.alignment = align_map[align]
        run = paragraph.runs[0]
        run.bold = True

        shading_elm = OxmlElement('w:shd')
        shading_elm.set(qn('w:fill'), '2E75B6')  # Kod koloru HEX (ciemnoniebieski)
        hdr_cells[i]._tc.get_or_add_tcPr().append(shading_elm)


    # Dane
    for row in data:
        row_cells = table.add_row().cells
        for i, cell in enumerate(row):
            row_cells[i].text = str(cell) if cell else ""
            paragraph = row_cells[i].paragraphs[0]
            paragraph.alignment = align_map[align]