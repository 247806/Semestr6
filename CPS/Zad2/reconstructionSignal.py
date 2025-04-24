import numpy as np
from scipy.interpolate import interp1d


def zeroOrderHold(signal, time):
    t_zoh = np.repeat(time, 2)[1:]
    x_zoh = np.repeat(signal, 2)[:-1]

    return t_zoh, x_zoh

def firstOrderHold(signal, time):
    foh = interp1d(time, signal, kind='linear')
    t_foh = np.linspace(time[0], time[-1], 1000)
    x_foh = foh(t_foh)

    return t_foh, x_foh

def sinc_interp(time, signal, t, m=100):
    T = time[1] - time[0]  # Zakładamy równomierne próbkowanie
    signal_interpolated = np.zeros_like(t)  # Przygotowanie pustej tablicy do wyników

    # Iterowanie przez każdą próbkę
    for n in range(len(time)):
        # Obliczanie funkcji sinc dla każdej próbki
        sinc_values = np.sinc((t - time[n]) / T)
        signal_interpolated += signal[n] * sinc_values

    return t, signal_interpolated

def sinc(t):
    return 1.0 if t == 0.0 else np.sin(np.pi * t) / (np.pi * t)

def valueFunc(t, signal_values, time_values, N):
    num_samples = len(signal_values)
    range_start = time_values[0]
    step = time_values[1] - time_values[0]  # Zakładamy równą siatkę

    index = int((t - range_start) / step)

    first_sample = index - N // 2
    last_sample = first_sample + N

    if first_sample < 0:
        last_sample += -first_sample
        first_sample = 0
    if last_sample > num_samples:
        first_sample -= (last_sample - num_samples)
        last_sample = num_samples
    if first_sample < 0:
        first_sample = 0

    sum_value = 0.0
    for i in range(first_sample, last_sample):
        sum_value += signal_values[i] * sinc((t - time_values[i]) / step)

    return sum_value