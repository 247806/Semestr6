import numpy as np

def delta_diraca(A, n1, ns, l, f):
    n = np.arange(n1, n1 + l)
    signal = np.zeros_like(n, dtype=float)

    signal[n == ns] = A

    return n, signal