import numpy as np
from scipy.interpolate import interp1d


def zeroOrderHold(signal, time):
    len_t_zoh = np.round(abs(time[-1] - time[0]) * 1000).astype(int)  # Zakładając 1000 punktów na jednostkę czasu
    t_zoh = np.linspace(time[0], time[-1], len_t_zoh, endpoint=False)  # Nowa tablica czasów (z endpoint=False)

    # Dopasowanie długości t_zoh do time[-1]
    if t_zoh[-1] != time[-1]:
        t_zoh = np.append(t_zoh, time[-1])  # Dodajemy time[-1] na końcu

    # Lista do trzymania wyników
    x_zoh = []

    # Iterujemy przez oryginalny czas i powielamy wartości sygnału
    for i in range(1, len(time)):
        # Powielamy poprzednią wartość odpowiednią liczbę razy
        num_points = int((time[i] - time[i - 1]) * 1000)  # Liczba punktów między time[i-1] a time[i]
        x_zoh.extend([signal[i - 1]] * num_points)  # Dodajemy wartość signal[i-1] tyle razy, ile wynosi num_points

    # Dopasowanie długości, jeśli to konieczne
    if len(x_zoh) < len(t_zoh):
        # Jeśli x_zoh jest krótsze, dodajemy na końcu ostatnią wartość sygnału
        x_zoh.extend([signal[-1]] * (len(t_zoh) - len(x_zoh)))
    elif len(x_zoh) > len(t_zoh):
        # Jeśli x_zoh jest za długie, przycinamy do długości t_zoh
        x_zoh = x_zoh[:len(t_zoh)]
    print(t_zoh)
    return t_zoh, x_zoh

def firstOrderHold(signal, time):
    foh = interp1d(time, signal, kind='linear')
    len = np.round(abs(time[-1] - time[0])) * 1000
    t_foh = np.linspace(time[0], time[-1], int(len))
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