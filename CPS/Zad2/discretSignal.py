import numpy as np

def delta_diraca(A, n1, ns, l, f):
    n = np.arange(n1, n1 + l, 1 / f)
    signal = np.zeros_like(n, dtype=float)

    signal[n == ns] = A

    return n, signal

def impuls_noise(A, t1, d, f, p):
    n = np.arange(t1, t1 + d, 1 / f)
    signal = np.zeros_like(n, dtype=float)

    for i in range(len(signal)):
        rand = np.random.uniform(0, 1)
        if rand <= p:
            signal[i] = A
    return n, signal

