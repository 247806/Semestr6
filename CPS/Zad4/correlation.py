import numpy as np

from convolve import convolve


def cross_convolution(x, h):
    if x is None or h is None:
        raise ValueError("Przynajmniej jeden z sygnałów (x lub h) jest None!")
    h_flipped = h[::-1]  # odbicie h względem czasu
    return convolve(x, h_flipped)

def cross_correlation_direct(x, h):
    N = len(x)
    M = len(h)
    L = N + M - 1  # długość wynikowego sygnału
    result = []

    for n in range(-N + 1, M):  # pełny zakres przesunięć (od -N+1 do M-1)
        sum_val = 0.0
        for k in range(N):
            h_index = n + k
            if 0 <= h_index < M:
                sum_val += x[k] * h[h_index]
        result.append(sum_val)

    return np.array(result)