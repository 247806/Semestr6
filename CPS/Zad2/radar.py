import tkinter as tk
from tkinter import ttk

import numpy as np

from continousSignal import sinusoidal
from myPlots import plot_counting

# --- Parametry symulacji ---
item_velocity = None        # prędkość obiektu (jednostki/s)
signal_velocity = None    # prędkość sygnału
time_step = None            # czas między krokami symulacji (s)
num_steps = None            # liczba kroków (sondań)
sampling_rate = None       # Hz, do dyskretyzacji sygnału
signal_duration = None      # długość sygnału w sekundach

def calculate_distance(probe_signal, feedback_signal):
    # Korelacja dyskretna (pełna korelacja)
    correlation = np.correlate(feedback_signal, probe_signal, mode='full')
    #correlation = cross_correlation_direct(probe_signal, feedback_signal)

    # Punkt środkowy korelacji (przy zerowym opóźnieniu)
    center_index = len(correlation) // 2
    index_of_max = center_index

    # Szukamy maksimum po prawej stronie (opóźnienie dodatnie)
    for i in range(center_index + 1, len(correlation)):
        if correlation[i] > correlation[index_of_max]:
            index_of_max = i

    # Obliczamy opóźnienie (w sekundach)
    delay_samples = index_of_max - center_index
    delay_seconds = delay_samples / sampling_rate

    # Obliczamy odległość
    distance = (delay_seconds * signal_velocity) / 2.0

    return distance


def generate_probe_signal(t, probe_signal_term):

    # Sygnał sinusoidalny: A = 1.0, f = 1 / probe_signal_term
    #sinusoidal = np.sin(2 * np.pi * t / probe_signal_term)
    sin = sinusoidal(1, probe_signal_term, t)

    # Sygnał prostokątny: A = 0.6, duty cycle = 0.3, okres = probe_signal_term / 3
    square_period = probe_signal_term / 3
    duty_cycle = 0.3
    rectangular = 0.6 * ((t % square_period) < (square_period * duty_cycle)).astype(float)
    #rectangular = square(0.6, square_period, 0, 0.3, t)

    # Suma sygnałów
    return rectangular + sin

def generate_feedback_signal(t, feedback_signal_term):
    timestamp = 0.0
    item_distance = 0.0
    distnace = []
    time = []

    probe_signals = []
    feedback_signals = []

    # --- Główna pętla ---
    for step in range(num_steps):
        # 1. Aktualizacja czasu i pozycji
        timestamp += time_step
        item_distance += item_velocity * time_step

        # 2. Obliczenie opóźnienia sygnału
        delay = (item_distance / signal_velocity) * 2.0

        # 3. Generowanie wektora czasu
        t = np.arange(0 - delay, signal_duration - delay, 1 / sampling_rate)

        # 4. Wygenerowanie sondy i feedbacku
        #probe = generate_probe_signal(t)
        feedback = generate_probe_signal(t, feedback_signal_term)  # opóźnienie czasowe

        # 5. Zapisanie sygnałów
        #probe_signals.append(probe)
        feedback_signals.append(feedback)
        distnace.append(item_distance)
        time.append(timestamp)
    return feedback_signals, distnace, time

def count_distance(param_tab, plot_1, plot_2, plot_3, quant_entry, step_entry, raport_entry, bufor_entry, sample_rate_entry, duty_cycle_entry_t, signal_velocity_entry, item_velocity_entry):
    global item_velocity, signal_velocity, time_step, num_steps, sampling_rate, signal_duration

    item_velocity = float(item_velocity_entry.get())
    signal_velocity = float(signal_velocity_entry.get())
    time_step = float(step_entry.get())
    num_steps = int(quant_entry.get())
    sampling_rate = float(sample_rate_entry.get())
    signal_duration = float(bufor_entry.get()) / sampling_rate
    probe_signal_term = float(duty_cycle_entry_t.get())

    i = 249
    t = np.arange(0, signal_duration, 1 / sampling_rate)
    d_true = generate_probe_signal(t, probe_signal_term)
    d_reported, distance, time = generate_feedback_signal(t, probe_signal_term)
    print("tablica")
    print(d_reported[i])
    dist = calculate_distance(d_true, d_reported[i])
    print(time[i])
    print(dist)
    print(distance[i])
    plot_counting(t, d_true, plot_1)
    plot_counting(t, d_reported[i], plot_2)

    # StringVar do powiązania z comboboxem
    selected_time = tk.StringVar()

    # Utworzenie comboboxa z listą wartości z time
    combo = ttk.Combobox(param_tab, textvariable=selected_time)
    combo['values'] = time  # przypisanie elementów tablicy
    combo.grid(row=9, column=0, padx=10, pady=10)

    # Opcjonalnie ustawienie wartości domyślnej (pierwszy element)
    combo.current(0)

    ttk.Label(param_tab, text=f"Obliczony dystans: {dist}").grid(row=10, column=0, padx=5, pady=5)
    ttk.Label(param_tab, text=f"Rzeczywisty dystans: {distance[combo.current()]}").grid(row=11, column=0, padx=5, pady=5)


def open_new_window(root):
    new_window = tk.Toplevel(root)
    new_window.title("Nowe Okno")
    new_window.geometry("1200x500")
    # label = ttk.Label(new_window, text="To jest nowe okno")
    # label.pack(pady=20)
    # --- GŁÓWNY NOTEBOOK ---
    main_notebook = ttk.Notebook(new_window)
    main_notebook.grid(row=0, column=0, padx=10, pady=10)

    param_tab = ttk.Frame(main_notebook)
    main_notebook.add(param_tab, text="Parametry")

    # Liczba pomiarów
    ttk.Label(param_tab, text="Liczba pomiarów:").grid(row=0, column=0, padx=5, pady=5)
    quant_var = tk.StringVar(value="10")
    quant_entry = ttk.Entry(param_tab, textvariable=quant_var)
    quant_entry.grid(row=0, column=1, padx=5, pady=5)

    # Prędkość obiektu
    ttk.Label(param_tab, text="Prędkość obiektu:").grid(row=1, column=0, padx=5, pady=5)
    item_velocity_var = tk.StringVar(value="10")
    item_velocity_entry = ttk.Entry(param_tab, textvariable=item_velocity_var)
    item_velocity_entry.grid(row=1, column=1, padx=5, pady=5)

    # Prędkość sygnału
    ttk.Label(param_tab, text="Prędkość sygnału:").grid(row=2, column=0, padx=5, pady=5)
    signal_velocity_var = tk.StringVar(value="100")
    signal_velocity_entry = ttk.Entry(param_tab, textvariable=signal_velocity_var)
    signal_velocity_entry.grid(row=2, column=1, padx=5, pady=5)

    # Okres sygnału
    ttk.Label(param_tab, text="Okres sygnału:").grid(row=3, column=0, padx=5, pady=5)
    duty_cycle_var = tk.StringVar(value="1")
    duty_cycle_entry = ttk.Entry(param_tab, textvariable=duty_cycle_var)
    duty_cycle_entry.grid(row=3, column=1, padx=5, pady=5)

    # Częstotliwość próbkowania
    ttk.Label(param_tab, text="Częstotliwość próbkowania:").grid(row=4, column=0, padx=5, pady=5)
    sample_rate_var = tk.StringVar(value="20")
    sample_rate_entry = ttk.Entry(param_tab, textvariable=sample_rate_var)
    sample_rate_entry.grid(row=4, column=1, padx=5, pady=5)

    # Długość bufora
    ttk.Label(param_tab, text="Długość bufora:").grid(row=5, column=0, padx=5, pady=5)
    bufor_var = tk.StringVar(value="60")
    bufor_entry = ttk.Entry(param_tab, textvariable=bufor_var)
    bufor_entry.grid(row=5, column=1, padx=5, pady=5)

    # Okres raportowania
    ttk.Label(param_tab, text="Okres raportowania:").grid(row=6, column=0, padx=5, pady=5)
    raport_var = tk.StringVar(value="60")
    raport_entry = ttk.Entry(param_tab, textvariable=raport_var)
    raport_entry.grid(row=6, column=1, padx=5, pady=5)

    # Jednostka czasowa
    ttk.Label(param_tab, text="Jednostka czasowa:").grid(row=7, column=0, padx=5, pady=5)
    step_var = tk.StringVar(value="0.1")
    step_entry = ttk.Entry(param_tab, textvariable=step_var)
    step_entry.grid(row=7, column=1, padx=5, pady=5)

    ttk.Button(param_tab, text="Oblicz dystans", command=lambda: count_distance(param_tab, plot_1, plot_2, plot_3, quant_entry, step_entry, raport_entry, bufor_entry, sample_rate_entry, duty_cycle_entry, signal_velocity_entry, item_velocity_entry)).grid(row=8, column=0, padx=5,
                                                                                            pady=5)

    plot_1 = ttk.Frame(new_window)
    plot_1.grid(row=0, column=2, padx=10, pady=10, sticky="nsew")

    plot_2 = ttk.Frame(new_window)
    plot_2.grid(row=1, column=2, padx=10, pady=10, sticky="nsew")

    plot_3 = ttk.Frame(new_window)
    plot_3.grid(row=2, column=2, padx=10, pady=10, sticky="nsew")




# plt.figure(figsize=(10, 5))
# plt.plot(t, d_true, label="Rzeczywista odległość")
# plt.plot(t, d_reported[i], 'o-', label="Raportowana przez czujnik")
# plt.xlabel("Czas [s]")
# plt.ylabel("Odległość [m]")
# plt.legend()
# plt.grid(True)
# plt.title("Symulacja korelacyjnego czujnika odległości")
# plt.tight_layout()
# plt.show()
