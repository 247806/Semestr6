import tkinter as tk
from tkinter import filedialog, messagebox, ttk, simpledialog
import pandas as pd
from docx_file import create_docx, prepare_data
from pdf_file import create_pdf

TABLES = []

def select_file():
    file_path = filedialog.askopenfilename(filetypes=[("Excel files", "*.xlsx")])
    if file_path:
        entry_file_path.delete(0, tk.END)
        entry_file_path.insert(0, file_path)

        global start_row
        start_row = simpledialog.askinteger(
            "Pierwszy wiersz z danymi",
            "Podaj numer pierwszego wiersza z danymi:",
            minvalue=1,
            initialvalue=1
        ) - 1

        if start_row is not None:  # Jeśli użytkownik nie anulował okienka
            load_column_sizes(start_row)  # Indeksowanie od 0
            btn_convert.config(state="normal")
            btn_refresh.config(state="normal")

column_entries = []

def update_column_sizes():
    """ Aktualizuje listę szerokości kolumn na podstawie wartości wpisanych przez użytkownika.
        Jeśli pole jest puste, wstawia poprzednią wartość z COLUMNS_WIDTHS. """
    updated_widths = []
    print(FIRST_COLUMNS_WIDTHS)
    for i, entry in enumerate(column_entries):
        value = entry.get().strip()
        print(value)
        if value == "":  # Jeśli pole jest puste, wstaw poprzednią wartość
            value = FIRST_COLUMNS_WIDTHS[i]
            entry.insert(0, f"{value:.2f}")  # Aktualizacja pola, by nie było puste
        updated_widths.append(float(value))
    return updated_widths

def convert():
    headers, data = read_excel(entry_file_path.get(), start_row)
    title = entry_title.get()
    add_page = page.get()
    align = align_var.get()
    if title == "":
        title = "file"

    column_widths = update_column_sizes()
    create_docx(headers, data, f"{title}.docx", add_page, column_widths, align)
    create_pdf(headers, data, f"{title}.pdf", add_page, column_widths, align)

def read_excel(file_path, header=0):
    df = pd.read_excel(file_path, engine="openpyxl", header=header)

    df = df.dropna(axis=1, how='all').fillna("")
    df.columns = [f"Kolumna_{i}" if col.startswith("Unnamed") else col for i, col in enumerate(df.columns)]

    headers = df.columns.tolist()
    data = df.values.tolist()
    return headers, data

def prepare_col_sizes(data):
    sizes = []
    for i in range(len(data[0])):
        while len(sizes) <= i:  # Upewniamy się, że istnieje odpowiedni wiersz
            sizes.append([])

        for j, row in enumerate(data):
            while len(sizes[i]) <= j:  # Upewniamy się, że istnieje odpowiednia kolumna
                sizes[i].append(0)
            sizes[i][j] = len(str(data[j][i])) * 0.381  # Średni rozmiar litery w cm

    print(sizes)
    max_col_size = 8.26
    min_col_size = 1.5

    column_widths = []
    for i in range(len(sizes)):
        max_width = max(sizes[i])
        # Ogranicz szerokość kolumny do zakresu [min_col_size, max_col_size]
        column_width = min(max_col_size,  max(min_col_size, max_width))
        column_widths.append(column_width)

    print(column_widths)
    return column_widths

def validate_column_input(P):
    """ Walidacja: pozwala tylko na liczby w zakresie 0-16 oraz pusty tekst (dla edycji). """
    if P == "":  # Pozwalamy na pustą wartość w trakcie edycji
        return True
    try:
        value = float(P)
        return 0.1 <= value <= 16.52
    except ValueError:
        return False

def load_column_sizes(start_row):
    global FIRST_COLUMNS_WIDTHS
    try:
        headers, data = read_excel(entry_file_path.get(), start_row)
        if not headers or not data:
            raise ValueError("Plik nie zawiera danych")

        column_widths = prepare_col_sizes([headers] + data)
        FIRST_COLUMNS_WIDTHS = column_widths
        data1, data2, data3 = prepare_data(headers, data, column_widths)
        create_table_preview(root, data1, data2, data3)

        # Aktywacja przycisków po poprawnym załadowaniu danych
        btn_convert.config(state="normal")
        btn_refresh.config(state="normal")

    except Exception as e:
        messagebox.showerror("Błąd", f"Nie udało się wczytać pliku: {e}")
        entry_file_path.delete(0, tk.END)

        # Dezaktywacja przycisków w razie błędu
        btn_convert.config(state="disabled")
        btn_refresh.config(state="disabled")

    # Usunięcie poprzednich pól wejściowych
    for widget in column_frame.winfo_children():
        widget.destroy()

    global column_entries
    column_entries = []

    tk.Label(column_frame, text="Szerokości kolumn:").grid(row=0, column=0, columnspan=2, sticky="w")

    validate_command = root.register(validate_column_input)

    for i, width in enumerate(column_widths):
        row, col = divmod(i, 3)  # Co dwie kolumny nowy wiersz

        label = tk.Label(column_frame, text=f"Kolumna {i + 1} (cm):")
        label.grid(row=row + 1, column=col * 2, sticky="e", padx=5, pady=2)

        entry = tk.Entry(column_frame, width=10, validate="key", validatecommand=(validate_command, "%P"))
        entry.insert(0, f"{width:.2f}")  # Wstawienie domyślnej wartości
        entry.grid(row=row + 1, column=col * 2 + 1, sticky="w", padx=5, pady=2)

        column_entries.append(entry)


def load_table_data(parent, headers, data, widths):
    PIXELS_PER_CM = 37.8

    table_frame = tk.Frame(parent, bd=2, relief="solid", height=100, width=640)
    table_frame.pack_propagate(False)
    table_frame.pack(padx=10, pady=5, fill="both")

    table = ttk.Treeview(table_frame, columns=headers, show="headings")

    # Pasek przewijania
    scrollbar_x = ttk.Scrollbar(table_frame, orient="horizontal", command=table.xview)
    scrollbar_y = ttk.Scrollbar(table_frame, orient="vertical", command=table.yview)

    table.configure(xscrollcommand=scrollbar_x.set, yscrollcommand=scrollbar_y.set)

    scrollbar_x.pack(side="bottom", fill="x")
    scrollbar_y.pack(side="right", fill="y")

    # Konfiguracja kolumn
    for header, width in zip(headers, widths):
        table.heading(header, text=header)
        table.column(header, width=int(width * PIXELS_PER_CM), stretch=False)  # Przeliczenie szerokości na piksele

    # Dodanie danych
    for row in data:
        table.insert("", "end", values=row)

    table.pack(fill="both", expand=True)
    return table_frame

def create_table_preview(root, headers_list, data_list, widths_list):
    for headers, data, widths in zip(headers_list, data_list, widths_list):
        TABLES.append(load_table_data(root, headers, data, widths))

def refresh_table():
    """Aktualizuje podgląd tabeli na podstawie zmienionych szerokości kolumn."""
    column_widths = update_column_sizes()  # Pobiera nowe szerokości kolumn
    headers, data = read_excel(entry_file_path.get(), start_row)
    data1, data2, data3 = prepare_data(headers, data, column_widths)

    for table in TABLES:
        table.destroy()

    TABLES.clear()
    create_table_preview(root, data1, data2, data3)


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

# Dodanie opcji wyrównania
tk.Label(frame, text="Wyrównanie tekstu:").grid(row=3, column=0, sticky="w", pady=5)
align_var = tk.StringVar(value="Do lewej")  # Domyślne wyrównanie
alignment_options = ["Do lewej", "Do środka", "Do prawej"]
alignment_menu = ttk.Combobox(frame, textvariable=align_var, values=alignment_options, state="readonly")
alignment_menu.grid(row=3, column=1, sticky="w")

column_frame = tk.Frame(root, padx=10, pady=10)
column_frame.pack()

btn_convert = tk.Button(frame, text="Konwertuj", command=convert, state="disabled")
btn_convert.grid(row=4, column=0, pady=10)
btn_refresh = tk.Button(frame, text="Aktualizuj podgląd", command=refresh_table, state="disabled")
btn_refresh.grid(row=4, column=1, padx=10, pady=10)

root.mainloop()
