import cmath
import math
import time

import numpy as np


def s1(t):
    component1 = 2 * np.sin(2 * np.pi * (1 / 2) * t + np.pi / 2)  # 1 Hz
    component2 = 5 * np.sin(2 * np.pi * (1 / 0.5) * t + np.pi / 2)  # 2 Hz

    # Sygnał złożony
    s = component1 + component2
    return s

def s2(t):
    component1 = 2 * np.sin(2 * np.pi * (1 / 2) * t)  # 0.5 Hz
    component2 = 1 * np.sin(2 * np.pi * 1 * t)  # 1 Hz
    component3 = 5 * np.sin(2 * np.pi * 2 * t)  # 2 Hz

    # Złożony sygnał
    s = component1 + component2 + component3
    return s

def s3(t):
    component1 = 5 * np.sin(2 * np.pi * (1 / 2) * t)  # 0.5 Hz
    component2 = 1 * np.sin(2 * np.pi * 4 * t)  # 4 Hz

    # Złożony sygnał
    s = component1 + component2
    return s

# --- DFT (IDFT jeśli podzielisz przez N) ---
def dft(x):
    start_timer = time.perf_counter()
    N = len(x)
    Warg = 2.0 * math.pi / N
    W = cmath.exp(1j * Warg)

    X = []

    for m in range(N):
        sum_ = 0j
        for n in range(N):
            sum_ += x[n] * (W ** (-m * n))
        X.append(sum_ / N)

    end_timer = time.perf_counter()
    timer = end_timer - start_timer
    return X, timer



def fft(x):
    start_timer = time.perf_counter()
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

    end_timer = time.perf_counter()
    timer = end_timer - start_timer

    return x, timer

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



def dct(x):
    start_timer = time.perf_counter()
    N = len(x)
    X = [0.0] * N

    for m in range(N):
        sum_val = 0.0
        for n in range(N):
            sum_val += x[n] * math.cos(math.pi * (2 * n + 1) * m / (2 * N))
        X[m] = c(m, N) * sum_val

    end_timer = time.perf_counter()
    timer = end_timer - start_timer

    return X, timer

def c(m, N):
    if m == 0:
        return math.sqrt(1.0 / N)
    else:
        return math.sqrt(2.0 / N)



def Walsh(x):
    start_timer = time.perf_counter()
    m = round(math.log2(len(x)))
    H = generate_hadamard_matrix(m)
    N = len(x)
    X = [0.0] * N
    for i in range(N):
        sum_val = 0.0
        for j in range(N):
            sum_val += x[j] * H[i * N + j]
        X[i] = sum_val
    end_timer = time.perf_counter()
    timer = end_timer - start_timer
    return X, timer

def generate_hadamard_matrix(m):
    size = 1
    factor = 1.0
    H = [1.0]
    for _ in range(1, m + 1):
        size *= 2
        previous = H
        H = [0.0] * (size * size)
        paste_matrix(previous, size // 2, H, size, 0, 0, factor)
        paste_matrix(previous, size // 2, H, size, 0, size // 2, factor)
        paste_matrix(previous, size // 2, H, size, size // 2, 0, factor)
        paste_matrix(previous, size // 2, H, size, size // 2, size // 2, -factor)
    return H

def paste_matrix(src, src_size, dst, dst_size, row, col, factor):
    for i in range(src_size):
        for j in range(src_size):
            dst[(i + row) * dst_size + (j + col)] = src[i * src_size + j] * factor



def FastWalsh(x):
    start_timer = time.perf_counter()
    mix(x, 0, len(x))
    end_timer = time.perf_counter()
    timer = end_timer - start_timer
    return x, timer

def mix(x, begin, end):
    N = end - begin
    if N == 1:
        return
    for i in range(N // 2):
        tmp = x[begin + i]
        x[begin + i] = tmp + x[begin + N // 2 + i]
        x[begin + N // 2 + i] = tmp - x[begin + N // 2 + i]
    mix(x, begin, begin + N // 2)
    mix(x, begin + N // 2, end)
