from matplotlib import pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

def plot_transform(time, signal, plot):
    plt.style.use('classic')
    for widget in plot.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()

    ax.plot(time, signal, 'o-')
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=plot)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=False, fill='both', padx=5, pady=5)


def plot_counting(time, signal, plot, title):
    plt.style.use('classic')
    for widget in plot.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots(figsize=(6, 2), dpi=100)
    ax.plot(time, signal,  'o-')
    ax.set_title(title)
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=plot)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=False, fill='both', padx=5, pady=5)

def plot_signal(time, signal, signal_types, plot, histogram):
    plt.style.use('classic')
    for widget in plot.winfo_children():
        widget.destroy()

    for widget in histogram.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    if signal_types in ["Impuls jednostkowy", "Szum impulsowy"]:
        ax.scatter(time, signal)
    elif signal_types == "test":
        ax.step(time, signal, where='post')
    else:
        ax.plot(time, signal,  'o-')
    ax.set_title("Wykres sygnału")
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=plot)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=True, fill='both', padx=5, pady=5)

def plot_signal_quant(time, signal, signal_types, plot, histogram, time_all, signal_all, signal_samp, time_samp):
    plt.style.use('classic')
    for widget in plot.winfo_children():
        widget.destroy()

    for widget in histogram.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    if signal_types in ["Impuls jednostkowy", "Szum impulsowy"]:
        ax.scatter(time, signal)
    elif signal_types == "test":
        # for i in range(len(time) - 1):
        #     if abs(signal[i + 1]) > abs(signal[i]):
        #         where = 'pre'  # Jeśli wartość rośnie -> 'pre'
        #     else:
        #         where = 'post'  # Jeśli wartość maleje -> 'post'

            # ax.step(time[i:i + 2], signal[i:i + 2], where=where, color='b')
        plt.step(time, signal, where='post')
        ax.plot(time_all, signal_all)
        ax.scatter(time_samp, signal_samp, color='red')
    else:
        ax.plot(time, signal)
    ax.set_title("Wykres sygnału")
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
    ax.set_ylim([min(signal) * 1.1, max(signal) * 1.1])
    canvas = FigureCanvasTkAgg(fig, master=plot)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=True, fill='both', padx=5, pady=5)

def plot_histogram(frame, signal, bins):
    plt.style.use('classic')
    for widget in frame.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    ax.hist(signal, bins=bins, alpha=0.7, color='blue', edgecolor='black')
    ax.set_xlabel("Amplituda")
    ax.set_title("Histogram")
    ax.set_ylabel("Częstotliwość")

    canvas = FigureCanvasTkAgg(fig, master=frame)
    canvas.draw()
    canvas.get_tk_widget().pack()

def plot_signal_samp(time, signal, plot):
    plt.style.use('classic')
    for widget in plot.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    ax.scatter(time, signal)
    ax.set_title("Wykres sygnału")
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
    canvas = FigureCanvasTkAgg(fig, master=plot)
    canvas.draw()
    canvas.get_tk_widget().pack(expand=True, fill='both', padx=5, pady=5)