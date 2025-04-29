import math

def mse(original, reconstructed):
    if original is None:
        print("original is None")
        return 0
    if reconstructed is None:
        print("reconstructed is None")
        return 0
    if len(reconstructed) != len(original):
        print("Lists must be of the same length.")
        return 0

    sum_error = 0.0
    for i in range(len(reconstructed)):
        sum_error += (reconstructed[i] - original[i]) ** 2

    return sum_error / len(reconstructed)

def snr(original, reconstructed):
    if len(reconstructed) != len(original):
        print("Lists must be of the same length.")
        return 0

    result_squared_sum = 0.0
    diff_squared_sum = 0.0
    for i in range(len(reconstructed)):
        result_squared_sum += reconstructed[i] ** 2
        diff_squared_sum += (reconstructed[i] - original[i]) ** 2

    return 10.0 * math.log10(result_squared_sum / diff_squared_sum)

def psnr(original, reconstructed):
    if len(reconstructed) != len(original):
        print("Lists must be of the same length.")
        return 0

    result_max = float('-inf')
    diff_squared_sum = 0.0
    for i in range(len(reconstructed)):
        if reconstructed[i] > result_max:
            result_max = reconstructed[i]
        diff_squared_sum += (reconstructed[i] - original[i]) ** 2

    return 10.0 * math.log10(result_max / (diff_squared_sum / len(reconstructed)))

def max_diff(original, reconstructed):
    if len(reconstructed) != len(original):
        print("Lists must be of the same length.")
        return 0

    max_diffs = float('-inf')
    for i in range(len(reconstructed)):
        diff = abs(reconstructed[i] - original[i])
        if diff > max_diffs:
            max_diffs = diff

    return max_diffs

def enob(original, reconstructed):
    return (snr(original, reconstructed) - 1.76) / 6.02