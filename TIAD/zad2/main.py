import speech_recognition as sr
import tkinter as tk
from tkinter import scrolledtext, messagebox
import threading
# from recipe_filter import filter_recipes_by_ingredients  # Załóżmy, że mamy taką funkcję


class RecipeApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Głosowa wyszukiwarka przepisów")
        self.root.geometry("600x400")

        # Interfejs graficzny
        self.label = tk.Label(root, text="🎤 Podaj składniki głosowo:", font=("Arial", 14))
        self.label.pack(pady=10)

        self.record_button = tk.Button(root, text="Nagrywaj", command=self.start_recording, font=("Arial", 12),
                                       bg="#4CAF50", fg="white")
        self.record_button.pack(pady=5)

        self.status_label = tk.Label(root, text="Status: Gotowy", font=("Arial", 10))
        self.status_label.pack(pady=5)

        self.ingredients_label = tk.Label(root, text="Wykryte składniki:", font=("Arial", 12))
        self.ingredients_label.pack(pady=5)

        self.ingredients_text = scrolledtext.ScrolledText(root, width=70, height=4, font=("Arial", 10))
        self.ingredients_text.pack(pady=5)

        self.recipes_label = tk.Label(root, text="Pasujące przepisy:", font=("Arial", 12))
        self.recipes_label.pack(pady=5)

        self.recipes_text = scrolledtext.ScrolledText(root, width=70, height=10, font=("Arial", 10))
        self.recipes_text.pack(pady=5)

        # Przykładowa baza przepisów
        self.recipes_db = [
            {"name": "Jajecznica", "ingredients": ["jajka", "sól", "pieprz", "masło"]},
            {"name": "Sałatka grecka", "ingredients": ["pomidory", "ogórek", "ser feta", "oliwki", "cebula"]},
            {"name": "Kanapka z serem", "ingredients": ["chleb", "ser", "masło"]},
            {"name": "Makaron z pesto", "ingredients": ["makaron", "pesto", "parmezan", "oliwa"]},
        ]

    def start_recording(self):
        self.record_button.config(text="Nagrywanie", state=tk.DISABLED, font=("Arial", 12),
                                       bg="#f7162d", fg="white")
        self.status_label.config(text="Status: Nagrywanie... mów teraz")
        threading.Thread(target=self.record_and_process).start()

    def record_and_process(self):
        recognizer = sr.Recognizer()
        microphone = sr.Microphone()

        try:
            with microphone as source:
                recognizer.adjust_for_ambient_noise(source)
                audio = recognizer.listen(source, timeout=5)

            self.status_label.config(text="Status: Przetwarzanie...")

            # Zamiana mowy na tekst
            try:
                text = recognizer.recognize_google(audio, language="pl-PL")
                self.ingredients_text.delete(1.0, tk.END)

                # Przetwarzanie tekstu na składniki
                ingredients = [word.lower() for word in text.split() if len(word) > 2]  # Prosta tokenizacja
                print(ingredients)
                unique_ingredients = list(set(ingredients))
                print(unique_ingredients)
                for ingredient in unique_ingredients:
                    self.ingredients_text.insert(tk.END, f"{ingredient} ")
                # self.ingredients_text.insert(tk.END, str(unique_ingredients))

                # Filtrowanie przepisów
                # matching_recipes = filter_recipes_by_ingredients(self.recipes_db, ingredients)

                # Wyświetlanie wyników
                # self.recipes_text.delete(1.0, tk.END)
                # if matching_recipes:
                #     for recipe in matching_recipes:
                #         self.recipes_text.insert(tk.END, f"{recipe['name']}: {', '.join(recipe['ingredients'])}\n\n")
                # else:
                #     self.recipes_text.insert(tk.END, "Nie znaleziono przepisów pasujących do podanych składników.")

                self.status_label.config(text="Status: Gotowy")
                self.record_button.config(text="Nagrywaj", font=("Arial", 12), bg="#4CAF50", fg="white")

            except sr.UnknownValueError:
                messagebox.showerror("Błąd", "Nie rozpoznano mowy. Spróbuj ponownie.")
                self.status_label.config(text="Status: Gotowy")
            except sr.RequestError:
                messagebox.showerror("Błąd", "Problem z połączeniem do usługi rozpoznawania mowy.")
                self.status_label.config(text="Status: Gotowy")

        except Exception as e:
            messagebox.showerror("Błąd", f"Wystąpił błąd: {str(e)}")
            self.status_label.config(text="Status: Gotowy")

        self.record_button.config(state=tk.NORMAL)


# def filter_recipes_by_ingredients(recipes, ingredients):
#     """Filtruje przepisy zawierające wszystkie podane składniki"""
#     matching_recipes = []
#     for recipe in recipes:
#         if all(ingredient in recipe['ingredients'] for ingredient in ingredients):
#             matching_recipes.append(recipe)
#     return matching_recipes


if __name__ == "__main__":
    root = tk.Tk()
    app = RecipeApp(root)
    root.mainloop()