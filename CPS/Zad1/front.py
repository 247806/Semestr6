import tkinter as tk
from tkinter import ttk
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import continousSignal
import discretSignal
import calculateParams as cp
signal = None

# Funkcja do generowania wykresu w aplikacji
def generate_signal():
    global signal
    A = float(amplitude_entry.get())
    T = float(duty_cycle_entry_t.get()) if signal_type.get() not in ["ones", "random_uniform_signal", "gaussian_noise", "delta_diraca", "impuls_noise"] else None
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())

    # Pobranie wartości tylko jeśli kw jest widoczne
    kw = float(duty_cycle_entry.get()) if signal_type.get() in ["squareSymmetric", "square", "triangle"] else None
    ts = float(duty_cycle_entry_ts.get()) if signal_type.get() in ["ones", "delta_diraca"] else None
    p = float(duty_cycle_entry_p.get()) if signal_type.get() == "impuls_noise" else None

    sample_rate = float(sample_rate_entry.get())
    time = np.arange(t1, d, 1 / sample_rate)

    if signal_type.get() == "sinusoidal":
        signal = continousSignal.sinusoidal(A, T, t1, d, time)
    elif signal_type.get() == "squareSymmetric":
        signal = continousSignal.squareSymetric(A, T, t1, d, kw, time)
    elif signal_type.get() == 'halfWaveSinusoidal':
        signal = continousSignal.halfWaveSinusoidal(A, T, t1, d, time)
    elif signal_type.get() == 'halfSinusoidal':
        signal = continousSignal.halfSinusoidal(A, T, t1, d, time)
    elif signal_type.get() == 'square':
        signal = continousSignal.square(A, T, t1, d, kw, time)
    elif signal_type.get() == 'triangle':
        signal = continousSignal.triangle(A, T, t1, d, kw, time)
    elif signal_type.get() == 'ones':
        signal = continousSignal.ones(A, t1, d, ts, time)
    elif signal_type.get() == 'random_uniform_signal':
        signal = continousSignal.random_uniform_signal(A, t1, d, time)
    elif signal_type.get() == 'gaussian_noise':
        signal = continousSignal.gaussian_noise(A, t1, d, time)
    elif signal_type.get() == 'delta_diraca':
        time, signal = discretSignal.delta_diraca(A, t1, ts, d, sample_rate)
    elif signal_type.get() == 'impuls_noise':
        time, signal = discretSignal.impuls_noise(A, t1, d, sample_rate, p)

    plot_signal(time, signal, signal_type.get())
    print(cp.avg_dis(signal, time))
    return signal

# Funkcja rysująca wykres w aplikacji
def plot_signal(time, signal, signal_type):
    global canvas

    # Usunięcie poprzedniego wykresu (jeśli istnieje)
    for widget in plot_frame.winfo_children():
        widget.destroy()

    for widget in histogram_frame.winfo_children():
        widget.destroy()

    if signal_type in ["delta_diraca", "impuls_noise"]:
    # Tworzenie nowej figury matplotlib
        fig, ax = plt.subplots(figsize=(5, 3))
        ax.scatter(time, signal, label=f"{signal_type.capitalize()} Signal")
        ax.set_xlabel("Time [s]")
        ax.set_ylabel("Amplitude")
        ax.set_title(f"{signal_type.capitalize()} Signal")
        ax.grid()
        ax.legend()
    else:
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


def plot_histogram():
    print(signal)
    for widget in histogram_frame.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots(figsize=(5, 3))
    ax.hist(signal, bins=int(bin_slider.get()), alpha=0.7, color='blue', edgecolor='black')
    ax.set_xlabel("Amplitude")
    ax.set_ylabel("Frequency")
    ax.set_title(f"Histogram - {signal_type.get().capitalize()} Signal")

    canvas = FigureCanvasTkAgg(fig, master=histogram_frame)
    canvas.draw()
    canvas.get_tk_widget().pack()


# Funkcja do pokazywania/ukrywania pola dla współczynnika wypełnienia
def toggle_fields():
    if signal_type.get() in ["squareSymmetric", "square", "triangle"]:
        duty_cycle_label.grid(row=6, column=0, padx=5, pady=5)
        duty_cycle_entry.grid(row=6, column=1, padx=5, pady=5)
    else:
        duty_cycle_label.grid_remove()
        duty_cycle_entry.grid_remove()

    if signal_type.get() in ["ones","delta_diraca"]:
        duty_cycle_label_ts.grid(row=6, column=0, padx=5, pady=5)
        duty_cycle_entry_ts.grid(row=6, column=1, padx=5, pady=5)
    else:
        duty_cycle_label_ts.grid_remove()
        duty_cycle_entry_ts.grid_remove()

    if signal_type.get() == "impuls_noise":
        duty_cycle_label_p.grid(row=6, column=0, padx=5, pady=5)
        duty_cycle_entry_p.grid(row=6, column=1, padx=5, pady=5)
    else:
        duty_cycle_label_p.grid_remove()
        duty_cycle_entry_p.grid_remove()


    if signal_type.get() in ["random_uniform_signal", "ones", "gaussian_noise", "delta_diraca", "impuls_noise"]:
        duty_cycle_label_t.grid_remove()
        duty_cycle_entry_t.grid_remove()
    else:
        duty_cycle_label_t.grid(row=3, column=0, padx=5, pady=5)
        duty_cycle_entry_t.grid(row=3, column=1, padx=5, pady=5)

# Tworzenie głównego okna aplikacji
root = tk.Tk()
root.title("Generator sygnałów")

# Etykiety i pola do wprowadzania danych
# Opcja wyboru typu sygnału
ttk.Label(root, text="Typ sygnału:").grid(row=0, column=0, padx=5, pady=5)
signal_type = tk.StringVar(value="sinusoidal")
signal_dropdown = ttk.Combobox(root, textvariable=signal_type, values=["random_uniform_signal", "gaussian_noise", "sinusoidal", "squareSymmetric", "halfWaveSinusoidal", "halfSinusoidal", "square", "triangle",  "ones", "delta_diraca", "impuls_noise"],
                               state="readonly")
signal_dropdown.grid(row=0, column=1, padx=5, pady=5)
signal_dropdown.bind("<<ComboboxSelected>>", lambda e: toggle_fields())  # Aktualizacja przy zmianie wyboru

sample_rate_var = tk.StringVar(value="1000")  # Ustaw domyślną wartość np. 1000 Hz

ttk.Label(root, text="Częstotliwość próbkowania").grid(row=1, column=0, padx=5, pady=5)
sample_rate_entry = ttk.Entry(root, textvariable=sample_rate_var)  # Przypisanie zmiennej
sample_rate_entry.grid(row=1, column=1, padx=5, pady=5)

ttk.Label(root, text="Amplituda (A):").grid(row=2, column=0, padx=5, pady=5)
amplitude_entry = ttk.Entry(root)
amplitude_entry.grid(row=2, column=1, padx=5, pady=5)

ttk.Label(root, text="Czas początkowy (t1):").grid(row=4, column=0, padx=5, pady=5)
start_time_entry = ttk.Entry(root)
start_time_entry.grid(row=4, column=1, padx=5, pady=5)

ttk.Label(root, text="Czas trwania (d):").grid(row=5, column=0, padx=5, pady=5)
duration_entry = ttk.Entry(root)
duration_entry.grid(row=5, column=1, padx=5, pady=5)

# Pola dla współczynnika wypełnienia i czasu skoku (ukryte domyślnie)
duty_cycle_label = ttk.Label(root, text="Współczynnik wypełnienia (kw):")
duty_cycle_entry = ttk.Entry(root)

duty_cycle_label_ts = ttk.Label(root, text="Czas skoku (ts):")
duty_cycle_entry_ts = ttk.Entry(root)

duty_cycle_label_t = ttk.Label(root, text="Okres (T):")
duty_cycle_entry_t = ttk.Entry(root)

duty_cycle_label_p = ttk.Label(root, text="Prawdopodobieństwo (p):")
duty_cycle_entry_p = ttk.Entry(root)

# Slider do ustawienia liczby przedziałów histogramu
bin_slider = tk.Scale(root, from_=5, to=20, orient=tk.HORIZONTAL, label="Liczba przedziałów histogramu")
bin_slider.set(10)
bin_slider.grid(row=6, column=0, columnspan=2, pady=5)

# Przycisk do generowania wykresu
generate_button = ttk.Button(root, text="Generuj sygnał", command=generate_signal)
generate_button.grid(row=7, column=0, padx=5, pady=5)

generate_button = ttk.Button(root, text="Generuj histogram", command=plot_histogram)
generate_button.grid(row=7, column=1, padx=5, pady=5)

# Ramka do osadzenia wykresu
plot_frame = ttk.Frame(root)
plot_frame.grid(row=8, column=0, columnspan=2, padx=10, pady=10)

histogram_frame = ttk.Frame(root)
histogram_frame.grid(row=9, column=0, columnspan=2, padx=10, pady=10)

# Ukrycie pola na starcie (bo domyślnie jest sinusoidal)
toggle_fields()

# Uruchomienie pętli głównej
root.mainloop()
