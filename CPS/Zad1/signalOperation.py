import numpy as np

def operate_signals(operation, signal_1, time_1, signal_2, time_2):

    if signal_1 is None or signal_2 is None:
        print("Brak dwóch sygnałów do wykonania operacji.")
        return

    signal_3 = np.copy(signal_1)
    time_3 = np.copy(time_1)

    min_time = max(time_1[0], time_2[0])
    max_time = min(time_1[-1], time_2[-1])

    if min_time >= max_time:
        print("Brak wspólnego przedziału czasowego.")
        return

    common_idx_1 = np.where((time_1 >= min_time) & (time_1 <= max_time))
    common_idx_2 = np.where((time_2 >= min_time) & (time_2 <= max_time))

    common_time_1 = time_1[common_idx_1]
    common_time_2 = time_2[common_idx_2]

    if len(common_time_1) > len(common_time_2):
        signal_2_aligned = np.interp(common_time_1, common_time_2, signal_2[common_idx_2])
        signal_1_interpolated = signal_1[common_idx_1]
    else:
        signal_1_interpolated = np.interp(common_time_2, common_time_1, signal_1[common_idx_1])
        signal_2_aligned = signal_2[common_idx_2]

    if operation == "add":
        result_signal = signal_1_interpolated + signal_2_aligned
    elif operation == "subtract":
        result_signal = signal_1_interpolated - signal_2_aligned
    elif operation == "multiply":
        result_signal = signal_1_interpolated * signal_2_aligned
    elif operation == "divide":
        with np.errstate(divide='ignore', invalid='ignore'):
            result_signal = np.where(signal_2_aligned != 0, signal_1_interpolated / signal_2_aligned, 0)
    else:
        print("Nieznana operacja.")
        return

    mask = (time_3 >= min_time) & (time_3 <= max_time)
    if result_signal.shape[0] != mask.sum():
        result_signal = result_signal[:mask.sum()]

    signal_3[mask] = result_signal

    return signal_3, time_3

