import numpy as np


def convolve(x, h):
    n = len(x)
    m = len(h)
    y = []

    for i in range(n + m - 1):
        sum_ = 0.0
        start_k = max(0, i - n + 1)
        end_k = min(m, i + 1)
        for k in range(start_k, end_k):
            sum_ += h[k] * x[i - k]
        y.append(sum_)

    return y

def convolve_time_axis(time_samp_1, time_samp_2):
    start_time = min(time_samp_1[0], time_samp_2[0])
    end_time = start_time + abs(time_samp_1[-1] - time_samp_1[0]) + abs(time_samp_2[-1] - time_samp_2[0])
    n = len(time_samp_1)
    m = len(time_samp_2)
    num_points = n + m - 1

    print("time")
    print(start_time)
    print(end_time)
    print(num_points)

    time_conv = np.linspace(start_time, end_time, num_points)
    print(time_conv)

    return time_conv