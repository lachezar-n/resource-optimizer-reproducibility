from matplotlib import pyplot as plt


times_s=[
    [0, 111.5, 320, 94.944, 155.014, 50.189], 
    [179, 125.728, 294, 104.824, 150.197, 46.08], 
    [0, 169.898, 304, 125.738, 238.549, 60.831],
    [659, 337.72, 898, 253.381, 235.588, 225.986],
]

times_m = [
    [0, 198.549, 480, 125.896, 222.003, 77.681],
    [0, 237.279, 526, 235.675, 257.204, 72.431],
    [0, 0, 990, 0, 2313.819, 334.755],
    [844, 440.621, 1122, 619.466, 561.399, 254.24]
]

times_l = [
    [0, 481.528, 532, 302.623, 526.812, 114.226],
    [0, 510.975, 566, 339.073, 528.045, 107.971],
    [0, 0, 1026.062, 576.373, 4415.096, 458.37],
    [938, 874.119, 1188, 328.189, 1149.728, 458.37]
]


prices_s = [
    [0, 0.144, 0.207, 0.182, 0.107, 0.938],
    [0.084, 0.149, 0.198, 0.187, 0.107, 0.925],
    [0, 0.165, 0.201, 0.262, 0.120, 0.944],
    [0.168, 0.223, 0.4, 0.477, 0.242, 0.712]
]


prices_m = [
    [0, 0.349, 0.520, 0.312, 0.275, 2.192],
    [0, 0.376, 0.550, 0.375, 0.296, 2.140],
    [0, 0, 0.860, 0, 0.742, 2.947],
    [0.4, 0.518, 0.948, 0.800, 0.402, 1.200]
]

prices_l = [
    [0, 0.712, 0.924, 0.583, 0.562, 3.062],
    [0, 0.739, 0.962, 0.622, 0.567, 3.024],
    [0, 0, 1.473, 2.232, 1.415, 5.616],
    [0.867, 1.07, 1.653, 1.909, 0.764, 2.729]
]

enum_time_l = [
    [302.623, 467.133, 302.623],
    [339.073, 555.85, 339.073],
    [576.373, 576.826, 623.414],
    [328.189, 1340.364, 1208.326]
]

enum_price_l = [
    [0.583, 0.650, 0.583],
    [0.622, 0.732, 0.622],
    [2.232, 3.105, 2.015],
    [1.909, 1.800, 1.956]
]

def plot_with_baseline():
    diagrams = ["L2SVM", "Linreg", "PCA", "PNMF"]

    categories = ["baseline1", "baseline1*","baseline2", "costs", "price", "time"]
    # Colors: first 3 bars in shades of grey, last 3 in colorful hues
    colors = ['#B0B0B0', '#808080', '#505050', '#FF5733', '#33FF57', '#3357FF']

    # Create a 2x2 plot layout with a single legend
    fig, axs = plt.subplots(2, 2, figsize=(12, 8))

    # Flatten the axes array for easier iteration
    axs = axs.flatten()

    to_use = times_l

    for i, ax in enumerate(axs):
        bars = ax.bar(categories, to_use[i], color=colors)
        ax.set_title(diagrams[i])
        ax.set_ylabel('Execution Time [s]')

        ax.axhline(y=4000, color='orange', linestyle='--', label=f"Max. price for objective func. 'time'")
        #ax.legend(loc='upper left', bbox_to_anchor=(0.01, 0.85))

        for bar, value in zip(bars, to_use[i]):
            if value == 0:
                # Add "Failed" text for each bar with zero value
                ax.text(
                    bar.get_x() + bar.get_width() / 2,
                    0.1,  # Position slightly above the x-axis
                    "Failed",
                    ha='center', va='bottom', fontsize=10, color='red'
                )
    
    fig.legend(["Max. time for objective func. 'price'"], loc='upper center')   

    # Adjust layout to make room for the legend
    plt.tight_layout(rect=[0.0, 0.0, 1, 0.97])
    plt.show()

def plot_without_baseline():
    diagrams = ["L2SVM", "Linreg", "PCA", "PNMF"]

    categories = ["grid", "interest", "prune"]
    # Colors: first 3 bars in shades of grey, last 3 in colorful hues
    colors = ['blue', 'orange', 'green']

    # Create a 2x2 plot layout with a single legend
    fig, axs = plt.subplots(1, 4, figsize=(12, 3))

    # Flatten the axes array for easier iteration
    axs = axs.flatten()

    to_use = enum_time_l

    for i, ax in enumerate(axs):
        bars = ax.bar(categories, to_use[i], color=colors)
        ax.set_title(diagrams[i])
        ax.set_ylabel('Execution Time [s]')

    # Adjust layout to make room for the legend
    plt.tight_layout(rect=[0, 0.0, 1, 1])
    plt.show()

plot_without_baseline()