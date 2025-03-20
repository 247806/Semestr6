import tkinter as tk
from tkinter import ttk
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
        signal = continousSignal.sinusoidal(A, T, t1, d, time)
    elif signal_type.get() == "Sygnał prostokątny symetryczny":
        signal = continousSignal.squareSymetric(A, T, t1, d, kw, time)
    elif signal_type.get() == 'Sygnał sinusoidalny wyprostowany jednopołówkowo':
        signal = continousSignal.halfWaveSinusoidal(A, T, t1, d, time)
    elif signal_type.get() == 'Sygnał sinusoidalny wyprostowany dwupołówkowo ':
        signal = continousSignal.halfSinusoidal(A, T, t1, d, time)
    elif signal_type.get() == 'Sygnał prostokątny':
        signal = continousSignal.square(A, T, t1, d, kw, time)
    elif signal_type.get() == 'Sygnał trójkątny':
        signal = continousSignal.triangle(A, T, t1, d, kw, time)
    elif signal_type.get() == 'Skok jednostkowy':
        signal = continousSignal.ones(A, t1, d, ts, time)
    elif signal_type.get() == 'Szum o rozkładzie jednostajnym':
        signal = continousSignal.random_uniform_signal(A, t1, d, time)
    elif signal_type.get() == 'Szum gaussowski':
        signal = continousSignal.gaussian_noise(A, t1, d, time)
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

def plot_signal(time, signal, signal_type, plot, histogram):

    for widget in plot.winfo_children():
        widget.destroy()

    for widget in histogram.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    if signal_type in ["Impuls jednostkowy", "Szum impulsowy"]:
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
        avg_label.grid(row=0, column=0, padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_cont(signal, time):.2f}")
        abs_avg_label.grid(row=1, column=0, padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_cont(signal, time):.2f}")
        power_label.grid(row=2, column=0, padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_cont(signal, time):.2f}")
        dev_label.grid(row=3, column=0, padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_cont(signal, time):.2f}")
        eff_power_label.grid(row=4, column=0, padx=5, pady=5)

    else:
        avg_label = ttk.Label(param, text=f"Średnia: {cp.avg_dis(signal, time):.2f}")
        avg_label.grid(row=0, column=0, padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_dis(signal, time):.2f}")
        abs_avg_label.grid(row=1, column=0, padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_dis(signal, time):.2f}")
        power_label.grid(row=2, column=0, padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_dis(signal, time):.2f}")
        dev_label.grid(row=3, column=0, padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_dis(signal, time):.2f}")
        eff_power_label.grid(row=4, column=0, padx=5, pady=5)


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

def operate_signals(operation):
    global signal_1, time_1, signal_2, time_2, signal_3, time_3
    signal_3, time_3 = so.operate_signals(operation, signal_1, time_1, signal_2, time_2)
    plot_signal(time_3, signal_3, "Wynik", plot_frame_3, histogram_frame_3)
    plot_histogram(histogram_frame_3, signal_3)
    create_parameters_tab(param_frame_3, signal_3, time_3)


root = tk.Tk()
root.title("Generator sygnałów")
root.geometry("1170x550")
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


generate_button = ttk.Button(root, text="Generuj sygnał", command=generate_signal)
generate_button.grid(row=8, column=0, padx=5, pady=5)

generate_button = ttk.Button(root, text="Generuj histogram", command=plot_histogram)
generate_button.grid(row=8, column=1, padx=5, pady=5)

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

ttk.Button(operations_frame, text="Dodaj", command=lambda: operate_signals("add")).pack(side=tk.LEFT, padx=5)
ttk.Button(operations_frame, text="Odejmij", command=lambda: operate_signals("subtract")).pack(side=tk.LEFT, padx=5)
ttk.Button(operations_frame, text="Pomnóż", command=lambda: operate_signals("multiply")).pack(side=tk.LEFT, padx=5)
ttk.Button(operations_frame, text="Podziel", command=lambda: operate_signals("divide")).pack(side=tk.LEFT, padx=5)

toggle_fields()

root.mainloop()