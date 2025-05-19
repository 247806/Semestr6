import continousSignal
import discretSignal


def function_type(A, T, t1, d, kw, ts, p, signal, signal_type, time, sample_rate):
    if signal_type == "Sygnał sinusoidalny":
        signal = continousSignal.sinusoidal(A, T, time)
        #signal = continousSignal.testFunction(time)
    elif signal_type == "Sygnał prostokątny symetryczny":
        signal = continousSignal.squareSymetric(A, T, t1, kw, time)
    elif signal_type == 'Sygnał sinusoidalny wyprostowany jednopołówkowo':
        signal = continousSignal.halfWaveSinusoidal(A, T, time)
    elif signal_type == 'Sygnał sinusoidalny wyprostowany dwupołówkowo ':
        signal = continousSignal.halfSinusoidal(A, T, time)
    elif signal_type == 'Sygnał prostokątny':
        signal = continousSignal.square(A, T, t1, kw, time)
    elif signal_type == 'Sygnał trójkątny':
        signal = continousSignal.triangle(A, T, t1, kw, time)
    elif signal_type == 'Skok jednostkowy':
        signal = continousSignal.ones(A, ts, time)
    elif signal_type== 'Szum o rozkładzie jednostajnym':
        signal = continousSignal.random_uniform_signal(A, time)
    elif signal_type == 'Szum gaussowski':
        signal = continousSignal.gaussian_noise(A, time)
    elif signal_type == 'Impuls jednostkowy':
        time, signal = discretSignal.delta_diraca(A, t1, ts, d, sample_rate)
    elif signal_type == 'Szum impulsowy':
        time, signal = discretSignal.impuls_noise(A, t1, d, sample_rate, p)

    return time, signal