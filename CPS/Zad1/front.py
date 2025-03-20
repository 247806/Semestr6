import tkinter as tk
from tkinter import ttk
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import continousSignal
import discretSignal
import calculateParams as cp
signal_1 = None
time_1 = None
signal_type_2 = None
signal_2 = None
time_2 = None
signal_3 = None
time_3 = None

def function_type(A, T, t1, d, kw, ts, p):
    global signal_1, time_1

    sample_rate = float(sample_rate_entry.get())
    time_1 = np.arange(t1, d, 1 / sample_rate)

    if signal_type.get() == "sinusoidal":
        signal_1 = continousSignal.sinusoidal(A, T, t1, d, time_1)
    elif signal_type.get() == "squareSymmetric":
        signal_1 = continousSignal.squareSymetric(A, T, t1, d, kw, time_1)
    elif signal_type.get() == 'halfWaveSinusoidal':
        signal_1 = continousSignal.halfWaveSinusoidal(A, T, t1, d, time_1)
    elif signal_type.get() == 'halfSinusoidal':
        signal_1 = continousSignal.halfSinusoidal(A, T, t1, d, time_1)
    elif signal_type.get() == 'square':
        signal_1 = continousSignal.square(A, T, t1, d, kw, time_1)
    elif signal_type.get() == 'triangle':
        signal_1 = continousSignal.triangle(A, T, t1, d, kw, time_1)
    elif signal_type.get() == 'ones':
        signal_1 = continousSignal.ones(A, t1, d, ts, time_1)
    elif signal_type.get() == 'random_uniform_signal':
        signal_1 = continousSignal.random_uniform_signal(A, t1, d, time_1)
    elif signal_type.get() == 'gaussian_noise':
        signal_1 = continousSignal.gaussian_noise(A, t1, d, time_1)
    elif signal_type.get() == 'delta_diraca':
        time_1, signal_1 = discretSignal.delta_diraca(A, t1, ts, d, sample_rate)
    elif signal_type.get() == 'impuls_noise':
        time_1, signal_1 = discretSignal.impuls_noise(A, t1, d, sample_rate, p)


# Funkcja do generowania wykresu w aplikacji
def generate_signal():
    global signal_1, time_1, signal_2, time_2, signal_type_2
    A = float(amplitude_entry.get())
    T = float(duty_cycle_entry_t.get()) if signal_type.get() not in ["ones", "random_uniform_signal", "gaussian_noise", "delta_diraca", "impuls_noise"] else None
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())

    # Pobranie wartości tylko jeśli kw jest widoczne
    kw = float(duty_cycle_entry.get()) if signal_type.get() in ["squareSymmetric", "square", "triangle"] else None
    ts = float(duty_cycle_entry_ts.get()) if signal_type.get() in ["ones", "delta_diraca"] else None
    p = float(duty_cycle_entry_p.get()) if signal_type.get() == "impuls_noise" else None

    if signal_1 is not None and time_1 is not None:
        signal_2 = signal_1
        time_2 = time_1
        update_plot(time_2, signal_2, signal_type_2)
    signal_type_2 = signal_type.get()

    function_type(A, T, t1, d, kw, ts, p)

    plot_signal(time_1, signal_1, signal_type.get())


    # if signal_type.get() not in ["ones", "random_uniform_signal", "gaussian_noise", "delta_diraca", "impuls_noise"] and d % T != 0:
    #     print("okres")
    #     full_periods = int(d // T)
    #     d = full_periods * T
    #     function_type(A, T, t1, d, kw, ts, p)


    plot_histogram()
    create_parameters_tab()

# Funkcja rysująca wykres w aplikacji
def plot_signal(time, signal, signal_type):

    for widget in plot_frame_1.winfo_children():
        widget.destroy()

    for widget in histogram_frame_1.winfo_children():
        widget.destroy()

    if signal_type in ["delta_diraca", "impuls_noise"]:
        fig, ax = plt.subplots()
        ax.scatter(time, signal, label=f"{signal_type.capitalize()} Signal")
        ax.set_xlabel("Time [s]")
        ax.set_ylabel("Amplitude")
        ax.set_title(f"{signal_type.capitalize()} Signal")
        ax.grid()
        ax.legend()
    else:
        fig, ax = plt.subplots()
        ax.plot(time, signal, label=f"{signal_type.capitalize()} Signal")
        ax.set_xlabel("Time [s]")
        ax.set_ylabel("Amplitude")
        ax.set_title(f"{signal_type.capitalize()} Signal")
        ax.grid()
        ax.legend()
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_1)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=True, fill='both', padx=5, pady=5)


def plot_histogram():
    for widget in histogram_frame_1.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    ax.hist(signal_1, bins=int(bins_var.get()), alpha=0.7, color='blue', edgecolor='black')
    ax.set_xlabel("Amplitude")
    ax.set_ylabel("Frequency")
    ax.set_title(f"Histogram - {signal_type.get().capitalize()} Signal")

    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1)
    canvas.draw()
    canvas.get_tk_widget().pack()


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

def create_parameters_tab():
    global signal_1, time_1

    if time_1 is not None and signal_1 is not None and signal_type.get() not in ["delta_diraca", "impuls_noise"]:
        avg_label = ttk.Label(param_frame_1, text=f"Średnia: {cp.avg_cont(signal_1, time_1):.2f}")
        avg_label.grid(row=0, column=0, padx=5, pady=5)

        abs_avg_label = ttk.Label(param_frame_1, text=f"Średnia bezwzględna: {cp.abs_avg_cont(signal_1, time_1):.2f}")
        abs_avg_label.grid(row=1, column=0, padx=5, pady=5)

        power_label = ttk.Label(param_frame_1, text=f"Moc: {cp.power_cont(signal_1, time_1):.2f}")
        power_label.grid(row=2, column=0, padx=5, pady=5)

        dev_label = ttk.Label(param_frame_1, text=f"Wariancja: {cp.dev_cont(signal_1, time_1):.2f}")
        dev_label.grid(row=3, column=0, padx=5, pady=5)

        eff_power_label = ttk.Label(param_frame_1, text=f"Wartość skuteczna: {cp.eff_power_cont(signal_1, time_1):.2f}")
        eff_power_label.grid(row=4, column=0, padx=5, pady=5)
    elif time_1 is not None and signal_1 is not None :
        avg_label = ttk.Label(param_frame_1, text=f"Średnia: {cp.avg_dis(signal_1, time_1):.2f}")
        avg_label.grid(row=0, column=0, padx=5, pady=5)

        abs_avg_label = ttk.Label(param_frame_1, text=f"Średnia bezwzględna: {cp.abs_avg_dis(signal_1, time_1):.2f}")
        abs_avg_label.grid(row=1, column=0, padx=5, pady=5)

        power_label = ttk.Label(param_frame_1, text=f"Moc: {cp.power_dis(signal_1, time_1):.2f}")
        power_label.grid(row=2, column=0, padx=5, pady=5)

        dev_label = ttk.Label(param_frame_1, text=f"Wariancja: {cp.dev_dis(signal_1, time_1):.2f}")
        dev_label.grid(row=3, column=0, padx=5, pady=5)

        eff_power_label = ttk.Label(param_frame_1, text=f"Wartość skuteczna: {cp.eff_power_dis(signal_1, time_1):.2f}")
        eff_power_label.grid(row=4, column=0, padx=5, pady=5)

    return param_frame_1

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

def update_plot(time, signal, signal_type):

    for widget in plot_frame_2.winfo_children():
        widget.destroy()

    for widget in histogram_frame_2.winfo_children():
        widget.destroy()

    if signal_type in ["delta_diraca", "impuls_noise"]:
        fig, ax = plt.subplots()
        ax.scatter(time, signal, label=f"{signal_type.capitalize()} Signal")
        ax.set_xlabel("Time [s]")
        ax.set_ylabel("Amplitude")
        ax.set_title(f"{signal_type.capitalize()} Signal")
        ax.grid()
        ax.legend()
    else:
        fig, ax = plt.subplots()
        ax.plot(time, signal, label=f"{signal_type.capitalize()} Signal")
        ax.set_xlabel("Time [s]")
        ax.set_ylabel("Amplitude")
        ax.set_title(f"{signal_type.capitalize()} Signal")
        ax.grid()
        ax.legend()
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_2)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=True, fill='both', padx=5, pady=5)

    for widget in histogram_frame_2.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    ax.hist(signal_1, bins=int(bins_var.get()), alpha=0.7, color='blue', edgecolor='black')
    ax.set_xlabel("Amplitude")
    ax.set_ylabel("Frequency")
    ax.set_title(f"Histogram - {signal_type.capitalize()} Signal")

    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2)
    canvas.draw()
    canvas.get_tk_widget().pack()


root = tk.Tk()
root.title("Generator sygnałów")
root.geometry("1050x550")  # Stałe wymiary okna
root.resizable(False, False)  # Blokada zmiany rozmiaru

ttk.Label(root, text="Typ sygnału:").grid(row=0, column=0, padx=5, pady=5)
signal_type = tk.StringVar(value="sinusoidal")
signal_dropdown = ttk.Combobox(root, textvariable=signal_type, values=["random_uniform_signal", "gaussian_noise", "sinusoidal", "squareSymmetric", "halfWaveSinusoidal", "halfSinusoidal", "square", "triangle",  "ones", "delta_diraca", "impuls_noise"],
                               state="readonly")
signal_dropdown.grid(row=0, column=1, padx=5, pady=5)
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

signal_1 = ttk.Frame(signal_notebook)
signal_2 = ttk.Frame(signal_notebook)
signal_3 = ttk.Frame(signal_notebook)

signal_notebook.add(signal_1, text="Sygnał 1")
signal_notebook.add(signal_2, text="Sygnał 2")
signal_notebook.add(signal_3, text="Wynik")

notebook = ttk.Notebook(signal_1)
notebook.pack(expand=True, fill='both')
plot_frame_1 = ttk.Frame(notebook)
histogram_frame_1 = ttk.Frame(notebook)
param_frame_1 = ttk.Frame(notebook)
notebook.add(plot_frame_1, text="Wykres")
notebook.add(histogram_frame_1, text="Histogram")
notebook.add(param_frame_1, text="Parametry")


notebook = ttk.Notebook(signal_2)
notebook.pack(expand=True, fill='both')
plot_frame_2 = ttk.Frame(notebook)
histogram_frame_2 = ttk.Frame(notebook)
param_frame_2 = ttk.Frame(notebook)
notebook.add(plot_frame_2, text="Wykres")
notebook.add(histogram_frame_2, text="Histogram")
notebook.add(param_frame_2, text="Parametry")


notebook = ttk.Notebook(signal_3)
notebook.pack(expand=True, fill='both')
plot_frame_3 = ttk.Frame(notebook)
histogram_frame_3 = ttk.Frame(notebook)
param_frame_3 = ttk.Frame(notebook)
notebook.add(plot_frame_3, text="Wykres")
notebook.add(histogram_frame_3, text="Histogram")
notebook.add(param_frame_3, text="Parametry")
plot_empty_chart()

toggle_fields()

#plot_empty_chart()

root.mainloop()