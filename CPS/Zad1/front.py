import struct
import tkinter as tk
from tkinter import ttk, filedialog
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import continousSignal
import discretSignal
import calculateParams as cp
import signalOperation as so

signal_1 = None
time_1 = None
signal_2 = None
time_2 = None
signal_3 = None
time_3 = None

def function_type(A, T, t1, d, kw, ts, p, signal):
    sample_rate = float(sample_rate_entry.get())
    time = np.arange(t1, d, 1 / sample_rate)

    if signal_type.get() == "Sygnał sinusoidalny":
        signal = continousSignal.sinusoidal(A, T, time)
    elif signal_type.get() == "Sygnał prostokątny symetryczny":
        signal = continousSignal.squareSymetric(A, T, t1, kw, time)
    elif signal_type.get() == 'Sygnał sinusoidalny wyprostowany jednopołówkowo':
        signal = continousSignal.halfWaveSinusoidal(A, T, time)
    elif signal_type.get() == 'Sygnał sinusoidalny wyprostowany dwupołówkowo ':
        signal = continousSignal.halfSinusoidal(A, T, time)
    elif signal_type.get() == 'Sygnał prostokątny':
        signal = continousSignal.square(A, T, t1, kw, time)
    elif signal_type.get() == 'Sygnał trójkątny':
        signal = continousSignal.triangle(A, T, t1, kw, time)
    elif signal_type.get() == 'Skok jednostkowy':
        signal = continousSignal.ones(A, ts, time)
    elif signal_type.get() == 'Szum o rozkładzie jednostajnym':
        signal = continousSignal.random_uniform_signal(A, time)
    elif signal_type.get() == 'Szum gaussowski':
        signal = continousSignal.gaussian_noise(A, time)
    elif signal_type.get() == 'Impuls jednostkowy':
        time, signal = discretSignal.delta_diraca(A, t1, ts, d, sample_rate)
    elif signal_type.get() == 'Szum impulsowy':
        time, signal = discretSignal.impuls_noise(A, t1, d, sample_rate, p)

    return time, signal

def generate_signal():
    global signal_1, time_1, signal_2, time_2

    A = float(amplitude_entry.get())
    T = float(duty_cycle_entry_t.get()) if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"] else None
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())
    kw = float(duty_cycle_entry.get()) if signal_type.get() in ["Sygnał prostokątny symetryczny", "Sygnał prostokątny", "Sygnał trójkątny"] else None
    ts = float(duty_cycle_entry_ts.get()) if signal_type.get() in ["Skok jednostkowy", "Impuls jednostkowy"] else None
    p = float(duty_cycle_entry_p.get()) if signal_type.get() == "Szum impulsowy" else None


    if signal_notebook.index(signal_notebook.select()) == 1:
        time_2, signal_2 = function_type(A, T, t1, d, kw, ts, p, signal_2)
        plot_signal(time_2, signal_2, signal_type.get(), plot_frame_2, histogram_frame_2)
        create_parameters_tab(param_frame_2, signal_2, time_2)
        plot_histogram(histogram_frame_2, signal_2)

    else:
        time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1)
        plot_signal(time_1, signal_1, signal_type.get(), plot_frame_1, histogram_frame_1)
        create_parameters_tab(param_frame_1, signal_1, time_1)
        plot_histogram(histogram_frame_1, signal_1)


    if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"] and d % T != 0:
        full_periods = int(d // T)
        d = full_periods * T
        time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1)

def plot_signal(time, signal, signal_types, plot, histogram):

    for widget in plot.winfo_children():
        widget.destroy()

    for widget in histogram.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    if signal_types in ["Impuls jednostkowy", "Szum impulsowy"]:
        ax.scatter(time, signal)
    else:
        ax.plot(time, signal)
    ax.set_title("Wykres sygnału")
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=plot)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=True, fill='both', padx=5, pady=5)

def histogram_managment():
    if signal_notebook.index(signal_notebook.select()) == 0:
        plot_histogram(histogram_frame_1, signal_1)
    elif signal_notebook.index(signal_notebook.select()) == 1:
        plot_histogram(histogram_frame_2, signal_2)
    else:
        plot_histogram(histogram_frame_3, signal_3)

def plot_histogram(frame, signal):
    for widget in frame.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    ax.hist(signal, bins=int(bins_var.get()), alpha=0.7, color='blue', edgecolor='black')
    ax.set_ylabel("Amplituda")
    ax.set_title("Histogram")
    ax.set_xlabel("Częstotliwość")

    canvas = FigureCanvasTkAgg(fig, master=frame)
    canvas.draw()
    canvas.get_tk_widget().pack()

def toggle_fields():
    if signal_type.get() in ["Sygnał prostokątny symetryczny", "Sygnał prostokątny", "Sygnał trójkątny"]:
        duty_cycle_label.grid(row=6, column=0, padx=5, pady=5)
        duty_cycle_entry.grid(row=6, column=1, padx=5, pady=5)
    else:
        duty_cycle_label.grid_remove()
        duty_cycle_entry.grid_remove()

    if signal_type.get() in ["Skok jednostkowy","Impuls jednostkowy"]:
        duty_cycle_label_ts.grid(row=6, column=0, padx=5, pady=5)
        duty_cycle_entry_ts.grid(row=6, column=1, padx=5, pady=5)
    else:
        duty_cycle_label_ts.grid_remove()
        duty_cycle_entry_ts.grid_remove()

    if signal_type.get() == "Szum impulsowy":
        duty_cycle_label_p.grid(row=6, column=0, padx=5, pady=5)
        duty_cycle_entry_p.grid(row=6, column=1, padx=5, pady=5)
    else:
        duty_cycle_label_p.grid_remove()
        duty_cycle_entry_p.grid_remove()


    if signal_type.get() in ["Szum o rozkładzie jednostajnym", "Skok jednostkowy", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"]:
        duty_cycle_label_t.grid_remove()
        duty_cycle_entry_t.grid_remove()
    else:
        duty_cycle_label_t.grid(row=3, column=0, padx=5, pady=5)
        duty_cycle_entry_t.grid(row=3, column=1, padx=5, pady=5)

def create_parameters_tab(param, signal, time):
    if signal_type.get() not in ["Impuls jednostkowy", "Szum impulsowy"]:
        avg_label = ttk.Label(param, text=f"Średnia: {cp.avg_cont(signal, time):.2f}")
        avg_label.pack(padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_cont(signal, time):.2f}")
        abs_avg_label.pack(padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_cont(signal, time):.2f}")
        power_label.pack(padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_cont(signal, time):.2f}")
        dev_label.pack(padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_cont(signal, time):.2f}")
        eff_power_label.pack(padx=5, pady=5)

    else:
        avg_label = ttk.Label(param, text=f"Średnia: {cp.avg_dis(signal, time):.2f}")
        avg_label.pack(padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_dis(signal, time):.2f}")
        abs_avg_label.pack(padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_dis(signal, time):.2f}")
        power_label.pack(padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_dis(signal, time):.2f}")
        dev_label.pack(padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_dis(signal, time):.2f}")
        eff_power_label.pack(padx=5, pady=5)

def plot_empty_chart():
    fig, ax = plt.subplots()
    ax.set_title("Wykres sygnału")
    ax.set_xlabel("Czas")
    ax.set_ylabel("Amplituda")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_3)
    canvas.get_tk_widget().pack()
    canvas.draw()

    fig, ax = plt.subplots()
    ax.set_ylabel("Amplituda")
    ax.set_title("Histogram")
    ax.set_xlabel("Częstotliwość")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_3)
    canvas.get_tk_widget().pack()
    canvas.draw()

def increase_bins():
    bins_var.set(min(bins_var.get() + 1, 20))

def decrease_bins():
    bins_var.set(max(bins_var.get() - 1, 5))

def operate_signals_sel(operation, signal_1_sel, time_1_sel, signal_2_sel, time_2_sel):
    global signal_3, time_3
    signal_3, time_3 = so.operate_signals(operation, signal_1_sel, time_1_sel, signal_2_sel, time_2_sel)
    plot_signal(time_3, signal_3, "Wynik", plot_frame_3, histogram_frame_3)
    plot_histogram(histogram_frame_3, signal_3)
    create_parameters_tab(param_frame_3, signal_3, time_3)

def operate_signals(operation):
    global signal_1, time_1, signal_2, time_2, signal_3, time_3
    signal_3, time_3 = so.operate_signals(operation, signal_1, time_1, signal_2, time_2)
    plot_signal(time_3, signal_3, "Wynik", plot_frame_3, histogram_frame_3)
    plot_histogram(histogram_frame_3, signal_3)
    create_parameters_tab(param_frame_3, signal_3, time_3)

def save_signal():
    global signal_1, time_1
    if signal_notebook.index(signal_notebook.select()) == 0:
        signal = signal_1
        time = time_1
    elif signal_notebook.index(signal_notebook.select()) == 1:
        signal = signal_2
        time = time_2
    else:
        signal = signal_3
        time = time_3

    if time is None or signal is None:
        print("Brak danych do zapisania")
        return

    if len(time) != len(signal):
        print("Długości time i signal muszą się zgadzać")
        return

    start_time = time[0]

    sampling_rate = 1 / (time[1] - time[0]) if len(time) > 1 else 1.0

    num_samples = len(signal)

    max_amplitude = max(abs(s) for s in signal)

    file_path = filedialog.asksaveasfilename(defaultextension=".bin",
                                             filetypes=[("Binary Files", "*.bin"), ("All Files", "*.*")])
    if not file_path:
        return

    with open(file_path, mode='wb') as file:
        file.write(struct.pack('d', start_time))
        file.write(struct.pack('d', sampling_rate))
        file.write(struct.pack('i', num_samples))
        file.write(struct.pack('d', max_amplitude))

        for t, s in zip(time, signal):
            file.write(struct.pack('d', t))
            file.write(struct.pack('d', s))

    print(f"Plik zapisany: {file_path}")

def load_signal(type):
    global time_1, signal_1, signal_2, time_2, signal_3, time_3
    file_path = filedialog.askopenfilename(filetypes=[("Binary Files", "*.bin"), ("All Files", "*.*")])
    if not file_path:
        return

    time_temp, signal_temp = [], []

    with open(file_path, mode='rb') as file:
        start_time = struct.unpack('d', file.read(8))[0]
        sampling_rate = struct.unpack('d', file.read(8))[0]
        num_samples = struct.unpack('i', file.read(4))[0]
        max_amplitude = struct.unpack('d', file.read(8))[0]

        for _ in range(num_samples):
            t = struct.unpack('d', file.read(8))[0]  # czas
            s = struct.unpack('d', file.read(8))[0]  # amplituda
            time_temp.append(t)
            signal_temp.append(s)

    if signal_notebook.index(signal_notebook.select()) == 0:
        time_1 = np.array(time_temp)
        signal_1 = np.array(signal_temp)
        signal = signal_1
        time = time_1
        plot = plot_frame_1
        histogram = histogram_frame_1
        param = param_frame_1
    elif signal_notebook.index(signal_notebook.select()) == 1:
        time_2 = np.array(time_temp)
        signal_2 = np.array(signal_temp)
        signal = signal_2
        time = time_2
        plot = plot_frame_2
        histogram = histogram_frame_2
        param = param_frame_2
    else:
        return

    if type == 0:
        plot_signal(time, signal, "Załadowany sygnał", plot, histogram)
        create_parameters_tab(param, signal, time)
        plot_histogram(histogram, signal)
    else:
        display_data_in_popup(time, signal, start_time, sampling_rate, num_samples, max_amplitude)

    print(f"\nPlik wczytany: {file_path}")


def display_data_in_popup(time_data, signal_data, start_time, sampling_rate, num_samples, max_amplitude):
    popup = tk.Toplevel(root)
    popup.title("Dane z pliku")
    popup.geometry("600x400")

    text_box = tk.Text(popup, wrap=tk.WORD, height=23, width=70)
    text_box.pack(padx=10, pady=10)

    text_box.insert(tk.END, f"Czas początkowy: {start_time}\n")
    text_box.insert(tk.END, f"Częstotliwość próbkowania: {sampling_rate}\n")
    text_box.insert(tk.END, f"Liczba próbek: {num_samples}\n")
    text_box.insert(tk.END, f"Maksymalna amplitiuda: {max_amplitude}\n")
    text_box.insert(tk.END, "\nCzas\tAmplituda\n")

    for t, s in zip(time_data, signal_data):
        text_box.insert(tk.END, f"{t:.6f}\t{s:.6f}\n")

    text_box.config(state=tk.DISABLED)


def select_signals_for_operation(operation):
    global signal_1, time_1, signal_2, time_2

    popup = tk.Toplevel(root)
    popup.title("Wybór sygnałów do operacji")
    popup.geometry("400x250")

    ttk.Label(popup, text="Wybierz sygnały do operacji:").pack(pady=10)

    signal_options = ["Sygnał 1", "Sygnał 2"]
    signal_1_var = tk.StringVar(value=signal_options[0])
    signal_2_var = tk.StringVar(value=signal_options[1])

    ttk.Label(popup, text="Sygnał 1:").pack()
    signal_1_dropdown = ttk.Combobox(popup, textvariable=signal_1_var, values=signal_options, state="readonly")
    signal_1_dropdown.pack()

    ttk.Label(popup, text="Sygnał 2:").pack()
    signal_2_dropdown = ttk.Combobox(popup, textvariable=signal_2_var, values=signal_options, state="readonly")
    signal_2_dropdown.pack()

    def confirm_selection(operation):
        if signal_1_var.get() == "Sygnał 1" and signal_2_var.get() == "Sygnał 2":
            selected_signal_1 = signal_1
            selected_time_1 = time_1
            selected_signal_2 = signal_2
            selected_time_2 = time_2
        elif signal_1_var.get() == "Sygnał 2" and signal_2_var.get() == "Sygnał 1":
            selected_signal_1 = signal_2
            selected_time_1 = time_2
            selected_signal_2 = signal_1
            selected_time_2 = time_1

        print(f"Wybrano {selected_signal_1} i {selected_signal_2} do operacji.")
        popup.destroy()
        operate_signals_sel(operation, selected_signal_1, selected_time_1, selected_signal_2, selected_time_2)


    confirm_button = ttk.Button(popup, text="Potwierdź", command=lambda: confirm_selection(operation))
    confirm_button.pack(pady=10)

    popup.transient(root)
    popup.grab_set()
    root.wait_window(popup)


root = tk.Tk()
root.title("Generator sygnałów")
root.geometry("1170x650")
root.resizable(False, False)

ttk.Label(root, text="Typ sygnału:").grid(row=0, column=0, padx=5, pady=5)
signal_type = tk.StringVar(value="Sygnał sinusoidalny")
signal_dropdown = ttk.Combobox(root, textvariable=signal_type, values=["Szum o rozkładzie jednostajnym", "Szum gaussowski", "Sygnał sinusoidalny", "Sygnał prostokątny symetryczny", "Sygnał sinusoidalny wyprostowany jednopołówkowo", "Sygnał sinusoidalny wyprostowany dwupołówkowo ", "Sygnał prostokątny", "Sygnał trójkątny",  "Skok jednostkowy", "Impuls jednostkowy", "Szum impulsowy"],
                               state="readonly",  width=50)
signal_dropdown.grid(row=0, column=1, columnspan=1, padx=5, pady=5)
signal_dropdown.bind("<<ComboboxSelected>>", lambda e: toggle_fields())

sample_rate_var = tk.StringVar(value="1000")

ttk.Label(root, text="Częstotliwość próbkowania").grid(row=1, column=0, padx=5, pady=5)
sample_rate_entry = ttk.Entry(root, textvariable=sample_rate_var)
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

duty_cycle_label = ttk.Label(root, text="Współczynnik wypełnienia (kw):")
duty_cycle_entry = ttk.Entry(root)

duty_cycle_label_ts = ttk.Label(root, text="Czas skoku (ts):")
duty_cycle_entry_ts = ttk.Entry(root)

duty_cycle_label_t = ttk.Label(root, text="Okres (T):")
duty_cycle_entry_t = ttk.Entry(root)

duty_cycle_label_p = ttk.Label(root, text="Prawdopodobieństwo (p):")
duty_cycle_entry_p = ttk.Entry(root)

bins_var = tk.IntVar(value=10)
ttk.Label(root, text="Liczba przedziałów histogramu").grid(row=7, column=0, padx=5, pady=5)
bins_frame = ttk.Frame(root)
bins_frame.grid(row=7, column=1, padx=5, pady=5)

decrease_button = ttk.Button(bins_frame, text="-", command=decrease_bins)
decrease_button.pack(side=tk.LEFT)

bins_label = ttk.Label(bins_frame, textvariable=bins_var)
bins_label.pack(side=tk.LEFT, padx=5)

increase_button = ttk.Button(bins_frame, text="+", command=increase_bins)
increase_button.pack(side=tk.LEFT)

generate_frame = ttk.Frame(root)
generate_frame.grid(row=8, column=0, columnspan=2, pady=10)

ttk.Button(generate_frame, text="Generuj sygnał", command=lambda: generate_signal()).pack(side=tk.LEFT, padx=5)
ttk.Button(generate_frame, text="Generuj histogram", command=lambda: histogram_managment()).pack(side=tk.LEFT, padx=5)

signal_notebook = ttk.Notebook(root)
signal_notebook.grid(row=0, column=2, rowspan=10, columnspan=10, padx=10, pady=10)

signal_frame_1 = ttk.Frame(signal_notebook)
signal_frame_2 = ttk.Frame(signal_notebook)
signal_frame_3 = ttk.Frame(signal_notebook)

signal_notebook.add(signal_frame_1, text="Sygnał 1")
signal_notebook.add(signal_frame_2, text="Sygnał 2")
signal_notebook.add(signal_frame_3, text="Wynik")

notebook = ttk.Notebook(signal_frame_1)
notebook.pack(expand=True, fill='both')
plot_frame_1 = ttk.Frame(notebook)
histogram_frame_1 = ttk.Frame(notebook)
param_frame_1 = ttk.Frame(notebook)
notebook.add(plot_frame_1, text="Wykres")
notebook.add(histogram_frame_1, text="Histogram")
notebook.add(param_frame_1, text="Parametry")


notebook = ttk.Notebook(signal_frame_2)
notebook.pack(expand=True, fill='both')
plot_frame_2 = ttk.Frame(notebook)
histogram_frame_2 = ttk.Frame(notebook)
param_frame_2 = ttk.Frame(notebook)
notebook.add(plot_frame_2, text="Wykres")
notebook.add(histogram_frame_2, text="Histogram")
notebook.add(param_frame_2, text="Parametry")


notebook = ttk.Notebook(signal_frame_3)
notebook.pack(expand=True, fill='both')
plot_frame_3 = ttk.Frame(notebook)
histogram_frame_3 = ttk.Frame(notebook)
param_frame_3 = ttk.Frame(notebook)
notebook.add(plot_frame_3, text="Wykres")
notebook.add(histogram_frame_3, text="Histogram")
notebook.add(param_frame_3, text="Parametry")
plot_empty_chart()

operations_frame = ttk.Frame(root)
operations_frame.grid(row=9, column=0, columnspan=2, pady=10)

ttk.Button(operations_frame, text="Dodaj", command=lambda: select_signals_for_operation("add")).pack(side=tk.LEFT, padx=5)
ttk.Button(operations_frame, text="Odejmij", command=lambda: select_signals_for_operation("subtract")).pack(side=tk.LEFT, padx=5)
ttk.Button(operations_frame, text="Pomnóż", command=lambda: select_signals_for_operation("multiply")).pack(side=tk.LEFT, padx=5)
ttk.Button(operations_frame, text="Podziel", command=lambda: select_signals_for_operation("divide")).pack(side=tk.LEFT, padx=5)

dao_frame = ttk.Frame(root)
dao_frame.grid(row=10, column=0, columnspan=2, pady=10)

ttk.Button(dao_frame, text="Zapisz sygnał", command=lambda: save_signal()).pack(side=tk.LEFT, padx=5)
ttk.Button(dao_frame, text="Wczytaj sygnał", command=lambda: load_signal(0)).pack(side=tk.LEFT, padx=5)
ttk.Button(dao_frame, text="Wyświetl dane", command=lambda: load_signal(1)).pack(side=tk.LEFT, padx=5)

toggle_fields()

root.mainloop()