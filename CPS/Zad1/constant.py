import numpy as np
import matplotlib.pyplot as plt

def sinusoidal(A, T, t1, d):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
    signal = A * np.sin((2 * np.pi / T) * time)
    return time, signal

def halfWaveSinusoidal(A, T, t1, d):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
    signal = 0.5 * A * (np.sin((2 * np.pi / T) * time) + np.abs(np.sin((2 * np.pi / T) * time)))
    return time, signal

def halfSinusoidal(A, T, t1, d):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
    signal = A * np.abs(np.sin((2 * np.pi / T) * time))
    return time, signal


def square(A, T, t1, d, kw):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
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

    return time, signal

def squareSymetric(A, T, t1, d, kw):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
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

    return time, signal

def triangle(A, T, t1, d, kw):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
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

    return time, signal

def ones(A, t1, d, ts):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
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

    return time, signal



def plot_signal(time, signal, signal_type):
    plt.figure()
    plt.plot(time, signal)
    plt.xlabel("Time [s]")
    plt.ylabel("Amplitude")
    plt.title(f"{signal_type.capitalize()} Signal")
    plt.grid()
    plt.show()


if __name__ == '__main__':
    time, signal = triangle(A=10, T =2,  t1=0, d=10, kw=0.5)
    plot_signal(time, signal, 'sinusoidal')
