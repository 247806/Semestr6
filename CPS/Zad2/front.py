import os
import tkinter as tk
from tkinter import ttk

import matplotlib.pyplot as plt
import numpy as np
from jupyter_lsp.specs import md
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from ttkthemes import ThemedTk

import calculateParams as cp
import continousSignal
import discretSignal
import ioModule
import signalOperation as so
from myPlots import plot_signal, plot_histogram, plot_signal_samp, plot_signal_quant
from quantization import clippQuant, roundQuant
from reconstructionSignal import zeroOrderHold, firstOrderHold, sinc_interp, valueFunc
from sampling import sampling
from similarityMeasure import mse, snr, psnr, max_diff

signal_1 = None
time_1 = None
signal_2 = None
time_2 = None
signal_3 = None
time_3 = None
signal_samp_1 = None
time_samp_1 = None
singal_samp_2 = None
time_samp_2 = None
T_1 = None
T_2 = None


def function_type(A, T, t1, d, kw, ts, p, signal):
    sample_rate = float(sample_rate_entry.get())
    time = np.arange(t1, t1 + d, 1 / sample_rate)
    print('dupa')
    print(time)

    if signal_type.get() == "Sygnał sinusoidalny":
        signal = continousSignal.sinusoidal(A, T, time)
        #signal = continousSignal.testFunction(time)
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
    global signal_1, time_1, signal_2, time_2, T_1, T_2

    A = float(amplitude_entry.get())
    if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"]:
        T = float(duty_cycle_entry_t.get())
        if signal_notebook.index(signal_notebook.select()) == 1:
            T_2 = T
        elif signal_notebook.index(signal_notebook.select()) == 0:
            T_1 = T
    else:
        None
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())
    kw = float(duty_cycle_entry.get()) if signal_type.get() in ["Sygnał prostokątny symetryczny", "Sygnał prostokątny", "Sygnał trójkątny"] else None
    ts = float(duty_cycle_entry_ts.get()) if signal_type.get() in ["Skok jednostkowy", "Impuls jednostkowy"] else None
    p = float(duty_cycle_entry_p.get()) if signal_type.get() == "Szum impulsowy" else None


    if signal_notebook.index(signal_notebook.select()) == 1:
        time_2, signal_2 = function_type(A, T, t1, d, kw, ts, p, signal_2)
        plot_signal(time_2, signal_2, signal_type.get(), plot_frame_2, histogram_frame_2)
        create_parameters_tab(param_frame_2, signal_2, time_2)
        plot_histogram(histogram_frame_2, signal_2, int(bins_var.get()))

    else:
        time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1)
        plot_signal(time_1, signal_1, signal_type.get(), plot_frame_1, histogram_frame_1)
        create_parameters_tab(param_frame_1, signal_1, time_1)
        plot_histogram(histogram_frame_1, signal_1, int(bins_var.get()))


    if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"] and d % T != 0:
        full_periods = int(d // T)
        d = full_periods * T
        time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1)




def samplingFun(sample_rate):
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2, T_1, T_2

    if signal_notebook.index(signal_notebook.select()) == 1:
        signal_samp_2, time_samp_2 = sampling(signal_2, time_2, int(sample_rate), T_2)
        plot_signal_samp(time_samp_2, signal_samp_2, sam_frame_2)
    else:
        signal_samp_1, time_samp_1 = sampling(signal_1, time_1, int(sample_rate), T_1)
        plot_signal_samp(time_samp_1, signal_samp_1, sam_frame_1)

    # t = np.linspace(3, 5, 5000)
    # #time_temp, signal_temp = valueFunc(t, signal_samp_1, time_samp_1 )
    # reconstructed_values = np.array([valueFunc(ti, signal_samp_1, time_samp_1) for ti in t])
    #
    # plot_signal_quant(t, reconstructed_values, "test", rec1_frame_1, histogram_frame_3, time_1, signal_1)

def quantizationFun(num_levels):
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2
    if signal_notebook.index(signal_notebook.select()) == 1:
        signal_quant = clippQuant(singal_samp_2, int(num_levels))
        signal_quant_2 = roundQuant(singal_samp_2, int(num_levels))
        plot_signal_quant(time_samp_2, signal_quant, "test", quad1_frame_2, histogram_frame_3, time_2, signal_2)
        plot_signal_quant(time_samp_2, signal_quant_2, "test", quad2_frame_2, histogram_frame_3, time_2, signal_2)
    else:
        signal_quant = clippQuant(signal_samp_1, int(num_levels))
        signal_quant_2 = roundQuant(signal_samp_1, int(num_levels))
        plot_signal_quant(time_samp_1, signal_quant, "test", quad1_frame_1, histogram_frame_3, time_1, signal_1)
        plot_signal_quant(time_samp_1, signal_quant_2, "test", quad2_frame_1, histogram_frame_3, time_1, signal_1)

def reconstructionFun(param):
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2, T_1, T_2

    if signal_notebook.index(signal_notebook.select()) == 1:
        time_temp, signal_temp = zeroOrderHold(signal_samp_2, time_samp_2 )
        plot_signal_quant(time_temp, signal_temp, "test", rec1_frame_2, histogram_frame_3, time_2, signal_2)
        time_temp, signal_temp = firstOrderHold(signal_samp_2, time_samp_2 )
        plot_signal_quant(time_temp, signal_temp, "test", rec2_frame_2, histogram_frame_3, time_2, signal_2)
        t = np.linspace(3, 5, 5000)
        reconstructed_values = np.array([valueFunc(ti, signal_samp_1, time_samp_1, int(param)) for ti in t])
        plot_signal_quant(t, reconstructed_values, "test", rec3_frame_2, histogram_frame_3, time_1, signal_1)
    else:
        time_temp, signal_temp = zeroOrderHold(signal_samp_1, time_samp_1 )
        plot_signal_quant(time_temp, signal_temp, "test", rec1_frame_1, histogram_frame_3, time_1, signal_1)
        time_temp_1, signal_temp_2 = firstOrderHold(signal_samp_1, time_samp_1 )
        plot_signal_quant(time_temp_1, signal_temp_2, "test", rec2_frame_1, histogram_frame_3, time_1, signal_1)
        len = np.round(abs(time_samp_1[-1] - time_samp_1[0]))*1000
        print(len)
        t = np.linspace(time_samp_1[0], time_samp_1[-1], int(len))
        reconstructed_values = np.array([valueFunc(ti, signal_samp_1, time_samp_1, int(param)) for ti in t])
        plot_signal_quant(t, reconstructed_values, "test", rec3_frame_1, histogram_frame_3, time_1, signal_1)
    temp = mse(signal_1, signal_temp_2)
    temp2 = snr(signal_1, signal_temp_2)
    temp3 = psnr(signal_1, signal_temp_2)
    temp4 = max_diff(signal_1, signal_temp_2)
    print(f"MSE: {temp}")
    print(f"SNR: {temp2}")
    print(f"PSNR: {temp3}")
    print(f"MD: {temp4}")


def histogram_managment():
    if signal_notebook.index(signal_notebook.select()) == 0:
        plot_histogram(histogram_frame_1, signal_1, int(bins_var.get()))
    elif signal_notebook.index(signal_notebook.select()) == 1:
        plot_histogram(histogram_frame_2, signal_2, int(bins_var.get()))
    else:
        plot_histogram(histogram_frame_3, signal_3, int(bins_var.get()))

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
    for widget in param.winfo_children():
        widget.destroy()

    if signal_type.get() not in ["Impuls jednostkowy", "Szum impulsowy"]:
        avg_label = ttk.Label(param, text=f"Średnia: {cp.avg_cont(signal, time):.3f}")
        avg_label.pack(padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_cont(signal, time):.3f}")
        abs_avg_label.pack(padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_cont(signal, time):.3f}")
        power_label.pack(padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_cont(signal, time):.3f}")
        dev_label.pack(padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_cont(signal, time):.3f}")
        eff_power_label.pack(padx=5, pady=5)

    else:
        avg_label = ttk.Label(param, text=f"Średnia: {cp.avg_dis(signal, time):.3f}")
        avg_label.pack(padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_dis(signal, time):.3f}")
        abs_avg_label.pack(padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_dis(signal, time):.3f}")
        power_label.pack(padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_dis(signal, time):.3f}")
        dev_label.pack(padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_dis(signal, time):.3f}")
        eff_power_label.pack(padx=5, pady=5)

def plot_empty_chart():
    plt.style.use('classic')
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
    canvas = FigureCanvasTkAgg(fig, master=sam_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=sam_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=quad1_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=quad1_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=quad2_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=quad2_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()

    fig, ax = plt.subplots()
    ax.set_xlabel("Amplituda")
    ax.set_title("Histogram")
    ax.set_ylabel("Częstotliwość")
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

    sampling_rate_1 = round(1 / (time_1[1] - time_1[0]), 2) if len(time_1) > 1 else 1.0
    sampling_rate_2 = round(1 / (time_2[1] - time_2[0]), 2) if len(time_2) > 1 else 1.0

    if sampling_rate_1 != sampling_rate_2:
        print("Różne próbkowanie")
    else:
        signal_3, time_3 = so.operate_signals(operation, signal_1_sel, time_1_sel, signal_2_sel, time_2_sel)
        plot_signal(time_3, signal_3, "Wynik", plot_frame_3, histogram_frame_3)
        plot_histogram(histogram_frame_3, signal_3, int(bins_var.get()))
        create_parameters_tab(param_frame_3, signal_3, time_3)

def operate_signals(operation):
    global signal_1, time_1, signal_2, time_2, signal_3, time_3

    sampling_rate_1 = round(1 / (time_1[1] - time_1[0]), 2) if len(time_1) > 1 else 1.0
    sampling_rate_2 = round(1 / (time_2[1] - time_2[0]), 2) if len(time_2) > 1 else 1.0

    if sampling_rate_1 != sampling_rate_2:
        print("Różne próbkowanie")
    else:
        signal_3, time_3 = so.operate_signals(operation, signal_1, time_1, signal_2, time_2)
        plot_signal(time_3, signal_3, "Wynik", plot_frame_3, histogram_frame_3)
        plot_histogram(histogram_frame_3, signal_3, int(bins_var.get()))
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

    ioModule.save_signal(signal, time)

def load_signal(type):
    global time_1, signal_1, signal_2, time_2, signal_3, time_3


    time_temp, signal_temp, start_time, sampling_rate, max_amplitude, num_samples = ioModule.load_signal()

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
        plot_histogram(histogram, signal, int(bins_var.get()))
    else:
        display_data_in_popup(time, signal, start_time, sampling_rate, num_samples, max_amplitude)

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

root = ThemedTk()
#root = tk.Tk()
# Uzyskanie katalogu, w którym znajduje się skrypt
base_dir = os.path.dirname(__file__)

# Utworzenie ścieżki względnej do pliku .tcl
tcl_path = os.path.join(base_dir, "breeze-dark", "breeze-dark.tcl")
print(tcl_path)
root.tk.call("source", tcl_path)

# Ustawienie motywu
root.set_theme("arc")  # Możesz zmienić "arc" na inny motyw
print(root.get_themes())
root.title("Generator sygnałów")
root.geometry("1200x600")
root.resizable(False, False)


# --- GŁÓWNY NOTEBOOK ---
main_notebook = ttk.Notebook(root)
main_notebook.grid(row=0, column=0, padx=10, pady=10)

# --- ZAKŁADKI ---
tab_generate = ttk.Frame(main_notebook)
tab_operations = ttk.Frame(main_notebook)
tab_save = ttk.Frame(main_notebook)
tab_sampAndQuant = ttk.Frame(main_notebook)
tab_reconstruction = ttk.Frame(main_notebook)

main_notebook.add(tab_generate, text="Generowanie")
main_notebook.add(tab_operations, text="Operacje matematyczne")
main_notebook.add(tab_save, text="Zapis i odczyt")
main_notebook.add(tab_sampAndQuant, text="Próbkowanie i kwantyzacja")
main_notebook.add(tab_reconstruction, text="Rekonstrukcja")

# --- GENEROWANIE ---
ttk.Label(tab_generate, text="Typ sygnału:").grid(row=0, column=0, padx=5, pady=5)
signal_type = tk.StringVar(value="Sygnał sinusoidalny")
signal_dropdown = ttk.Combobox(tab_generate, textvariable=signal_type,
                               values=["Szum o rozkładzie jednostajnym", "Szum gaussowski", "Sygnał sinusoidalny",
                                       "Sygnał prostokątny symetryczny",
                                       "Sygnał sinusoidalny wyprostowany jednopołówkowo",
                                       "Sygnał sinusoidalny wyprostowany dwupołówkowo ",
                                       "Sygnał prostokątny", "Sygnał trójkątny", "Skok jednostkowy",
                                       "Impuls jednostkowy", "Szum impulsowy"],
                               state="readonly", width=50)
signal_dropdown.grid(row=0, column=1, padx=5, pady=5)
signal_dropdown.bind("<<ComboboxSelected>>", lambda e: toggle_fields())

ttk.Label(tab_generate, text="Częstotliwość próbkowania").grid(row=1, column=0, padx=5, pady=5)
sample_rate_var = tk.StringVar(value="1000")
sample_rate_entry = ttk.Entry(tab_generate, textvariable=sample_rate_var)
sample_rate_entry.grid(row=1, column=1, padx=5, pady=5)

ttk.Label(tab_generate, text="Amplituda (A):").grid(row=2, column=0, padx=5, pady=5)
amplitude_var = tk.StringVar(value="1")
amplitude_entry = ttk.Entry(tab_generate, textvariable=amplitude_var)
amplitude_entry.grid(row=2, column=1, padx=5, pady=5)

ttk.Label(tab_generate, text="Czas początkowy (t1):").grid(row=4, column=0, padx=5, pady=5)
start_time_var = tk.StringVar(value="0")
start_time_entry = ttk.Entry(tab_generate, textvariable=start_time_var)
start_time_entry.grid(row=4, column=1, padx=5, pady=5)

ttk.Label(tab_generate, text="Czas trwania (d):").grid(row=5, column=0, padx=5, pady=5)
duration_var = tk.StringVar(value="5")
duration_entry = ttk.Entry(tab_generate, textvariable=duration_var)
duration_entry.grid(row=5, column=1, padx=5, pady=5)

duty_cycle_label = ttk.Label(tab_generate, text="Współczynnik wypełnienia (kw):")
duty_cycle_entry = ttk.Entry(tab_generate)

duty_cycle_label_ts = ttk.Label(tab_generate, text="Czas skoku (ts):")
duty_cycle_entry_ts = ttk.Entry(tab_generate)

duty_cycle_label_t = ttk.Label(tab_generate, text="Okres (T):")
duty_cycle_var = tk.StringVar(value="1")
duty_cycle_entry_t = ttk.Entry(tab_generate, textvariable=duty_cycle_var)

duty_cycle_label_p = ttk.Label(tab_generate, text="Prawdopodobieństwo (p):")
duty_cycle_entry_p = ttk.Entry(tab_generate)

bins_var = tk.IntVar(value=10)
ttk.Label(tab_generate, text="Liczba przedziałów histogramu").grid(row=7, column=0, padx=5, pady=5)
bins_frame = ttk.Frame(tab_generate)
bins_frame.grid(row=7, column=1, padx=5, pady=5)

decrease_button = ttk.Button(bins_frame, text="-", command=decrease_bins)
decrease_button.pack(side=tk.LEFT)

bins_label = ttk.Label(bins_frame, textvariable=bins_var)
bins_label.pack(side=tk.LEFT, padx=5)

increase_button = ttk.Button(bins_frame, text="+", command=increase_bins)
increase_button.pack(side=tk.LEFT)

ttk.Button(tab_generate, text="Generuj sygnał", command=lambda: generate_signal()).grid(row=8, column=0, padx=5, pady=5)
ttk.Button(tab_generate, text="Generuj histogram", command=lambda: histogram_managment()).grid(row=8, column=1, padx=5, pady=5)


# --- OPERACJE ---
ttk.Button(tab_operations, text="Dodaj", command=lambda: select_signals_for_operation("add")).grid(row=1, column=0, padx=100, pady=50)
ttk.Button(tab_operations, text="Odejmij", command=lambda: select_signals_for_operation("subtract")).grid(row=1, column=1, padx=10, pady=5)
ttk.Button(tab_operations, text="Pomnóż", command=lambda: select_signals_for_operation("multiply")).grid(row=2, column=0, padx=100, pady=5)
ttk.Button(tab_operations, text="Podziel", command=lambda: select_signals_for_operation("divide")).grid(row=2, column=1, padx=10, pady=5)

# --- ZAPIS I ODCZYT ---
ttk.Button(tab_save, text="Zapisz sygnał", command=lambda: save_signal()).grid(row=1, column=0, padx=100, pady=50)
ttk.Button(tab_save, text="Wczytaj sygnał", command=lambda: load_signal(0)).grid(row=1, column=1, padx=5, pady=5)
ttk.Button(tab_save, text="Wyświetl dane", command=lambda: load_signal(1)).grid(row=2, column=0, padx=5, pady=5)

# --- PRÓBKOWANIE I KWANTYZACJA ---
ttk.Label(tab_sampAndQuant, text="Częstotliwość próbkowania").grid(row=1, column=0, padx=50, pady=50)
sample_rate_fun_var = tk.StringVar(value="10")
sample_rate_fun_entry = ttk.Entry(tab_sampAndQuant, textvariable=sample_rate_fun_var)
sample_rate_fun_entry.grid(row=1, column=1, padx=5, pady=5)
ttk.Button(tab_sampAndQuant, text="Próbkuj", command=lambda: samplingFun(sample_rate_fun_entry.get())).grid(row=3, column=0, padx=10, pady=10)

ttk.Label(tab_sampAndQuant, text="Liczba poziomów kwantyzacji").grid(row=2, column=0, padx=50, pady=50)
num_levels_var = tk.StringVar(value="10")
num_levels_entry = ttk.Entry(tab_sampAndQuant, textvariable=num_levels_var)
num_levels_entry.grid(row=2, column=1, padx=5, pady=5)
ttk.Button(tab_sampAndQuant, text="Kwantyzuj", command=lambda: quantizationFun(num_levels_entry.get())).grid(row=3, column=1, padx=10, pady=10)

# --- RECONSTRUKCJA ---
ttk.Label(tab_reconstruction, text="Parametr funkcji sinc").grid(row=1, column=0, padx=50, pady=50)
param_var = tk.StringVar(value="10")
param_entry = ttk.Entry(tab_reconstruction, textvariable=param_var)
param_entry.grid(row=1, column=1, padx=5, pady=5)
ttk.Button(tab_reconstruction, text="Zrekonstruuj", command=lambda: reconstructionFun(param_var.get())).grid(row=2, column=0, padx=10, pady=10)


# --- WYKRESY ---
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
sam_frame_1 = ttk.Frame(notebook)
quad_frame = ttk.Notebook(notebook)
rec_frame = ttk.Notebook(notebook)
notebook.add(plot_frame_1, text="Wykres")
notebook.add(histogram_frame_1, text="Histogram")
notebook.add(param_frame_1, text="Parametry")
notebook.add(sam_frame_1, text="Próbkowanie")
notebook.add(quad_frame, text="Kwantyzacja")
notebook.add(rec_frame, text="Rekonstrukcja")

quad1_frame_1 = ttk.Frame(quad_frame)
quad2_frame_1 = ttk.Frame(quad_frame)
quad_frame.add(quad1_frame_1, text="Kwantyzacja z obcięciem")
quad_frame.add(quad2_frame_1, text="Kwantyzacja z zaokrągleniem")

rec1_frame_1 = ttk.Frame(rec_frame)
rec2_frame_1 = ttk.Frame(rec_frame)
rec3_frame_1 = ttk.Frame(rec_frame)
rec_frame.add(rec1_frame_1, text="Zero-order hold")
rec_frame.add(rec2_frame_1, text="First-order hold")
rec_frame.add(rec3_frame_1, text="Sinc Interpolation")

notebook = ttk.Notebook(signal_frame_2)
notebook.pack(expand=True, fill='both')
plot_frame_2 = ttk.Frame(notebook)
histogram_frame_2 = ttk.Frame(notebook)
param_frame_2 = ttk.Frame(notebook)
sam_frame_2 = ttk.Frame(notebook)
quad_frame = ttk.Notebook(notebook)
rec_frame = ttk.Notebook(notebook)
notebook.add(plot_frame_2, text="Wykres")
notebook.add(histogram_frame_2, text="Histogram")
notebook.add(param_frame_2, text="Parametry")
notebook.add(sam_frame_2, text="Próbkowanie")
notebook.add(quad_frame, text="Kwantyzacja")
notebook.add(rec_frame, text="Rekonstrukcja")

quad1_frame_2 = ttk.Frame(quad_frame)
quad2_frame_2 = ttk.Frame(quad_frame)
quad_frame.add(quad1_frame_2, text="Kwantyzacja z obcięciem")
quad_frame.add(quad2_frame_2, text="Kwantyzacja z zaokrągleniem")

rec1_frame_2 = ttk.Frame(rec_frame)
rec2_frame_2 = ttk.Frame(rec_frame)
rec3_frame_2 = ttk.Frame(rec_frame)
rec_frame.add(rec1_frame_2, text="Zero-order hold")
rec_frame.add(rec2_frame_2, text="First-order hold")
rec_frame.add(rec3_frame_2, text="Sinc Interpolation")


notebook = ttk.Notebook(signal_frame_3)
notebook.pack(expand=True, fill='both')
plot_frame_3 = ttk.Frame(notebook)
histogram_frame_3 = ttk.Frame(notebook)
param_frame_3 = ttk.Frame(notebook)
notebook.add(plot_frame_3, text="Wykres")
notebook.add(histogram_frame_3, text="Histogram")
notebook.add(param_frame_3, text="Parametry")
plot_empty_chart()

toggle_fields()

root.mainloop()