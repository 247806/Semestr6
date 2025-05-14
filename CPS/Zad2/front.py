import os
import tkinter as tk
from tkinter import ttk

import matplotlib.pyplot as plt
import numpy as np
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from ttkthemes import ThemedTk

import calculateParams as cp
import continousSignal
import discretSignal
import ioModule
import signalOperation as so
from convolve import convolve, convolve_time_axis
from correlation import cross_convolution, cross_correlation_direct
from filters import low_pass_filter, band_pass_filter, high_pass_filter
from functionType import function_type
from myPlots import plot_signal, plot_histogram, plot_signal_samp, plot_signal_quant
from quantization import clippQuant, roundQuant
from reconstructionSignal import zeroOrderHold, firstOrderHold, valueFunc
from sampling import sampling
from similarityMeasure import mse, snr, psnr, max_diff, enob

signal_1 = None
time_1 = None
signal_2 = None
time_2 = None
signal_3 = None
time_3 = None
signal_samp_1 = None
time_samp_1 = None
signal_samp_2 = None
time_samp_2 = None
T_1 = None
T_2 = None
kw_1 = None
kw_2 = None
p_1 = None
p_2 = None
ts_1 = None
ts_2 = None
signal_ko_1 = None
signal_ko_2 = None
signal_kz_1 = None
signal_kz_2 = None
signal_rz_1 = None
signal_rz_2 = None
signal_rp_1 = None
signal_rp_2 = None
signal_rs_1 = None
signal_rs_2 = None
signal_type_1 = None
signal_type_2 = None


def generate_signal():
    global signal_1, time_1, signal_2, time_2, T_1, T_2, signal_type_1, signal_type_2, kw_1, kw_2, p_1, p_2, ts_1, ts_2

    A = float(amplitude_entry.get())
    if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"]:
        T = float(duty_cycle_entry_t.get())
        if signal_notebook.index(signal_notebook.select()) == 1:
            T_2 = T
        elif signal_notebook.index(signal_notebook.select()) == 0:
            T_1 = T
    else:
        T = 0
    t1 = float(start_time_entry.get())
    d = float(duration_entry.get())

    if signal_type.get() in ["Sygnał prostokątny symetryczny", "Sygnał prostokątny", "Sygnał trójkątny"]:
        kw = float(duty_cycle_entry.get())
        if signal_notebook.index(signal_notebook.select()) == 1:
            kw_2 = kw
        elif signal_notebook.index(signal_notebook.select()) == 0:
            kw_1 = kw
    else:
        kw = None

    if signal_type.get() in ["Skok jednostkowy", "Impuls jednostkowy"]:
        ts = float(duty_cycle_entry_ts.get())
        if signal_notebook.index(signal_notebook.select()) == 1:
            ts_2 = ts
        elif signal_notebook.index(signal_notebook.select()) == 0:
            ts_1 = ts
    else:
        ts = None

    if signal_type.get() == "Szum impulsowy":
        p = float(duty_cycle_entry_p.get())
        if signal_notebook.index(signal_notebook.select()) == 1:
            p_2 = p
        elif signal_notebook.index(signal_notebook.select()) == 0:
            p_1 = p
    else:
        p = None

    sample_rate = float(sample_rate_entry.get())
    time = np.arange(t1, t1 + d, 1 / sample_rate)

    if signal_notebook.index(signal_notebook.select()) == 1:
        time_2, signal_2 = function_type(A, T, t1, d, kw, ts, p, signal_2, signal_type.get(), time, sample_rate_entry.get())
        if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski",
                                     "Impuls jednostkowy", "Szum impulsowy"] and d % T != 0:
            full_periods = int(d // T)
            d = full_periods * T
            time_2, signal_2 = function_type(A, T, t1, d, kw, ts, p, signal_2, signal_type.get(), time,
                                             sample_rate_entry.get())

        plot_signal(time_2, signal_2, signal_type.get(), plot_frame_2, histogram_frame_2)
        create_parameters_tab(param_frame_2, signal_2, time_2, signal_type.get())
        plot_histogram(histogram_frame_2, signal_2, int(bins_var.get()))
        signal_type_2 = signal_type.get()

    else:
        time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1, signal_type.get(), time, sample_rate_entry.get())
        if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski",
                                     "Impuls jednostkowy", "Szum impulsowy"] and d % T != 0:
            full_periods = int(d // T)
            d = full_periods * T
            time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1, signal_type.get(), time,
                                             sample_rate_entry.get())

        plot_signal(time_1, signal_1, signal_type.get(), plot_frame_1, histogram_frame_1)
        create_parameters_tab(param_frame_1, signal_1, time_1, signal_type.get())
        plot_histogram(histogram_frame_1, signal_1, int(bins_var.get()))
        signal_type_1 = signal_type.get()



    # if signal_type.get() not in ["Skok jednostkowy", "Szum o rozkładzie jednostajnym", "Szum gaussowski", "Impuls jednostkowy", "Szum impulsowy"] and d % T != 0:
    #     full_periods = int(d // T)
    #     d = full_periods * T
    #     time_1, signal_1 = function_type(A, T, t1, d, kw, ts, p, signal_1, signal_type.get(), time, sample_rate_entry.get())

def samplingFun(sample_rate):
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2, T_1, T_2, p_2, ts_2, kw_2, p_1, ts_1, kw_1

    if signal_notebook.index(signal_notebook.select()) == 1:
        signal_samp_2, time_samp_2 = sampling(signal_2, time_2, int(sample_rate), T_2, signal_type_2, p_2, ts_2, kw_2)
        plot_signal_samp(time_samp_2, signal_samp_2, plot_frame_samp_2)
        plot_histogram(histogram_frame_samp_2, signal_samp_2, int(bins_var.get()))
        create_parameters_tab(param_frame_samp_2, signal_samp_2, time_1, "Szum impulsowy")
    else:
        signal_samp_1, time_samp_1 = sampling(signal_1, time_1, int(sample_rate), T_1, signal_type_1, p_1, ts_1, kw_1)
        plot_signal_samp(time_samp_1, signal_samp_1, plot_frame_samp_1)
        plot_histogram(histogram_frame_samp_1, signal_samp_1, int(bins_var.get()))
        create_parameters_tab(param_frame_samp_1, signal_samp_1, time_1, "Szum impulsowy")
        

def quantizationFun(num_levels):
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2, signal_ko_1, signal_ko_2, signal_kz_1, signal_kz_2
    if signal_notebook.index(signal_notebook.select()) == 1:
        signal_ko_2 = clippQuant(signal_samp_2, int(num_levels))
        signal_kz_2 = roundQuant(signal_samp_2, int(num_levels))
        plot_signal_quant(time_samp_2, signal_ko_2, "test", quad1_frame_2, histogram_frame_3, time_2, signal_2, signal_samp_2, time_samp_2)
        plot_signal_quant(time_samp_2, signal_kz_2, "test", quad2_frame_2, histogram_frame_3, time_2, signal_2, signal_samp_2, time_samp_2)
        plot_histogram(histogram_frame_2_quand_1, signal_ko_2, int(bins_var.get()))
        create_parameters_tab(param_frame_2_quand_1, signal_ko_2, time_1, "Szum impulsowy")
        plot_histogram(histogram_frame_2_quand_2, signal_kz_2, int(bins_var.get()))
        create_parameters_tab(param_frame_2_quand_2, signal_kz_2, time_1, "Szum impulsowy")
    else:
        signal_ko_1 = clippQuant(signal_samp_1, int(num_levels))
        signal_kz_1 = roundQuant(signal_samp_1, int(num_levels))
        plot_signal_quant(time_samp_1, signal_ko_1, "test", quad1_frame_1, histogram_frame_3, time_1, signal_1, signal_samp_1, time_samp_1)
        plot_signal_quant(time_samp_1, signal_kz_1, "test", quad2_frame_1, histogram_frame_3, time_1, signal_1, signal_samp_1, time_samp_1)
        plot_histogram(histogram_frame_1_quand_1, signal_ko_1, int(bins_var.get()))
        create_parameters_tab(param_frame_1_quand_1, signal_ko_1, time_1, "Szum impulsowy")
        plot_histogram(histogram_frame_1_quand_2, signal_kz_1, int(bins_var.get()))
        create_parameters_tab(param_frame_1_quand_2, signal_kz_1, time_1, "Szum impulsowy")

def reconstructionFun(param):
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2, T_1, T_2, signal_rp_1, signal_rp_2, signal_rs_1, signal_rs_2,signal_rz_2,signal_rz_1


    if signal_notebook.index(signal_notebook.select()) == 1:
        time_temp, signal_rz_2 = zeroOrderHold(signal_samp_2, time_samp_2, time_2)
        plot_signal_quant(time_temp, signal_rz_2, "test", rec1_frame_2, histogram_frame_3, time_2, signal_2, signal_samp_2, time_samp_2)
        time_temp, signal_rp_2 = firstOrderHold(signal_samp_2, time_samp_2 )
        plot_signal_quant(time_temp, signal_rp_2, "test", rec2_frame_2, histogram_frame_3, time_2, signal_2, signal_samp_2, time_samp_2)
        len = np.round(abs(time_samp_2[-1] - time_samp_2[0]))*1000
        t = np.linspace(time_samp_2[0], time_samp_2[-1], int(len))
        signal_rs_2 = np.array([valueFunc(ti, signal_samp_1, time_samp_1, int(param)) for ti in t])
        plot_signal_quant(t, signal_rs_2, "test", rec3_frame_2, histogram_frame_3, time_2, signal_2, signal_samp_2, time_samp_2)
        plot_histogram(histogram_frame_2_rec_1, signal_rz_2, int(bins_var.get()))
        create_parameters_tab(param_frame_2_rec_1, signal_rz_2, time_1, "Szum impulsowy")
        plot_histogram(histogram_frame_2_rec_2, signal_rp_2, int(bins_var.get()))
        create_parameters_tab(param_frame_2_rec_2, signal_rp_2, time_1, "Szum impulsowy")
        plot_histogram(histogram_frame_2_rec_3, signal_rs_2, int(bins_var.get()))
        create_parameters_tab(param_frame_2_rec_3, signal_rs_2, time_1, "Szum impulsowy")
    else:
        time_temp, signal_rz_1 = zeroOrderHold(signal_samp_1, time_samp_1, time_1)
        plot_signal_quant(time_temp, signal_rz_1, "test", rec1_frame_1, histogram_frame_3, time_1, signal_1, signal_samp_1, time_samp_1)
        time_temp_1, signal_rp_1 = firstOrderHold(signal_samp_1, time_samp_1 )
        plot_signal_quant(time_temp_1, signal_rp_1, "test", rec2_frame_1, histogram_frame_3, time_1, signal_1, signal_samp_1, time_samp_1)
        len = np.round(abs(time_samp_1[-1] - time_samp_1[0]))*1000
        t = np.linspace(time_samp_1[0], time_samp_1[-1], int(len))
        signal_rs_1 = np.array([valueFunc(ti, signal_samp_1, time_samp_1, int(param)) for ti in t])
        plot_signal_quant(t, signal_rs_1, "test", rec3_frame_1, histogram_frame_3, time_1, signal_1, signal_samp_1, time_samp_1)
        plot_histogram(histogram_frame_1_rec_1, signal_rz_1, int(bins_var.get()))
        create_parameters_tab(param_frame_1_rec_1, signal_rz_1, time_1, "Szum impulsowy")
        plot_histogram(histogram_frame_1_rec_2, signal_rp_1, int(bins_var.get()))
        create_parameters_tab(param_frame_1_rec_2, signal_rp_1, time_1, "Szum impulsowy")
        plot_histogram(histogram_frame_1_rec_3, signal_rs_1, int(bins_var.get()))
        create_parameters_tab(param_frame_1_rec_3, signal_rs_1, time_1, "Szum impulsowy")

def similarityCheck():
    temp, temp2, temp3, temp4, temp5 = similaryCheckFun()

    ttk.Label(tab_similarity, text=f"MSE: {temp:.3f}").grid(row=3, column=0, padx=10, pady=10)

    ttk.Label(tab_similarity, text=f"SNR: {temp2:.3f}").grid(row=4, column=0, padx=10, pady=10)

    ttk.Label(tab_similarity, text=f"PSNR: {temp3:.3f}").grid(row=5, column=0, padx=10, pady=10)

    ttk.Label(tab_similarity, text=f"MD: {temp4:.3f}").grid(row=6, column=0, padx=10, pady=10)

    ttk.Label(tab_similarity, text=f"ENOB: {temp5:.3f}").grid(row=7, column=0, padx=10, pady=10)

def similaryCheckFun():
    global singal_1, time_1, signal_2, time_2, signal_3, time_3, signal_samp_1, time_samp_1, signal_samp_2, time_samp_2, signal_rp_1, signal_rp_2, signal_rs_1, signal_rs_2,signal_rz_2,signal_rz_2,  signal_ko_1, signal_ko_2, signal_kz_1, signal_kz_2
    if signal_sim.get() == "Sygnał 1":
        orginal = signal_1
    elif signal_sim.get() == "Sygnał 2":
        orginal = signal_2
    elif signal_sim.get() == "Sygnał 3":
        orginal = signal_3
    elif signal_sim.get() == "Sygnał 1 Próbkowanie":
        orginal = signal_samp_1
    elif signal_sim.get() == "Sygnał 1 Kwantyzacja z obcięciem":
        orginal = signal_ko_1
    elif signal_sim.get() == "Sygnał 1 Kwantyzacja z zaokrągleniem":
        orginal = signal_kz_1
    elif signal_sim.get() == "Sygnał 1 Rekonstrukcja zerowego rzędu":
        orginal = signal_rz_1
    elif signal_sim.get() == "Sygnał 1 Rekonstrukcja pierwszego rzędu":
        orginal = signal_rp_1
    elif signal_sim.get() == "Sygnał 1 Rekonstrukcja funkcja sinc":
        orginal = signal_rs_1
    elif signal_sim.get() == "Sygnał 2 Próbkowanie":
        orginal = signal_samp_2
    elif signal_sim.get() == "Sygnał 2 Kwantyzacja z obcięciem":
        orginal = signal_ko_2
    elif signal_sim.get() == "Sygnał 2 Kwantyzacja z zaokrągleniem":
        orginal = signal_kz_2
    elif signal_sim.get() == "Sygnał 2 Rekonstrukcja zerowego rzędu":
        orginal = signal_rz_2
    elif signal_sim.get() == "Sygnał 2 Rekonstrukcja pierwszego rzędu":
        orginal = signal_rp_2
    elif signal_sim.get() == "Sygnał 2 Rekonstrukcja funkcja sinc":
        orginal = signal_rs_2

    if signal_sim_2.get() == "Sygnał 1":
        reconstructed = signal_1
    elif signal_sim_2.get() == "Sygnał 2":
        reconstructed = signal_2
    elif signal_sim_2.get() == "Sygnał 3":
        reconstructed = signal_3
    elif signal_sim_2.get() == "Sygnał 1 Próbkowanie":
        reconstructed = signal_samp_1
    elif signal_sim_2.get() == "Sygnał 1 Kwantyzacja z obcięciem":
        reconstructed = signal_ko_1
    elif signal_sim_2.get() == "Sygnał 1 Kwantyzacja z zaokrągleniem":
        reconstructed = signal_kz_1
    elif signal_sim_2.get() == "Sygnał 1 Rekonstrukcja zerowego rzędu":
        reconstructed = signal_rz_1
    elif signal_sim_2.get() == "Sygnał 1 Rekonstrukcja pierwszego rzędu":
        reconstructed = signal_rp_1
    elif signal_sim_2.get() == "Sygnał 1 Rekonstrukcja funkcja sinc":
        reconstructed = signal_rs_1
    elif signal_sim_2.get() == "Sygnał 2 Próbkowanie":
        reconstructed = signal_samp_2
    elif signal_sim_2.get() == "Sygnał 2 Kwantyzacja z obcięciem":
        reconstructed = signal_ko_2
    elif signal_sim_2.get() == "Sygnał 2 Kwantyzacja z zaokrągleniem":
        reconstructed = signal_kz_2
    elif signal_sim_2.get() == "Sygnał 2 Rekonstrukcja zerowego rzędu":
        reconstructed = signal_rz_2
    elif signal_sim_2.get() == "Sygnał 2 Rekonstrukcja pierwszego rzędu":
        reconstructed = signal_rp_2
    elif signal_sim_2.get() == "Sygnał 2 Rekonstrukcja funkcja sinc":
        reconstructed = signal_rs_2

    temp = mse(orginal, reconstructed)
    temp2 = snr(orginal, reconstructed)
    temp3 = psnr(orginal, reconstructed)
    temp4 = max_diff(orginal, reconstructed)
    temp5 = enob(orginal, reconstructed)

    return temp, temp2, temp3, temp4, temp5

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

def create_parameters_tab(param, signal, time, type):
    for widget in param.winfo_children():
        widget.destroy()

    if type not in ["Impuls jednostkowy", "Szum impulsowy"]:
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
        avg_label = ttk.Label(param, text=f"Średnia: {cp.avg_dis(signal):.3f}")
        avg_label.pack(padx=5, pady=5)

        abs_avg_label = ttk.Label(param, text=f"Średnia bezwzględna: {cp.abs_avg_dis(signal):.3f}")
        abs_avg_label.pack(padx=5, pady=5)

        power_label = ttk.Label(param, text=f"Moc: {cp.power_dis(signal):.3f}")
        power_label.pack(padx=5, pady=5)

        dev_label = ttk.Label(param, text=f"Wariancja: {cp.dev_dis(signal):.3f}")
        dev_label.pack(padx=5, pady=5)

        eff_power_label = ttk.Label(param, text=f"Wartość skuteczna: {cp.eff_power_dis(signal):.3f}")
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
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_samp_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=plot_frame_samp_2)
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
    canvas = FigureCanvasTkAgg(fig, master=rec1_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=rec2_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=rec3_frame_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=rec1_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=rec2_frame_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=rec3_frame_2)
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
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_samp_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_samp_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1_quand_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2_quand_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1_quand_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2_quand_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1_rec_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1_rec_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_1_rec_3)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2_rec_1)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2_rec_2)
    canvas.get_tk_widget().pack()
    canvas.draw()
    canvas = FigureCanvasTkAgg(fig, master=histogram_frame_2_rec_3)
    canvas.get_tk_widget().pack()
    canvas.draw()

def increase_bins():
    bins_var.set(min(bins_var.get() + 1, 20))

def decrease_bins():
    bins_var.set(max(bins_var.get() - 1, 5))

def operate_signals_sel(operation, signal_1_sel, time_1_sel, signal_2_sel, time_2_sel):
    global signal_3, time_3, time_samp_1, time_samp_2
    if operation not in ["convolve", "splot", "direct"]:
        sampling_rate_1 = round(1 / (time_1[1] - time_1[0]), 2) if len(time_1) > 1 else 1.0
        sampling_rate_2 = round(1 / (time_2[1] - time_2[0]), 2) if len(time_2) > 1 else 1.0

        if sampling_rate_1 != sampling_rate_2:
            print("Różne próbkowanie")
        else:
            signal_3, time_3 = so.operate_signals(operation, signal_1_sel, time_1_sel, signal_2_sel, time_2_sel)
    elif operation == "convolve":
        signal_3 = convolve(signal_1_sel, signal_2_sel)
        time_3 = convolve_time_axis(time_1_sel, time_2_sel)
    elif operation == "direct":
        signal_3 = cross_correlation_direct(signal_1_sel, signal_2_sel)
        time_3 = convolve_time_axis(time_1_sel, time_2_sel)
    elif operation == "splot":
        signal_3 = cross_convolution(signal_1_sel, signal_2_sel)
        time_3 = convolve_time_axis(time_1_sel, time_2_sel)
    plot_signal(time_3, signal_3, "Wynik", plot_frame_3, histogram_frame_3)
    plot_histogram(histogram_frame_3, signal_3, int(bins_var.get()))

    if operation not in ["convolve", "splot", "direct"]:
        create_parameters_tab(param_frame_3, signal_3, time_3, signal_type.get())

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
        create_parameters_tab(param_frame_3, signal_3, time_3, signal_type.get())

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
        create_parameters_tab(param, signal, time, signal_type.get())
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
    global signal_1, time_1, signal_2, time_2, signal_samp_2, signal_samp_1, time_samp_1, time_samp_2

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
            if operation in ["convolve", "splot", "direct"]:
                selected_signal_1 = signal_samp_1
                selected_time_1 = time_samp_1
                selected_signal_2 = signal_samp_2
                selected_time_2 = time_samp_2
            else:
                selected_signal_1 = signal_1
                selected_time_1 = time_1
                selected_signal_2 = signal_2
                selected_time_2 = time_2
        elif signal_1_var.get() == "Sygnał 2" and signal_2_var.get() == "Sygnał 1":
            if operation in ["convolve", "splot", "direct"]:
                selected_signal_1 = signal_samp_2
                selected_time_1 = time_samp_2
                selected_signal_2 = signal_samp_1
                selected_time_2 = time_samp_1
            else:
                selected_signal_1 = signal_2
                selected_time_1 = time_2
                selected_signal_2 = signal_1
                selected_time_2 = time_1

        print(f"Wybrano {selected_signal_1} i {selected_signal_2} do operacji.")
        popup.destroy()
        operate_signals_sel(operation, selected_signal_1, selected_time_1, selected_signal_2, selected_time_2)
        #operate_signals_sel(operation, signal_samp_1, selected_time_1, signal_samp_2, selected_time_2)


    confirm_button = ttk.Button(popup, text="Potwierdź", command=lambda: confirm_selection(operation))
    confirm_button.pack(pady=10)

    popup.transient(root)
    popup.grab_set()
    root.wait_window(popup)

def filter():
    signal = high_pass_filter()
    sample_rate = 100
    M = 15
    print("filtr")
    print(signal)
    time_conv = np.arange(0, (1 / sample_rate) * M, 1 / sample_rate)
    print(time_conv)
    plot_signal(time_conv, signal, "Szum impulsowy", plot_frame_3, histogram_frame_3)

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
root.geometry("1510x600")
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
tab_similarity = ttk.Frame(main_notebook)

main_notebook.add(tab_generate, text="Generowanie")
main_notebook.add(tab_operations, text="Operacje matematyczne")
main_notebook.add(tab_save, text="Zapis i odczyt")
main_notebook.add(tab_sampAndQuant, text="Próbkowanie i kwantyzacja")
main_notebook.add(tab_reconstruction, text="Rekonstrukcja")
main_notebook.add(tab_similarity, text="Porównanie")

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
ttk.Button(tab_operations, text="Korelacja bezpośrednia", command=lambda: select_signals_for_operation("direct")).grid(row=3, column=0, padx=100, pady=5)
ttk.Button(tab_operations, text="Korelacja splot", command=lambda: select_signals_for_operation("splot")).grid(row=3, column=1, padx=10, pady=5)
ttk.Button(tab_operations, text="Splot", command=lambda: select_signals_for_operation("convolve")).grid(row=4, column=0, padx=100, pady=5)
ttk.Button(tab_operations, text="filtr", command=lambda: filter()).grid(row=4, column=1, padx=100, pady=5)

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

# --- PORÓWNANIE ---
ttk.Label(tab_similarity, text="Pierwszy sygnał:").grid(row=0, column=0, padx=5, pady=5)
signal_sim = ttk.Combobox(tab_similarity,
                               values=["Sygnał 1", "Sygnał 2", "Sygnał 3",
                                       "Sygnał 1 Próbkowanie",
                                       "Sygnał 1 Kwantyzacja z obcięciem",
                                       "Sygnał 1 Kwantyzacja z zaokrągleniem",
                                       "Sygnał 1 Rekonstrukcja zerowego rzędu",
                                       "Sygnał 1 Rekonstrukcja pierwszego rzędu",
                                       "Sygnał 1 Rekonstrukcja funkcja sinc",
                                       "Sygnał 2 Próbkowanie",
                                       "Sygnał 2 Kwantyzacja z obcięciem",
                                       "Sygnał 2 Kwantyzacja z zaokrągleniem",
                                       "Sygnał 2 Rekonstrukcja zerowego rzędu",
                                       "Sygnał 2 Rekonstrukcja pierwszego rzędu",
                                       "Sygnał 2 Rekonstrukcja funkcja sinc"],
                               state="readonly", width=50)
signal_sim.grid(row=1, column=0, padx=5, pady=5)
signal_sim.bind("<<ComboboxSelected>>")

ttk.Label(tab_similarity, text="Drugi sygnał:").grid(row=0, column=1, padx=5, pady=5)
signal_sim_2 = ttk.Combobox(tab_similarity,
                               values=["Sygnał 1", "Sygnał 2", "Sygnał 3",
                                       "Sygnał 1 Próbkowanie",
                                       "Sygnał 1 Kwantyzacja z obcięciem",
                                       "Sygnał 1 Kwantyzacja z zaokrągleniem",
                                       "Sygnał 1 Rekonstrukcja zerowego rzędu",
                                       "Sygnał 1 Rekonstrukcja pierwszego rzędu",
                                       "Sygnał 1 Rekonstrukcja funkcja sinc",
                                       "Sygnał 2 Próbkowanie",
                                       "Sygnał 2 Kwantyzacja z obcięciem",
                                       "Sygnał 2 Kwantyzacja z zaokrągleniem",
                                       "Sygnał 2 Rekonstrukcja zerowego rzędu",
                                       "Sygnał 2 Rekonstrukcja pierwszego rzędu",
                                       "Sygnał 2 Rekonstrukcja funkcja sinc"],
                               state="readonly", width=50)
signal_sim_2.grid(row=1, column=1, padx=5, pady=5)
signal_sim_2.bind("<<ComboboxSelected>>")

ttk.Button(tab_similarity, text="Porównaj", command=lambda: similarityCheck()).grid(row=2, column=0, padx=10, pady=10)

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

sygnal_frame = ttk.Notebook(notebook)
sam_frame = ttk.Notebook(notebook)
quad_frame_1 = ttk.Notebook(notebook)
quad_frame_2 = ttk.Notebook(notebook)
rec_frame_1 = ttk.Notebook(notebook)
rec_frame_2 = ttk.Notebook(notebook)
rec_frame_3 = ttk.Notebook(notebook)

notebook.add(sygnal_frame, text="Sygnał")
notebook.add(sam_frame, text="Próbkowanie")
notebook.add(quad_frame_1, text="Kwantyzacja z obcięciem")
notebook.add(quad_frame_2, text="Kwantyzacja z zaokrągleniem")
notebook.add(rec_frame_1, text="Rekonstrukcja ZOH")
notebook.add(rec_frame_2, text="Rekonstrukcja FOH")
notebook.add(rec_frame_3, text="Rekonstrukcja Sinc")

plot_frame_1 = ttk.Frame(sygnal_frame)
histogram_frame_1 = ttk.Frame(sygnal_frame)
param_frame_1 = ttk.Frame(sygnal_frame)
sygnal_frame.add(plot_frame_1, text="Wykres")
sygnal_frame.add(histogram_frame_1, text="Histogram")
sygnal_frame.add(param_frame_1, text="Parametry")

plot_frame_samp_1 = ttk.Frame(sam_frame)
histogram_frame_samp_1 = ttk.Frame(sam_frame)
param_frame_samp_1 = ttk.Frame(sam_frame)
sam_frame.add(plot_frame_samp_1, text="Wykres")
sam_frame.add(histogram_frame_samp_1, text="Histogram")
sam_frame.add(param_frame_samp_1, text="Parametry")

quad1_frame_1 = ttk.Frame(quad_frame_1)
histogram_frame_1_quand_1 = ttk.Frame(quad_frame_1)
param_frame_1_quand_1 = ttk.Frame(quad_frame_1)
quad_frame_1.add(quad1_frame_1, text="Wykres")
quad_frame_1.add(histogram_frame_1_quand_1, text="Histogram")
quad_frame_1.add(param_frame_1_quand_1, text="Parametry")

quad2_frame_1 = ttk.Frame(quad_frame_2)
histogram_frame_1_quand_2 = ttk.Frame(quad_frame_2)
param_frame_1_quand_2 = ttk.Frame(quad_frame_2)
quad_frame_2.add(quad2_frame_1, text="Wykres")
quad_frame_2.add(histogram_frame_1_quand_2, text="Histogram")
quad_frame_2.add(param_frame_1_quand_2, text="Parametry")

rec1_frame_1 = ttk.Frame(rec_frame_1)
histogram_frame_1_rec_1 = ttk.Frame(rec_frame_1)
param_frame_1_rec_1 = ttk.Frame(rec_frame_1)
rec_frame_1.add(rec1_frame_1, text="Wykres")
rec_frame_1.add(histogram_frame_1_rec_1, text="Histogram")
rec_frame_1.add(param_frame_1_rec_1, text="Parametry")

rec2_frame_1 = ttk.Frame(rec_frame_2)
histogram_frame_1_rec_2 = ttk.Frame(rec_frame_2)
param_frame_1_rec_2 = ttk.Frame(rec_frame_2)
rec_frame_2.add(rec2_frame_1, text="Wykres")
rec_frame_2.add(histogram_frame_1_rec_2, text="Histogram")
rec_frame_2.add(param_frame_1_rec_2, text="Parametry")

rec3_frame_1 = ttk.Frame(rec_frame_3)
histogram_frame_1_rec_3 = ttk.Frame(rec_frame_3)
param_frame_1_rec_3 = ttk.Frame(rec_frame_3)
rec_frame_3.add(rec3_frame_1, text="Wykres")
rec_frame_3.add(histogram_frame_1_rec_3, text="Histogram")
rec_frame_3.add(param_frame_1_rec_3, text="Parametry")


notebook = ttk.Notebook(signal_frame_2)
notebook.pack(expand=True, fill='both')
sam_frame = ttk.Notebook(notebook)
quad_frame_1 = ttk.Notebook(notebook)
quad_frame_2 = ttk.Notebook(notebook)
rec_frame_1 = ttk.Notebook(notebook)
rec_frame_2 = ttk.Notebook(notebook)
rec_frame_3 = ttk.Notebook(notebook)
sygnal_frame = ttk.Notebook(notebook)

notebook.add(sygnal_frame, text="Sygnał")
notebook.add(sam_frame, text="Próbkowanie")
notebook.add(quad_frame_1, text="Kwantyzacja z obcięciem")
notebook.add(quad_frame_2, text="Kwantyzacja z zaokrągleniem")
notebook.add(rec_frame_1, text="Rekonstrukcja ZOH")
notebook.add(rec_frame_2, text="Rekonstrukcja FOH")
notebook.add(rec_frame_3, text="Rekonstrukcja Sinc")

plot_frame_2 = ttk.Frame(sygnal_frame)
histogram_frame_2 = ttk.Frame(sygnal_frame)
param_frame_2 = ttk.Frame(sygnal_frame)
sygnal_frame.add(plot_frame_2, text="Wykres")
sygnal_frame.add(histogram_frame_2, text="Histogram")
sygnal_frame.add(param_frame_2, text="Parametry")

plot_frame_samp_2 = ttk.Frame(sam_frame)
histogram_frame_samp_2 = ttk.Frame(sam_frame)
param_frame_samp_2 = ttk.Frame(sam_frame)
sam_frame.add(plot_frame_samp_2, text="Wykres")
sam_frame.add(histogram_frame_samp_2, text="Histogram")
sam_frame.add(param_frame_samp_2, text="Parametry")

quad1_frame_2 = ttk.Frame(quad_frame_1)
histogram_frame_2_quand_1 = ttk.Frame(quad_frame_1)
param_frame_2_quand_1 = ttk.Frame(quad_frame_1)
quad_frame_1.add(quad1_frame_2, text="Wykres")
quad_frame_1.add(histogram_frame_2_quand_1, text="Histogram")
quad_frame_1.add(param_frame_2_quand_1, text="Parametry")

quad2_frame_2 = ttk.Frame(quad_frame_2)
histogram_frame_2_quand_2 = ttk.Frame(quad_frame_2)
param_frame_2_quand_2 = ttk.Frame(quad_frame_2)
quad_frame_2.add(quad2_frame_2, text="Wykres")
quad_frame_2.add(histogram_frame_2_quand_2, text="Histogram")
quad_frame_2.add(param_frame_2_quand_2, text="Parametry")

rec1_frame_2 = ttk.Frame(rec_frame_1)
histogram_frame_2_rec_1 = ttk.Frame(rec_frame_1)
param_frame_2_rec_1 = ttk.Frame(rec_frame_1)
rec_frame_1.add(rec1_frame_2, text="Wykres")
rec_frame_1.add(histogram_frame_2_rec_1, text="Histogram")
rec_frame_1.add(param_frame_2_rec_1, text="Parametry")

rec2_frame_2 = ttk.Frame(rec_frame_2)
histogram_frame_2_rec_2 = ttk.Frame(rec_frame_2)
param_frame_2_rec_2 = ttk.Frame(rec_frame_2)
rec_frame_2.add(rec2_frame_2, text="Wykres")
rec_frame_2.add(histogram_frame_2_rec_2, text="Histogram")
rec_frame_2.add(param_frame_2_rec_2, text="Parametry")

rec3_frame_2 = ttk.Frame(rec_frame_3)
histogram_frame_2_rec_3 = ttk.Frame(rec_frame_3)
param_frame_2_rec_3 = ttk.Frame(rec_frame_3)
rec_frame_3.add(rec3_frame_2, text="Wykres")
rec_frame_3.add(histogram_frame_2_rec_3, text="Histogram")
rec_frame_3.add(param_frame_2_rec_3, text="Parametry")

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