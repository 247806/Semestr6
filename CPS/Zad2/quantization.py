import numpy as np


def clippQuant(signal, time):
    new_time = []
    for value in time:
        new_time.append(round(value - 0.001, 3))
        new_time.append(round(value, 3))
         # Dodajemy element mniejszy o 0.001
    new_time = new_time[1:]
    new_signal = []
    for value in signal:
        new_signal.append(value)
        new_signal.append(value)

    new_signal = new_signal[:-1]

    print(new_signal)
    print(new_time)

    return new_time, new_signal