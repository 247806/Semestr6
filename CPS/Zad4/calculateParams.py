import numpy as np
from scipy.integrate import simpson

def avg_cont(signal, time):
    integral = simpson(signal, time)
    return integral / (time[-1] - time[0])

def avg_dis(signal):
    amount = np.sum(signal)
    print(amount)
    return amount / len(signal)

def abs_avg_cont(signal, time):
    abs_signal = np.abs(signal)
    integral = simpson(abs_signal, time)
    return integral / (time[-1] - time[0])

def abs_avg_dis(signal):
    abs_signal = np.abs(signal)
    amount = np.sum(abs_signal)
    return amount / len(signal)

def power_cont(signal, time):
    signal_sq = signal ** 2
    integral = simpson(signal_sq, time)
    return integral / (time[-1] - time[0])

def power_dis(signal):
    signal_sq = signal ** 2
    amount = np.sum(signal_sq)
    return amount / len(signal)

def dev_cont(signal, time):
    mean_signal = np.mean(signal)
    signal_sq = (signal - mean_signal) ** 2
    integral = simpson(signal_sq, time)
    return integral / (time[-1] - time[0])

def dev_dis(signal):
    mean_signal = np.mean(signal)
    signal_sq = (signal - mean_signal) ** 2
    amount = np.sum(signal_sq)
    return amount / len(signal)

def eff_power_cont(signal, time):
    signal_sq = signal ** 2
    integral = simpson(signal_sq, time)
    return np.sqrt(integral / (time[-1] - time[0]))

def eff_power_dis(signal):
    signal_sq = signal ** 2
    amount = np.sum(signal_sq)
    return np.sqrt(amount / len(signal))