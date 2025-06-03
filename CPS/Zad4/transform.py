import numpy as np
import matplotlib.pyplot as plt
import math
import cmath
from continousSignal import sinusoidal

# --- DFT (IDFT jeśli podzielisz przez N) ---
def transform(x):
    N = len(x)
    Warg = 2.0 * math.pi / N
    W = cmath.exp(1j * Warg)

    X = []

    for m in range(N):
        sum_ = 0j
        for n in range(N):
            sum_ += x[n] * (W ** (-m * n))
        X.append(sum_ / N)
    return X

# --- Tryby wykresów ---
def plot_W1(X, fs):
    N = len(X)
    freqs = np.arange(N) * fs / N
    real = [z.real for z in X]
    imag = [z.imag for z in X]

    plt.figure(figsize=(10, 6))
    plt.subplot(2, 1, 1)
    plt.plot(freqs, real)
    plt.title("W1 – Część rzeczywista amplitudy 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("Re")

    plt.subplot(2, 1, 2)
    plt.plot(freqs, imag)
    plt.title("W1 – Część urojona amplitudy 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("Im")

    plt.tight_layout()
    plt.show()


def plot_W2(X, fs):
    N = len(X)
    freqs = np.arange(N) * fs / N
    magnitudes = [abs(z) for z in X]
    phases = [cmath.phase(z) for z in X]

    print("Min freq:", min(freqs))
    print("Max freq:", max(freqs))
    print("Sample rate fs:", fs)
    print("N (długość sygnału):", N)

    plt.figure(figsize=(10, 6))
    plt.subplot(2, 1, 1)
    plt.plot(freqs, magnitudes)
    plt.title("W2 – Moduł (amplituda) 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("|X(f)|")

    plt.subplot(2, 1, 2)
    plt.plot(freqs, phases)
    plt.title("W2 – Argument (faza) 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("∠X(f) [rad]")

    plt.tight_layout()
    plt.show()



# --- Przykładowy sygnał ---
fs = 10  # Hz, częstotliwość próbkowania
t = np.arange(0, 1, 1/fs)
f0 = 50  # Hz
time = np.arange(0, 0 + 5, 1 / 10)
signal = sinusoidal(1, 1, time)
x = [complex(s, 0) for s in signal]

# --- Obliczenie DFT ---
X = transform(x)

# --- Wybór trybu prezentacji ---
mode = "W22"  # W1 lub W2

if mode == "W1":
    plot_W1(X, fs)
elif mode == "W2":
    plot_W2(X, fs)
else:
    plot_W1(X, fs)
    plot_W2(X, fs)
