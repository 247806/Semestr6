import numpy as np

def hamming_window(n, M):
    return 0.53836 - 0.46164 * np.cos(2 * np.pi * n / M)

def rectangular_window(n, M):
    return 1.0

def hann_window(n, M):
    return 0.5 - 0.5 * np.cos(2 * np.pi * n / M)

def black_window(n, M):
    return 0.42 - 0.5 * np.cos(2.0 * np.pi * n / M) + 0.08 * np.cos(4.0 * np.pi * n / M);

def low_pass_filter(M, K, window_func=rectangular_window):
    c = (M - 1) // 2
    h = np.zeros(M)

    for n in range(M):
        if n == c:
            sinc = 2.0 / K
        else:
            sinc = np.sin(2.0 * np.pi * (n - c) / K) / (np.pi * (n - c))
        h[n] = sinc * window_func(n, M)

    return h

def band_pass_filter(M, K, window_func=rectangular_window):
    h_lp = low_pass_filter(M, K, window_func)
    h_bp = np.zeros(M)

    for n in range(M):
        h_bp[n] = h_lp[n] * 2.0 * np.sin(np.pi * n / 2.0)

    return h_bp

def high_pass_filter(M, K, window_func=rectangular_window):
    h_lp = low_pass_filter(M, K, window_func)
    h_hp = np.zeros(M)

    for n in range(M):
        sign = -1.0 if (n & 0x01) == 1 else 1.0
        h_hp[n] = h_lp[n] * sign

    return h_hp