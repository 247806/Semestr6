import numpy as np

import continousSignal


def sampling(signal, time, sample_rate):
    A = max(abs(s) for s in signal)
    t1 = time[0]
    T = 1
    d = 12

    time_samp = np.arange(t1, d, 1 / sample_rate)

    signal_samp = continousSignal.sinusoidal(A, T, time_samp)
    print(len(signal_samp))
    print(len(time_samp))
    return signal_samp, time_samp