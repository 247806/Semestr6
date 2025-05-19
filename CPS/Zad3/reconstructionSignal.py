import numpy as np
from scipy.interpolate import interp1d


def zeroOrderHold(signal, time, ori_time):
    sampling_rate = 1000
    len_t_zoh = int(np.round((ori_time[-1] - time[0]) * sampling_rate))
    t_zoh = np.linspace(time[0], ori_time[-1], len_t_zoh, endpoint=False)

    x_zoh = []

    for i in range(1, len(time)):
        num_points = int((time[i] - time[i - 1]) * sampling_rate)
        x_zoh.extend([signal[i - 1]] * num_points)

    # Dopasowanie długości x_zoh do t_zoh
    if len(x_zoh) < len(t_zoh):
        x_zoh.extend([signal[-1]] * (len(t_zoh) - len(x_zoh)))
    elif len(x_zoh) > len(t_zoh):
        x_zoh = x_zoh[:len(t_zoh)]

    # Dodaj końcowy punkt ori_time[-1] i ostatnią wartość sygnału
    if t_zoh[-1] < ori_time[-1]:
        t_zoh = np.append(t_zoh, ori_time[-1])
        x_zoh.append(signal[-1])

    print(len(t_zoh))
    print(len(x_zoh))
    return t_zoh, np.array(x_zoh)

def firstOrderHold(signal, time):
    foh = interp1d(time, signal, kind='linear')
    lens = np.round(abs(time[-1] - time[0])) * 1000
    t_foh = np.linspace(time[0], time[-1], int(lens))
    x_foh = foh(t_foh)

    return t_foh, x_foh

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