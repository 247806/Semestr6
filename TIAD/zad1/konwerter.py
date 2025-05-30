import tkinter as tk
from tkinter import filedialog, messagebox, ttk, simpledialog
import pandas as pd
from docx_file import create_docx, prepare_data
from pdf_file import create_pdf
import json
import os
from docx2pdf import convert

TABLES = []
SETTINGS_FILE = "settings.json"
column_entries = []
column_checkboxes = []

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

        if start_row is not None:
            load_column_sizes(start_row)
            btn_convert.config(state="normal")
            btn_refresh.config(state="normal")
            if os.path.exists(SETTINGS_FILE):
                btn_load_sizes.config(state="normal")

#Aktualizuje szerokości kolumn na podstawie tego co wprowadził użytkownik
def update_column_sizes():
    updated_widths = []
    for i, entry in enumerate(column_entries):
        value = entry.get().strip()
        if value == "":
            value = FIRST_COLUMNS_WIDTHS[i]
            entry.insert(0, f"{value:.2f}")
        updated_widths.append(float(value))
    return updated_widths

def convert_excel():
    headers, data = read_excel(entry_file_path.get(), start_row)
    title = entry_title.get()
    heading = entry_heading.get()
    create_table = table.get()
    add_page = page.get()
    align = align_var.get()
    if title == "":
        title = "file"
    column_widths = update_column_sizes()
    # save_settings()
    create_docx(headers, data, f"{direction}/{title}.docx", add_page, column_widths, align, heading,
                create_table, column_checkboxes)
    convert(f"{direction}/{title}.docx")
    # create_pdf(headers, data, f"{direction}/{title}.pdf", add_page, column_widths, align)

def read_excel(file_path, header=0):
    df = pd.read_excel(file_path, engine="openpyxl", header=header)

    df = df.dropna(axis=1, how='all').fillna("")
    df.columns = [f"Kolumna_{i+1}" if col.startswith("Unnamed") else col for i, col in enumerate(df.columns)]

    headers = df.columns.tolist()
    data = df.values.tolist()
    return headers, data

#Oblicza szerokości kolumn
def prepare_col_sizes(data):
    sizes = []
    for i in range(len(data[0])):
        while len(sizes) <= i:
            sizes.append([])

        for j, row in enumerate(data):
            while len(sizes[i]) <= j:
                sizes[i].append(0)
            sizes[i][j] = len(str(data[j][i])) * 0.381

    max_col_size = 12.00
    min_col_size = 1.5

    column_widths = []
    for i in range(len(sizes)):
        max_width = max(sizes[i])
        column_width = min(max_col_size,  max(min_col_size, max_width))
        column_widths.append(column_width)

    return column_widths

def validate_column_input(P):
    if P == "":
        return True
    try:
        value = float(P)
        return 0.1 <= value <= 16.52
    except ValueError:
        return False

#Odczytuje dane po wczytaniu pliku przez użytkownika i wyświetla wielkości kolumn do edycji
def load_column_sizes(start_row):
    global FIRST_COLUMNS_WIDTHS
    global column_checkboxes

    try:
        for table in TABLES:
            table.destroy()
        TABLES.clear()

        if len(column_checkboxes) != 0:
            column_checkboxes.clear()

        headers, data = read_excel(entry_file_path.get(), start_row)
        if not headers or not data:
            raise ValueError("Plik nie zawiera danych")

        column_widths = prepare_col_sizes([headers] + data)
        FIRST_COLUMNS_WIDTHS = column_widths
        data1, data2, data3 = prepare_data(headers, data, column_widths)
        create_table_preview(column_tables_frame, data1, data2, data3)

        btn_convert.config(state="normal")
        btn_refresh.config(state="normal")
        if os.path.exists(SETTINGS_FILE):
            btn_load_sizes.config(state="normal")

    except Exception as e:
        messagebox.showerror("Błąd", f"Nie udało się wczytać pliku: {e}")
        entry_file_path.delete(0, tk.END)

        btn_convert.config(state="disabled")
        btn_refresh.config(state="disabled")
        btn_load_sizes.config(state="disabled")

    load_column_sizes_preview(FIRST_COLUMNS_WIDTHS)

def load_column_sizes_preview(width):
    for widget in column_frame.winfo_children():
        widget.destroy()

    global column_entries
    column_entries = []

    tk.Label(column_frame, text="Szerokości kolumn:").grid(row=0, column=0, columnspan=1, sticky="w")

    validate_command = root.register(validate_column_input)

    for i, width in enumerate(width):
        row, col = divmod(i, 4)

        label = tk.Label(column_frame, text=f"Kolumna {i + 1} (cm):")
        label.grid(row=row + 1, column=col * 3, padx=5, pady=2)

        entry = tk.Entry(column_frame, width=5, validate="key", validatecommand=(validate_command, "%P"))
        entry.insert(0, f"{width:.2f}")
        entry.grid(row=row + 1, column=col * 3 + 1, padx=5, pady=2)

        column_entries.append(entry)

        var = tk.BooleanVar(value=True)  # Domyślnie kolumna ma być włączona
        checkbox = tk.Checkbutton(column_frame, variable=var)
        checkbox.grid(row=row + 1, column=col * 3 + 2, padx=5, pady=2)
        column_checkboxes.append(var)

#Wyświetla podgląd tabeli
def load_table_data(parent, headers, data, widths):
    PIXELS_PER_CM = 37.8

    table_frame = tk.Frame(parent, bd=2, relief="solid", height=100, width=640)
    table_frame.pack_propagate(False)
    table_frame.pack(padx=10, pady=5, fill="x", expand=False)

    table = ttk.Treeview(table_frame, columns=headers, show="headings")
    scrollbar_x = ttk.Scrollbar(table_frame, orient="horizontal", command=table.xview)
    scrollbar_y = ttk.Scrollbar(table_frame, orient="vertical", command=table.yview)

    table.configure(xscrollcommand=scrollbar_x.set, yscrollcommand=scrollbar_y.set)

    scrollbar_x.pack(side="bottom", fill="x")
    scrollbar_y.pack(side="right", fill="y")

    for header, width in zip(headers, widths):
        table.heading(header, text=header)
        table.column(header, width=int(width * PIXELS_PER_CM), stretch=False)  # Przeliczenie szerokości na piksele

    for row in data:
        table.insert("", "end", values=row)

    table.pack(fill="both", expand=True)
    return table_frame

def create_table_preview(root, headers_list, data_list, widths_list):
    for headers, data, widths in zip(headers_list, data_list, widths_list):
        TABLES.append(load_table_data(root, headers, data, widths))

def refresh_table():
    column_widths = update_column_sizes()
    headers, data = read_excel(entry_file_path.get(), start_row)
    for i in range(len(column_checkboxes) - 1, -1, -1):  # Iteracja od końca
        if not column_checkboxes[i].get():  # Jeśli checkbox jest odznaczony
            del headers[i]  # Usuń nagłówek
            for row in data:
                del row[i]
            del column_widths[i]

    data1, data2, data3 = prepare_data(headers, data, column_widths)

    # for i, entry in enumerate(column_entries):
    #     if not column_checkboxes[i].get():
    #         entry.delete(0, tk.END)  # Wyczyść pole
    #         entry.insert(0, "0.0")


    for table in TABLES:
        table.destroy()

    TABLES.clear()
    create_table_preview(column_tables_frame, data1, data2, data3)
    # save_settings()

def save_settings():
    file_name = simpledialog.askstring(
        "Zapisz ustawienia", "Podaj nazwę pliku (bez rozszerzenia):", parent=root
    )
    if not file_name:  # Jeśli użytkownik nic nie poda, zakończ funkcję
        return

    # Dodaj rozszerzenie .json, jeśli nie zostało podane
    if not file_name.endswith(".json"):
        file_name += ".json"

    # Określ ścieżkę pliku w folderze programu
    file_path = os.path.join(os.getcwd(), file_name)

    # Pobierz dane do zapisania
    settings = {
        "title": entry_title.get(),
        "page_numbering": page.get(),
        "alignment": align_var.get(),
        "column_widths": update_column_sizes(),
        "doc_title": entry_heading.get()
    }

    try:
        # Zapisz dane do pliku JSON
        with open(file_path, "w") as f:
            json.dump(settings, f, indent=4)
        messagebox.showinfo("Sukces", f"Ustawienia zostały zapisane jako {file_name} w katalogu programu.")
    except Exception as e:
        # W przypadku błędu wyświetl komunikat
        messagebox.showerror("Błąd", f"Nie udało się zapisać ustawień: {e}")


def load_settings():
    file_path = filedialog.askopenfilename(
        title="Wczytaj ustawienia",
        filetypes=[("JSON files", "*.json")],
        initialdir=os.getcwd(),
    )
    if not file_path:
        return
    try:
        with open(file_path, "r") as f:
            settings = json.load(f)

        entry_title.insert(0, settings.get("title", ""))
        page.set(settings.get("page_numbering", False))
        align_var.set(settings.get("alignment", "Do lewej"))
        entry_heading.insert(0, settings.get("doc_title", ""))

    except Exception as e:
        messagebox.showerror("Błąd", f"Nie udało się wczytać ustawień: {e}")


#Wczytuje szerokości kolumn z pliku
def load_col_sizes():
    if os.path.exists(SETTINGS_FILE):
        with open(SETTINGS_FILE, "r") as f:
            settings = json.load(f)

    if "column_widths" in settings:
        for i, width in enumerate(settings["column_widths"]):
            if i < len(column_entries):
                column_entries[i].delete(0, tk.END)
                column_entries[i].insert(0, f"{width:.2f}")
    refresh_table()

def choose_dir():
    global direction
    direction = filedialog.askdirectory()
    if direction:
        path_var.set(direction)

#GUI
root = tk.Tk()
root.title("Konwerter XLSX do DOCX/PDF")
# root.after(100, load_settings)

path_var = tk.StringVar()

frame = tk.Frame(root, padx=10, pady=10)
frame.pack(padx=10, pady=10)

tk.Label(frame, text="Wybierz plik .xlsx:").grid(row=0, column=0, sticky="w")
entry_file_path = tk.Entry(frame, width=50)
entry_file_path.grid(row=0, column=1)
tk.Button(frame, text="Wybierz", command=select_file).grid(row=0, column=2, padx=10)

tk.Label(frame, text="Wybierz folder zapisu:").grid(row=1, column=0, sticky="w", pady=5)
entry_path = tk.Entry(frame, textvariable=path_var, width=50)
entry_path.grid(row=1, column=1)
tk.Button(frame, text="Wybierz", command=choose_dir).grid(row=1, column=2, padx=10)

tk.Label(frame, text="Tytuł pliku:").grid(row=2, column=0, sticky="w", pady=10)
entry_title = tk.Entry(frame, width=50)
entry_title.grid(row=2, column=1, columnspan=2, sticky="w")

tk.Label(frame, text="Tytuł dokumentu:").grid(row=3, column=0, sticky="w", pady=10)
entry_heading = tk.Entry(frame, width=50)
entry_heading.grid(row=3, column=1, columnspan=2, sticky="w")

page = tk.BooleanVar()
check_page = tk.Checkbutton(frame, text="Numeruj strony", variable=page)
check_page.grid(row=4, column=0, pady=5)

table = tk.BooleanVar()
check_table = tk.Checkbutton(frame, text="Tabela", variable=table)
check_table.grid(row=4, column=1, pady=5)

tk.Label(frame, text="Wyrównanie tekstu:").grid(row=4, column=1, sticky="e", pady=5)
align_var = tk.StringVar(value="Do lewej")
alignment_options = ["Do lewej", "Do środka", "Do prawej"]
alignment_menu = ttk.Combobox(frame, textvariable=align_var, values=alignment_options, state="readonly")
alignment_menu.grid(row=4, column=2, sticky="w")

btn_save_settings = tk.Button(frame, text="Zapisz ustawienia...", command=save_settings)
btn_save_settings.grid(row=5, column=0, pady=10)

btn_load_settings = tk.Button(frame, text="Wczytaj ustawienia...", command=load_settings)
btn_load_settings.grid(row=5, column=1, pady=10)


column_frame = tk.Frame(root, padx=10, pady=10)
column_frame.pack()

scroll_canvas = tk.Canvas(root)
scroll_canvas.pack(side="left", fill="both", expand=True, padx=10, pady=10)

scrollbar = ttk.Scrollbar(root, orient="vertical", command=scroll_canvas.yview)
scrollbar.pack(side="right", fill="y")

scroll_canvas.configure(yscrollcommand=scrollbar.set)
scroll_canvas.bind('<Configure>', lambda e: scroll_canvas.configure(scrollregion=scroll_canvas.bbox("all")))

column_tables_frame = tk.Frame(scroll_canvas)
scroll_canvas.create_window((0, 0), window=column_tables_frame, anchor="n")


btn_convert = tk.Button(frame, text="Konwertuj", command=convert_excel, state="disabled")
btn_convert.grid(row=6, column=0, pady=10)
btn_refresh = tk.Button(frame, text="Aktualizuj podgląd", command=refresh_table, state="disabled")
btn_refresh.grid(row=6, column=1, padx=10, pady=10)
btn_load_sizes = tk.Button(frame, text="Wczytaj kolumny", command=load_col_sizes, state="disabled")
btn_load_sizes.grid(row=6, column=2, padx=10, pady=10)

# entry_title.bind("<KeyRelease>", lambda e: save_settings())
# alignment_menu.bind("<<ComboboxSelected>>", lambda e: save_settings())
# check_page.config(command=save_settings)

root.mainloop()
