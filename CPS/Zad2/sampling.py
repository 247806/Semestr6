import numpy as np

import continousSignal


def sampling(signal, time, sample_rate, T):
    A = max(abs(s) for s in signal)
    t1 = time[0]
    d = np.round(abs(time[-1] - time[0]),2)
    print('dupa')
    print(T)
    print(d)
    time_samp = np.arange(t1, d, 1 / sample_rate)

    signal_samp = continousSignal.sinusoidal(A, T, time_samp)
    print(len(signal_samp))
    print(len(time_samp))
    return signal_samp, time_samp