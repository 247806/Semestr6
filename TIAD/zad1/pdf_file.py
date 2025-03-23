from reportlab.lib.pagesizes import letter, A4
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import Paragraph, Spacer
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_RIGHT
from docx_file import prepare_data

def create_pdf(headers, data, output_file, add_page, column_widths, align):
    pdf = SimpleDocTemplate(output_file, pagesize=A4)
    elements = []

    styles = getSampleStyleSheet()
    elements.append(Paragraph("Tabela danych", styles['Title']))

    headers, data, widths = prepare_data(headers, data, column_widths)
    for i in range(len(headers)):
        elements.append(Spacer(1, 20))
        elements.append(create_table_pdf(headers[i], data[i], widths[i], align))

    if add_page:
        pdf.build(elements, onFirstPage=add_page_number if add_page else None, onLaterPages=add_page_number if add_page else None)
    else:
        pdf.build(elements)
    print(f"Plik PDF zapisany: {output_file}")


def add_page_number(canvas, doc):
    page_num = canvas.getPageNumber()
    text = f"Strona {page_num}"

    canvas.setFont("Helvetica", 10)

    canvas.drawCentredString(
        x=doc.pagesize[0] / 2,
        y=20,
        text=text
    )


def create_table_pdf(headers, data, column_widths, align):
    align_map = {
        "Do lewej": TA_LEFT,
        "Do środka": TA_CENTER,
        "Do prawej": TA_RIGHT,
    }

    styles = getSampleStyleSheet()

    custom_style = ParagraphStyle(
        name="CustomStyle",
        parent=styles["Normal"],
        alignment=align_map[align]
    )

    cm_to_points = lambda cm: cm * 28.35

    for i, size in enumerate(column_widths):
        column_widths[i] = cm_to_points(size)

    table_data = [[Paragraph(header, custom_style) for header in headers]]

    for row in data:
        wrapped_row = [Paragraph(str(cell), custom_style) for cell in row]
        table_data.append(wrapped_row)

    table = Table(table_data, colWidths=column_widths)

    table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.lightblue),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('GRID', (0, 0), (-1, -1), 1, colors.black),
    ]))

    return table
