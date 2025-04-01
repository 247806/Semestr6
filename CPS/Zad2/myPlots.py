from matplotlib import pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg


def plot_signal(time, signal, signal_types, plot, histogram):
    plt.style.use('classic')
    for widget in plot.winfo_children():
        widget.destroy()

    for widget in histogram.winfo_children():
        widget.destroy()

    fig, ax = plt.subplots()
    if signal_types in ["Impuls jednostkowy", "Szum impulsowy"]:
        ax.scatter(time, signal)
    else:
        ax.plot(time, signal)
    ax.set_title("Wykres sygnału")
    ax.set_xlabel("Czas [s]")
    ax.set_ylabel("Amplituda")
    ax.grid()
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