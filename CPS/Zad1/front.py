import tkinter as tk
from tkinter import ttk
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import constant


# Funkcja do generowania wykresu w aplikacji
def generate_signal():
    A = float(amplitude_entry.get())
    T = float(duty_cycle_entry_t.get()) if signal_type.get() != "ones" else None
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())

    # Pobranie wartości tylko jeśli kw jest widoczne
    kw = float(duty_cycle_entry.get()) if signal_type.get() == "squareSymmetric" or signal_type.get() == "square" or signal_type.get() == "triangle" else None
    ts = float(duty_cycle_entry_ts.get()) if signal_type.get() == "ones" else None

    if signal_type.get() == "sinusoidal":
        time, signal = constant.sinusoidal(A, T, t1, d)
    elif signal_type.get() == "squareSymmetric":
        time, signal = constant.squareSymetric(A, T, t1, d, kw)
    elif signal_type.get() == 'halfWaveSinusoidal':
        time, signal = constant.halfWaveSinusoidal(A, T, t1, d)
    elif signal_type.get() == 'halfSinusoidal':
        time, signal = constant.halfSinusoidal(A, T, t1, d)
    elif signal_type.get() == 'square':
        time, signal = constant.square(A, T, t1, d, kw)
    elif signal_type.get() == 'triangle':
        time, signal = constant.triangle(A, T, t1, d, kw)
    elif signal_type.get() == 'ones':
        time, signal = constant.ones(A, t1, d, ts)

    plot_signal(time, signal, signal_type.get())


# Funkcja rysująca wykres w aplikacji
def plot_signal(time, signal, signal_type):
    global canvas

    # Usunięcie poprzedniego wykresu (jeśli istnieje)
    for widget in plot_frame.winfo_children():
        widget.destroy()

    # Tworzenie nowej figury matplotlib
    fig, ax = plt.subplots(figsize=(5, 3))
    ax.plot(time, signal, label=f"{signal_type.capitalize()} Signal")
    ax.set_xlabel("Time [s]")
    ax.set_ylabel("Amplitude")
    ax.set_title(f"{signal_type.capitalize()} Signal")
    ax.grid()
    ax.legend()

    # Osadzenie wykresu w oknie Tkinter
    canvas = FigureCanvasTkAgg(fig, master=plot_frame)
    canvas.draw()
    canvas.get_tk_widget().pack()


# Funkcja do pokazywania/ukrywania pola dla współczynnika wypełnienia
def toggle_fields():
    if signal_type.get() in ["squareSymmetric", "square", "triangle"]:
        duty_cycle_label.grid(row=5, column=0, padx=5, pady=5)
        duty_cycle_entry.grid(row=5, column=1, padx=5, pady=5)
    else:
        duty_cycle_label.grid_remove()
        duty_cycle_entry.grid_remove()

    if signal_type.get() == "ones":
        duty_cycle_label_ts.grid(row=5, column=0, padx=5, pady=5)
        duty_cycle_entry_ts.grid(row=5, column=1, padx=5, pady=5)
        duty_cycle_label_t.grid_remove()
        duty_cycle_entry_t.grid_remove()
    else:
        duty_cycle_label_ts.grid_remove()
        duty_cycle_entry_ts.grid_remove()
        duty_cycle_label_t.grid(row=1, column=0, padx=5, pady=5)
        duty_cycle_entry_t.grid(row=1, column=1, padx=5, pady=5)


# Tworzenie głównego okna aplikacji
root = tk.Tk()
root.title("Generator sygnałów")

# Etykiety i pola do wprowadzania danych
ttk.Label(root, text="Amplituda (A):").grid(row=0, column=0, padx=5, pady=5)
amplitude_entry = ttk.Entry(root)
amplitude_entry.grid(row=0, column=1, padx=5, pady=5)

# ttk.Label(root, text="Okres (T):").grid(row=1, column=0, padx=5, pady=5)
# period_entry = ttk.Entry(root)
# period_entry.grid(row=1, column=1, padx=5, pady=5)

ttk.Label(root, text="Czas początkowy (t1):").grid(row=2, column=0, padx=5, pady=5)
start_time_entry = ttk.Entry(root)
start_time_entry.grid(row=2, column=1, padx=5, pady=5)

ttk.Label(root, text="Czas trwania (d):").grid(row=3, column=0, padx=5, pady=5)
duration_entry = ttk.Entry(root)
duration_entry.grid(row=3, column=1, padx=5, pady=5)

# Opcja wyboru typu sygnału
ttk.Label(root, text="Typ sygnału:").grid(row=4, column=0, padx=5, pady=5)
signal_type = tk.StringVar(value="sinusoidal")
signal_dropdown = ttk.Combobox(root, textvariable=signal_type, values=["sinusoidal", "squareSymmetric", "halfWaveSinusoidal", "halfSinusoidal", "square", "triangle", "ones"],
                               state="readonly")
signal_dropdown.grid(row=4, column=1, padx=5, pady=5)
signal_dropdown.bind("<<ComboboxSelected>>", lambda e: toggle_fields())  # Aktualizacja przy zmianie wyboru

# Pola dla współczynnika wypełnienia i czasu skoku (ukryte domyślnie)
duty_cycle_label = ttk.Label(root, text="Współczynnik wypełnienia (kw):")
duty_cycle_entry = ttk.Entry(root)

duty_cycle_label_ts = ttk.Label(root, text="Czas skoku (ts):")
duty_cycle_entry_ts = ttk.Entry(root)

duty_cycle_label_t = ttk.Label(root, text="Okres (T):")
duty_cycle_entry_t = ttk.Entry(root)

# Przycisk do generowania wykresu
generate_button = ttk.Button(root, text="Generuj sygnał", command=generate_signal)
generate_button.grid(row=6, column=0, columnspan=2, pady=10)

# Ramka do osadzenia wykresu
plot_frame = ttk.Frame(root)
plot_frame.grid(row=7, column=0, columnspan=2, padx=10, pady=10)

# Ukrycie pola na starcie (bo domyślnie jest sinusoidal)
toggle_fields()

# Uruchomienie pętli głównej
root.mainloop()
