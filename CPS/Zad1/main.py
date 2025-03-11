import numpy as np
import matplotlib.pyplot as plt

def sinusoidal(A, T, t1, d):
    sample_rate = 1000
    time = np.arange(t1, d, 1 / sample_rate)
    signal = A * np.sin((2 * np.pi / T) * time)
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

    print('1. Szum o rozkladzie jednostajnym')
    print('2. Szum gaussowski')
    print('3. Szum sinusoidalny')
    i = int(input('Wybór: '))

    if i == 3:
        time, signal = sinusoidal(A=10, T=6, t1=0, d=10)
        plot_signal(time, signal, 'sinusoidal')
    else:
        print('aaa')
