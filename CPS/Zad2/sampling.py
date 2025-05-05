import numpy as np

import continousSignal
from functionType import function_type


def sampling(signal, time, sample_rate, T, signal_type, p, ts, kw):
    A = max(abs(s) for s in signal)
    t1 = time[0]
    d = np.round(abs(time[-1] - time[0]),2)
    print('Sampling')
    print(T)
    print(d)
    #print(time)
    #time_samp = np.arange(t1, t1 + d, 1 / sample_rate)
    time_samp = np.arange(t1, t1 + d, 1 / sample_rate)
    last_value = t1 + d
    time_samp = np.append(time_samp, last_value)
    time_temp, signal_samp = function_type(A, T, t1, d, kw, ts, p, signal, signal_type, time_samp, sample_rate)
    #signal_samp = continousSignal.sinusoidal(A, T, time_samp)
    return signal_samp, time_samp