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

def reverse_bits(value: int, number_of_bits: int) -> int:
    for i in range(number_of_bits // 2):
        j = number_of_bits - i - 1
        bit_i = (value >> i) & 1
        bit_j = (value >> j) & 1
        if bit_i != bit_j:
            # Zamiana bitów i i j przez XOR
            value ^= (1 << i) | (1 << j)
    return value


def mix_samples(samples):
    length = len(samples)

    # Oblicz liczbę bitów potrzebnych do reprezentacji length
    number_of_bits = 0
    for i in range(32):
        if (1 << i) & length != 0:
            number_of_bits = i
            break

    # Zamiana próbek zgodnie z odwróconymi bitami indeksów
    for i in range(length):
        new_index = reverse_bits(i, number_of_bits)
        if new_index > i:
            samples[i], samples[new_index] = samples[new_index], samples[i]

def calculate_vector_of_W_params(N):
    Warg = 2.0 * math.pi / N
    W = cmath.exp(1j * Warg)  # e^(j*2π/N)
    allW = [W**i for i in range(N // 2)]
    return allW

def retrieve_W_from_vector(N, k, vectorW):
    # normalize to [0, N - 1]
    k = k % N
    if k < 0:
        k += N

    # find k in new N (vectorW length * 2)
    k = int(k * (len(vectorW) * 2 / N))
    if k < len(vectorW):
        return vectorW[k]
    else:
        return vectorW[k - len(vectorW)] * complex(-1, 0)

def transformFFT(x):
    mix_samples(x)  # zakładamy, że ta funkcja jest zaimplementowana i modyfikuje listę x inplace
    W = calculate_vector_of_W_params(len(x))  # zwraca listę wartości W

    N = 2
    while N <= len(x):
        for i in range(len(x) // N):  # dzielenie całkowite
            for m in range(N // 2):
                offset = i * N
                tmp = x[offset + m + N // 2] * retrieve_W_from_vector(N, -m, W)
                x[offset + m + N // 2] = x[offset + m] - tmp
                x[offset + m] = x[offset + m] + tmp
        N *= 2

    return x

def dct_transform(x):
    N = len(x)
    X = [0.0] * N

    for m in range(N):
        sum_val = 0.0
        for n in range(N):
            sum_val += x[n] * math.cos(math.pi * (2 * n + 1) * m / (2 * N))
        X[m] = c(m, N) * sum_val

    return X

def c(m, N):
    if m == 0:
        return math.sqrt(1.0 / N)
    else:
        return math.sqrt(2.0 / N)


# --- Tryby wykresów ---
def plot_W1(X, fs):
    N = len(X)
    freqs = np.arange(N) * fs / N
    real = [z.real for z in X]
    imag = [z.imag for z in X]

    plt.figure(figsize=(10, 6))
    plt.subplot(2, 1, 1)
    plt.stem(freqs, real)
    plt.title("W1 – Część rzeczywista amplitudy 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("Re")
    plt.grid(True)

    plt.subplot(2, 1, 2)
    plt.stem(freqs, imag)
    plt.title("W1 – Część urojona amplitudy 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("Im")
    plt.grid(True)

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
    plt.stem(freqs, magnitudes)
    plt.title("W2 – Moduł (amplituda) 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("|X(f)|")
    plt.grid(True)

    plt.subplot(2, 1, 2)
    plt.stem(freqs, phases)
    plt.title("W2 – Argument (faza) 0–10 Hz")
    plt.xlabel("Częstotliwość [Hz]")
    plt.ylabel("∠X(f) [rad]")
    plt.grid(True)

    plt.tight_layout()
    plt.show()

def plt3 (X, fs):
    N = len(X)
    freqs = np.arange(N) * fs / N

    # Transformata DCT
    plt.stem(freqs, X, basefmt=" ")
    plt.title("DCT sygnału")
    plt.xlabel("m")
    plt.ylabel("X[m]")
    plt.grid(True)

    plt.tight_layout()
    plt.show()


# --- Przykładowy sygnał ---
fs = 16  # Hz, częstotliwość próbkowania
t = np.arange(0, 1, 1/fs)
f0 = 50  # Hz
time = np.arange(0, 0 + 4, 1 / fs)
signal = sinusoidal(1, 1, time)
x = [complex(s, 0) for s in signal]

# --- Obliczenie DFT ---
X = transformFFT(x)

tmp = dct_transform(signal)
plt3(tmp, fs)


# --- Wybór trybu prezentacji ---
mode = "W22"  # W1 lub W2

# if mode == "W1":
#     plot_W1(X, fs)
# elif mode == "W2":
#     plot_W2(X, fs)
# else:
#     plot_W1(X, fs)
#     plot_W2(X, fs)
