import numpy as np


def clippQuant(signal, num_levels):
    # Znajdź maksymalną i minimalną wartość sygnału
    signal_min = np.min(signal)
    signal_max = np.max(signal)

    # Przekształć sygnał do zakresu od 0 do 1
    signal_normalized = (signal - signal_min) / (signal_max - signal_min)

    # Kwantyzacja do określonej liczby poziomów
    step = 1.0 / (num_levels - 1)  # Oblicz wielkość kroku kwantyzacji
    quantized_signal = np.floor(signal_normalized / step) * step

    # Przekształć z powrotem do oryginalnego zakresu
    quantized_signal = quantized_signal * (signal_max - signal_min) + signal_min

    print("Kwantyzowany sygnał:")
    print(quantized_signal)
    return quantized_signal

def roundQuant(signal, num_levels):

    signal_min = np.min(signal)
    signal_max = np.max(signal)
    # Normalizacja sygnału do [0, 1]
    signal_norm = (signal - signal_min) / (signal_max - signal_min)
    # Kwantyzacja do zadanej liczby poziomów
    quantized = np.round(signal_norm * (num_levels - 1)) / (num_levels - 1)
    # Denormalizacja z powrotem
    quantized_signal = quantized * (signal_max - signal_min) + signal_min

    print("Kwantyzowany sygnał:")
    print(quantized_signal)
    return quantized_signal
