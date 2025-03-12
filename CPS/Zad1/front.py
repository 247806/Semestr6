import tkinter as tk
from tkinter import ttk
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

# Funkcje generujące sygnały
def sinusoidal(A, T, t1, d):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
    signal = A * np.sin((2 * np.pi / T) * time)
    return time, signal

def squareSymmetric(A, T, t1, d, kw):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
    signal = np.zeros(len(time))
    k = 0
    i = 0
    for times in time:
        if times >= k * T + t1 and times < kw * T + k * T + t1:
            signal[i] = A
        elif times >= kw * T - k * T + t1 and times < T + k * T + t1:
            signal[i] = -A
        if times == T * (k + 1):
            k += 1
        i += 1
    return time, signal

# Funkcja do generowania wykresu w aplikacji
def generate_signal():
    A = float(amplitude_entry.get())
    T = float(period_entry.get())
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())
    kw = float(duty_cycle_entry.get()) if signal_type.get() == "squareSymmetric" else None

    if signal_type.get() == "sinusoidal":
        time, signal = sinusoidal(A, T, t1, d)
    elif signal_type.get() == "squareSymmetric":
        time, signal = squareSymmetric(A, T, t1, d, kw)

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

# Tworzenie głównego okna aplikacji
root = tk.Tk()
root.title("Generator sygnałów")

# Etykiety i pola do wprowadzania danych
ttk.Label(root, text="Amplituda (A):").grid(row=0, column=0, padx=5, pady=5)
amplitude_entry = ttk.Entry(root)
amplitude_entry.grid(row=0, column=1, padx=5, pady=5)

ttk.Label(root, text="Okres (T):").grid(row=1, column=0, padx=5, pady=5)
period_entry = ttk.Entry(root)
period_entry.grid(row=1, column=1, padx=5, pady=5)

ttk.Label(root, text="Czas początkowy (t1):").grid(row=2, column=0, padx=5, pady=5)
start_time_entry = ttk.Entry(root)
start_time_entry.grid(row=2, column=1, padx=5, pady=5)

ttk.Label(root, text="Czas trwania (d):").grid(row=3, column=0, padx=5, pady=5)
duration_entry = ttk.Entry(root)
duration_entry.grid(row=3, column=1, padx=5, pady=5)

# Opcja wyboru typu sygnału
ttk.Label(root, text="Typ sygnału:").grid(row=4, column=0, padx=5, pady=5)
signal_type = tk.StringVar(value="sinusoidal")
signal_dropdown = ttk.Combobox(root, textvariable=signal_type, values=["sinusoidal", "squareSymmetric"])
signal_dropdown.grid(row=4, column=1, padx=5, pady=5)

# Pole dla współczynnika wypełnienia (tylko dla squareSymmetric)
ttk.Label(root, text="Współczynnik wypełnienia (kw):").grid(row=5, column=0, padx=5, pady=5)
duty_cycle_entry = ttk.Entry(root)
duty_cycle_entry.grid(row=5, column=1, padx=5, pady=5)

# Przycisk do generowania wykresu
generate_button = ttk.Button(root, text="Generuj sygnał", command=generate_signal)
generate_button.grid(row=6, column=0, columnspan=2, pady=10)

# Ramka do osadzenia wykresu
plot_frame = ttk.Frame(root)
plot_frame.grid(row=7, column=0, columnspan=2, padx=10, pady=10)

# Uruchomienie pętli głównej
root.mainloop()
