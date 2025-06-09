import struct
from tkinter import filedialog


def save_signal(signal, time):

    if time is None or signal is None:
        print("Brak danych do zapisania")
        return

    if len(time) != len(signal):
        print("Długości time i signal muszą się zgadzać")
        return

    start_time = time[0]

    sampling_rate = 1 / (time[1] - time[0]) if len(time) > 1 else 1.0

    num_samples = len(signal)

    max_amplitude = max(abs(s) for s in signal)

    file_path = filedialog.asksaveasfilename(defaultextension=".bin",
                                             filetypes=[("Binary Files", "*.bin"), ("All Files", "*.*")])
    if not file_path:
        return

    with open(file_path, mode='wb') as file:
        file.write(struct.pack('d', start_time))
        file.write(struct.pack('d', sampling_rate))
        file.write(struct.pack('i', num_samples))
        file.write(struct.pack('d', max_amplitude))

        for t, s in zip(time, signal):
            file.write(struct.pack('d', t))
            file.write(struct.pack('d', s))

    print(f"Plik zapisany: {file_path}")

def load_signal():
    file_path = filedialog.askopenfilename(filetypes=[("Binary Files", "*.bin"), ("All Files", "*.*")])
    if not file_path:
        return 0

    time_temp, signal_temp = [], []

    with open(file_path, mode='rb') as file:
        start_time = struct.unpack('d', file.read(8))[0]
        sampling_rate = struct.unpack('d', file.read(8))[0]
        num_samples = struct.unpack('i', file.read(4))[0]
        max_amplitude = struct.unpack('d', file.read(8))[0]

        for _ in range(num_samples):
            t = struct.unpack('d', file.read(8))[0]  # czas
            s = struct.unpack('d', file.read(8))[0]  # amplituda
            time_temp.append(t)
            signal_temp.append(s)

    return time_temp, signal_temp, start_time, sampling_rate, max_amplitude, num_samples

def save_signal_complex(signal, time):
    if time is None or signal is None:
        print("Brak danych do zapisania")
        return

    if len(time) != len(signal):
        print("Długości time i signal muszą się zgadzać")
        return

    start_time = time[0]
    sampling_rate = 1 / (time[1] - time[0]) if len(time) > 1 else 1.0
    num_samples = len(signal)
    max_amplitude = max(abs(s) for s in signal)

    file_path = filedialog.asksaveasfilename(defaultextension=".bin",
                                             filetypes=[("Binary Files", "*.bin"), ("All Files", "*.*")])
    if not file_path:
        return

    with open(file_path, mode='wb') as file:
        # Nagłówek
        file.write(struct.pack('d', start_time))
        file.write(struct.pack('d', sampling_rate))
        file.write(struct.pack('i', num_samples))
        file.write(struct.pack('d', max_amplitude))

        # Dane: czas (double) + część rzeczywista (double) + część urojona (double)
        for t, s in zip(time, signal):
            file.write(struct.pack('d', t))
            file.write(struct.pack('d', s.real))
            file.write(struct.pack('d', s.imag))

    print(f"Plik zapisany: {file_path}")


def load_signal_complex():
    file_path = filedialog.askopenfilename(filetypes=[("Binary Files", "*.bin"), ("All Files", "*.*")])
    if not file_path:
        return None

    time_temp, signal_temp = [], []

    with open(file_path, mode='rb') as file:
        start_time = struct.unpack('d', file.read(8))[0]
        sampling_rate = struct.unpack('d', file.read(8))[0]
        num_samples = struct.unpack('i', file.read(4))[0]
        max_amplitude = struct.unpack('d', file.read(8))[0]

        for _ in range(num_samples):
            t = struct.unpack('d', file.read(8))[0]        # czas
            real = struct.unpack('d', file.read(8))[0]     # część rzeczywista
            imag = struct.unpack('d', file.read(8))[0]     # część urojona
            time_temp.append(t)
            signal_temp.append(complex(real, imag))

    return time_temp, signal_temp, start_time, sampling_rate, max_amplitude, num_samples