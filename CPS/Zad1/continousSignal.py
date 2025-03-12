import numpy as np
import matplotlib.pyplot as plt

def random_uniform_signal(A, t1, d, time):
    signal = np.random.uniform(-A, A, size=len(time))
    return signal

def gaussian_noise(A, t1, d, time):
    signal = A * np.random.normal(loc=0, scale=1, size=len(time))
    return signal

def sinusoidal(A, T, t1, d, time):
    signal = A * np.sin((2 * np.pi / T) * time)
    return signal

def halfWaveSinusoidal(A, T, t1, d, time):
    signal = 0.5 * A * (np.sin((2 * np.pi / T) * time) + np.abs(np.sin((2 * np.pi / T) * time)))
    return signal

def halfSinusoidal(A, T, t1, d, time):
    signal = A * np.abs(np.sin((2 * np.pi / T) * time))
    return signal


def square(A, T, t1, d, kw, time):
    signal = np.zeros(len(time))
    k = 0
    i = 0
    for times in time:
        if k * T + t1 <= times < kw * T + k * T + t1:
            signal[i] = A
        elif kw * T - k * T + t1 <= times < T + k * T + t1:
            signal[i] = 0
        if times == T * (k + 1):
            k += 1
        i += 1

    return signal

def squareSymetric(A, T, t1, d, kw, time):
    signal = np.zeros(len(time))
    k = 0
    i = 0
    for times in time:
        if k * T + t1 <= times < kw * T + k * T + t1:
            signal[i] = A
        elif kw * T - k * T + t1 <= times < T + k * T + t1:
            signal[i] = -A
        if times == T * (k + 1):
            k += 1
        i += 1

    return signal

def triangle(A, T, t1, d, kw, time):
    signal = np.zeros(len(time))
    k = 0
    i = 0
    for times in time:
        if k * T + t1 <= times < kw * T + k * T + t1:
            signal[i] = A / (kw * T) * (times - k * T - t1)
        elif kw * T + t1 + k * T <= times < T + k * T + t1:
            signal[i] = -A / (T * (1 - kw)) * (times - k * T - t1) + A / (1 - kw)
        if times == T * (k + 1):
            k += 1
        i += 1

    return signal

def ones(A, t1, d, ts, time):
    signal = np.zeros(len(time))
    i = 0
    for times in time:
        if times > ts:
            signal[i] = A
        elif times == ts:
            signal[i] = A / 2
        else :
            signal[i] = 0
        i += 1

    return signal