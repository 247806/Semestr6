from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.applications import ResNet50, MobileNetV2, InceptionV3
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Dense, GlobalAveragePooling2D, Input
from sklearn.metrics import confusion_matrix, classification_report, roc_curve, auc
from sklearn.metrics import precision_score, recall_score, f1_score, accuracy_score
import matplotlib.pyplot as plt
import seaborn as sns
import time

# Ustawienia
img_size = (128, 128)
batch_size = 32
dataset_path = 'dataset'
split_ratio = 0.6  # użytkownik może zmieniać ten procent
roc_data = []

# Przygotowanie danych
datagen = ImageDataGenerator(rescale=1./255, validation_split=1 - split_ratio)

train_data = datagen.flow_from_directory(
    dataset_path,
    target_size=img_size,
    batch_size=batch_size,
    class_mode='binary',
    subset='training',
    shuffle=True
)

val_data = datagen.flow_from_directory(
    dataset_path,
    target_size=img_size,
    batch_size=batch_size,
    class_mode='binary',
    subset='validation',
    shuffle=False
)

def build_model(base_model):
    base_model.trainable = False
    inputs = Input(shape=(img_size[0], img_size[1], 3))
    x = base_model(inputs, training=False)
    x = GlobalAveragePooling2D()(x)
    x = Dense(64, activation='relu')(x)
    x = Dense(32, activation='relu')(x)
    outputs = Dense(1, activation='sigmoid')(x)
    return Model(inputs, outputs)

models_to_test = {
    "Inception": InceptionV3(weights='imagenet', include_top=False, input_shape=(img_size[0], img_size[1], 3)),
    "ResNet50": ResNet50(weights='imagenet', include_top=False, input_shape=(img_size[0], img_size[1], 3)),
    "MobileNetV2": MobileNetV2(weights='imagenet', include_top=False, input_shape=(img_size[0], img_size[1], 3))
}

# fig_roc, ax_roc = plt.subplots(figsize=(10, 8))

for name, base_model in models_to_test.items():
    print(f"\nTrenowanie modelu: {name}")
    model = build_model(base_model)
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

    # Czas trenowania
    start_train = time.time()
    model.fit(train_data, epochs=5, validation_data=val_data, verbose=1)
    training_time = time.time() - start_train

    # Czas predykcji
    val_data.reset()
    start_pred = time.time()
    predictions = model.predict(val_data)
    prediction_time = time.time() - start_pred

    y_pred = (predictions > 0.5).astype(int).flatten()
    y_true = val_data.classes

    # Wyniki
    acc = accuracy_score(y_true, y_pred)
    print(f"\nWyniki dla {name}:")
    print("Dokładność:", acc)
    print(f"Czas trenowania: {training_time:.2f} sekund")
    print(f"Czas predykcji: {prediction_time:.2f} sekund")

    # Raport klasyfikacji
    class_names = list(val_data.class_indices.keys())
    report = classification_report(y_true, y_pred, target_names=class_names, digits=4)
    print("Raport klasyfikacji:\n", report)

    # Macierz pomyłek – w osobnej figurze
    fig_cm, ax_cm = plt.subplots(figsize=(6, 5))
    cm = confusion_matrix(y_true, y_pred)
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', xticklabels=class_names, yticklabels=class_names)
    plt.title(f'Macierz pomyłek – {name}')
    plt.xlabel('Przewidywana klasa')
    plt.ylabel('Rzeczywista klasa')
    fig_cm.tight_layout()
    fig_cm.show()

    # ROC – dodawanie do wspólnego wykresu
    fpr, tpr, _ = roc_curve(y_true, predictions)
    roc_auc = auc(fpr, tpr)
    roc_data.append((name, fpr, tpr, roc_auc))

# Finalny wykres ROC
fig_roc, ax_roc = plt.subplots(figsize=(10, 8))
for name, fpr, tpr, roc_auc in roc_data:
    ax_roc.plot(fpr, tpr, label=f'{name} (AUC = {roc_auc:.2f})')
ax_roc.plot([0, 1], [0, 1], 'k--')
ax_roc.set_xlabel('False Positive Rate')
ax_roc.set_ylabel('True Positive Rate')
ax_roc.set_title('Krzywa ROC dla różnych modeli')
ax_roc.legend(loc='lower right')
ax_roc.grid(True)
plt.tight_layout()
plt.show()
