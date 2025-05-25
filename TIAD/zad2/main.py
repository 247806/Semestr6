import psycopg2
import speech_recognition as sr
import tkinter as tk
from tkinter import scrolledtext, messagebox
import threading
from langdetect import detect
from deep_translator import GoogleTranslator

class RecipeSearcher:
    def __init__(self, db_config):
        self.db_config = db_config
        self.conn = None

    def connect(self):
        """Nawiązuje połączenie z bazą danych"""
        try:
            self.conn = psycopg2.connect(**self.db_config)
            return True
        except Exception as e:
            messagebox.showerror("Błąd bazy danych", f"Nie można połączyć się z bazą: {e}")
            return False

    def search_recipes(self, ingredients):
        """
        Wyszukuje przepisy zawierające CO NAJMNIEJ JEDEN podany składnik
        :param ingredients: lista składników np. ['jajka', 'mąka']
        :return: lista słowników z przepisami lub None w przypadku błędu
        """
        if not self.conn:
            if not self.connect():
                return None

        try:
            with self.conn.cursor() as cursor:
                query = """
                        SELECT r.name, \
                               r.instructions,
                               STRING_AGG(i.name, ', ' ORDER BY i.name)               as ingredients,
                               COUNT(DISTINCT CASE WHEN i.name IN %s THEN i.name END) as matched_ingredients
                        FROM recipes r
                                 JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id
                                 JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
                        WHERE EXISTS (SELECT 1 \
                                      FROM recipe_ingredients ri2 \
                                               JOIN ingredients i2 ON ri2.ingredient_id = i2.ingredient_id \
                                      WHERE ri2.recipe_id = r.recipe_id \
                                        AND i2.name IN %s)
                        GROUP BY r.name, r.instructions
                        ORDER BY matched_ingredients DESC, r.name \
                        """
                cursor.execute(query, (tuple(ingredients), tuple(ingredients)))
                return cursor.fetchall()

        except Exception as e:
            messagebox.showerror("Błąd wyszukiwania", f"Błąd podczas wyszukiwania przepisów: {e}")
            return None


class RecipeApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Głosowa wyszukiwarka przepisów")
        self.root.geometry("800x600")

        # Inicjalizacja wyszukiwarki przepisów
        self.searcher = RecipeSearcher({
            'dbname': 'recipes',
            'user': 'postgres',
            'password': '1234',
            'host': 'localhost'
        })

        self.original_ingredients = []
        self.translated_ingredients = []
        self.translated_ingredients_text = []
        self.polish_ingredients = []
        self.selected_language = 'auto'
        self.translation_language = 'pl'
        self.detected_lang = 'pl'
        self.result_translation = 'pl'

        self.create_ui()

    def create_ui(self):
        """Tworzy interfejs użytkownika"""
        # Nagłówek
        header = tk.Frame(self.root, bg="#4CAF50", height=80)
        header.pack(fill=tk.X)
        tk.Label(header, text="🍳 Głosowa Wyszukiwarka Przepisów",
                 font=("Arial", 18, "bold"), bg="#4CAF50", fg="white").pack(pady=20)

        # Główny kontener
        main_frame = tk.Frame(self.root)
        main_frame.pack(pady=20, padx=20, fill=tk.BOTH, expand=True)

        # Panel nagrywania
        record_frame = tk.Frame(main_frame)
        record_frame.pack(fill=tk.X)

        self.record_button = tk.Button(record_frame, text="🎤 Nagraj składniki", command=self.start_recording, font=("Arial", 12),
                  bg="#4CAF50", fg="white", height=2, width=20)
        self.record_button.pack(side=tk.LEFT)

        self.status_label = tk.Label(
            record_frame, text="Status: Gotowy do nagrywania",
            font=("Arial", 10), fg="#666"
        )
        self.status_label.pack(side=tk.LEFT, padx=20)

        # Wybór języka
        self.lang_selection_label = tk.Label(
            record_frame, text="Język rozpoznawania:",
            font=("Arial", 10), fg="#666"
        )
        self.lang_selection_label.pack(side=tk.LEFT, padx=10)

        self.lang_selection = tk.StringVar(value='auto')
        self.lang_menu = tk.OptionMenu(
            record_frame,
            self.lang_selection,
            'polski', 'angielski', 'niemiecki', 'hiszpański', 'francuski', 'włoski',
            command=self.language_changed
        )
        self.lang_menu.pack(side=tk.LEFT)

        self.lang_label = tk.Label(
            record_frame, text="Wykryty język: brak",
            font=("Arial", 10), fg="#666"
        )
        self.lang_label.pack(side=tk.RIGHT)

        # Składniki
        tk.Label(main_frame, text="Wykryte składniki:",
                 font=("Arial", 12)).pack(anchor=tk.W)

        self.ingredients_text = scrolledtext.ScrolledText(
            main_frame, width=80, height=4,
            font=("Arial", 10), wrap=tk.WORD
        )
        self.ingredients_text.pack(fill=tk.X, pady=(0, 5))

        translate_frame = tk.Frame(main_frame)
        translate_frame.pack(anchor=tk.E, pady=5)

        # Przycisk do wyświetlenia tłumaczenia składników
        self.translate_button = tk.Button(
            translate_frame,
            text="⚙ Wyświetl tłumaczenie składników",
            command=self.toggle_translation_view,
            font=("Arial", 10),
            bg="#f0f0f0",
            fg="black"
        )
        self.translate_button.pack(side=tk.LEFT, padx=(0, 10))

        # Menu wyboru języka do tłumaczenia
        self.translation_language = tk.StringVar(value='polski')
        self.translate_language_menu = tk.OptionMenu(
            translate_frame,
            self.translation_language,
            'polski', 'angielski', 'niemiecki', 'hiszpański', 'francuski', 'włoski',
            command=self.translate_language
        )
        self.translate_language_menu.config(font=("Arial", 10))
        self.translate_language_menu.pack(side=tk.LEFT)

        # Wyniki wyszukiwania
        tk.Label(main_frame, text="Pasujące przepisy:",
                 font=("Arial", 12)).pack(anchor=tk.W)

        self.recipes_text = scrolledtext.ScrolledText(
            main_frame, width=80, height=15,
            font=("Arial", 10), wrap=tk.WORD
        )
        self.recipes_text.pack(fill=tk.BOTH, expand=True)

        # Kontener na przycisk tłumaczenia i wybór języka – poniżej wyników
        translate_frame2 = tk.Frame(main_frame)
        translate_frame2.pack(anchor=tk.E, pady=5)

        # Przycisk do wyświetlenia tłumaczenia składników
        self.translate_button2 = tk.Button(
            translate_frame2,
            text="⚙ Wyświetl tłumaczenie wyników",
            command=self.search_and_display_recipes,
            font=("Arial", 10),
            bg="#f0f0f0",
            fg="black"
        )
        self.translate_button2.pack(side=tk.LEFT, padx=(0, 10))

        # Menu wyboru języka do tłumaczenia wyników
        self.translation_language2 = tk.StringVar(value='polski')  # Domyślnie język polski
        self.translate_language_menu2 = tk.OptionMenu(
            translate_frame2,
            self.translation_language2,  # Zmienna dla wyboru języka
            'polski', 'angielski', 'niemiecki', 'hiszpański', 'francuski', 'włoski',
            command=self.result_language
        )
        self.translate_language_menu2.config(font=("Arial", 10))  # Wygląd menu
        self.translate_language_menu2.pack(side=tk.LEFT)

        # Stopka
        tk.Label(self.root, text="© 2025 Wyszukiwarka Przepisów",
                 fg="#999", font=("Arial", 8)).pack(side=tk.BOTTOM, pady=10)

    def language_changed(self, lang):
        match lang:
            case 'auto':
                 self.selected_language = 'auto'
            case 'polski':
                self.selected_language = 'pl'
            case 'angielski':
                self.selected_language = 'en'
            case 'niemiecki':
                self.selected_language = 'de'
            case 'hiszpański':
                self.selected_language = 'es'
            case 'francuski':
                self.selected_language = 'fr'
            case 'włoski':
                self.selected_language = 'it'

        print(f"Wybrano język: {self.selected_language}")

    def result_language(self, lang):
        match lang:
            case 'polski':
                self.result_translation = 'pl'
            case 'angielski':
                self.result_translation = 'en'
            case 'niemiecki':
                self.result_translation = 'de'
            case 'hiszpański':
                self.result_translation = 'es'
            case 'francuski':
                self.result_translation = 'fr'
            case 'włoski':
                self.result_translation = 'it'

    def translate_language(self, lang):
        match lang:
            case 'polski':
                self.translation_language = 'pl'
            case 'angielski':
                self.translation_language = 'en'
            case 'niemiecki':
                self.translation_language = 'de'
            case 'hiszpański':
                self.translation_language = 'es'
            case 'francuski':
                self.translation_language = 'fr'
            case 'włoski':
                self.translation_language = 'it'

    def toggle_translation_view(self):
        if self.translate_button.config('text')[-1] == "⚙ Wyświetl tłumaczenie składników":
            for i in range(len(self.original_ingredients)):
                self.translated_ingredients[i] = GoogleTranslator(source=self.detected_lang, target=self.translation_language).translate(
                    self.original_ingredients[i])
            # Pokazanie przetłumaczonych składników
            self.ingredients_text.delete(1.0, tk.END)
            self.ingredients_text.insert(tk.END, ", ".join(self.translated_ingredients))
            self.translate_button.config(text="⚙ Wyświetl składniki oryginalne")
        else:
            # Pokazanie oryginalnych składników
            self.ingredients_text.delete(1.0, tk.END)
            self.ingredients_text.insert(tk.END, ", ".join(self.original_ingredients))
            self.translate_button.config(text="⚙ Wyświetl tłumaczenie składników")

    def start_recording(self):
        """Rozpoczyna proces nagrywania w osobnym wątku"""
        self.record_button.config(text="Nagrywanie", state="disabled", font=("Arial", 12),
                                  bg="#f7162d", fg="white")
        self.status_label.config(text="Status: Nagrywanie... mów teraz")
        threading.Thread(target=self.record_and_process).start()

    def record_and_process(self):
        """Przetwarza mowę na tekst i wyszukuje przepisy"""
        recognizer = sr.Recognizer()

        try:
            with sr.Microphone() as source:
                recognizer.adjust_for_ambient_noise(source)
                audio = recognizer.listen(source, timeout=5)

            self.status_label.config(text="Status: Przetwarzanie mowy...")
            if self.selected_language == 'auto':
                text = recognizer.recognize_google(audio)
                self.detected_lang = detect(text)
            else:
                text = recognizer.recognize_google(audio, language=self.selected_language)
                self.detected_lang = self.selected_language

            self.lang_label.config(text=f"Wykryty język: {self.detected_lang.upper()}")
            print(f"Wykryto język: {self.detected_lang}")

            # Przetwarzanie tekstu na listę składników
            self.original_ingredients = list(set(word.lower() for word in text.split() if len(word) > 2))
            self.translated_ingredients = self.original_ingredients.copy()
            self.translated_ingredients_text = self.original_ingredients.copy()
            self.polish_ingredients = self.original_ingredients.copy()

            # Tłumaczenie składników, jeśli język jest inny niż polski
            if self.detected_lang != 'pl' and self.detected_lang != 'auto':
                for i in range(len(self.original_ingredients)):
                    self.polish_ingredients[i] = GoogleTranslator(source=self.detected_lang, target='pl').translate(
                        self.original_ingredients[i])

            # Wyświetlenie składników (domyślnie oryginalnych)
            self.ingredients_text.delete(1.0, tk.END)
            self.ingredients_text.insert(tk.END, ", ".join(self.original_ingredients))

            # Wyszukiwanie przepisów na podstawie przetłumaczonych składników
            self.search_and_display_recipes()

        except sr.UnknownValueError:
            messagebox.showwarning("Błąd", "Nie rozpoznano mowy. Spróbuj ponownie.")
        except sr.RequestError:
            messagebox.showerror("Błąd", "Brak połączenia z usługą rozpoznawania mowy.")
        except Exception as e:
            messagebox.showerror("Błąd", f"Niespodziewany błąd: {str(e)}")
        finally:
            self.record_button.config(text="🎤 Nagraj składniki", state="normal", command=self.start_recording, font=("Arial", 12),
                                      bg="#4CAF50", fg="white")
            self.status_label.config(text="Status: Gotowy do nagrywania")

    def show_ingredients(self, ingredients):
        """Wyświetla wykryte składniki w interfejsie"""
        self.ingredients_text.delete(1.0, tk.END)
        self.ingredients_text.insert(tk.END, ", ".join(ingredients))

    def search_and_display_recipes(self):
        """Wyszukuje i wyświetla przepisy"""
        self.recipes_text.delete(1.0, tk.END)
        self.status_label.config(text="Status: Wyszukiwanie przepisów...")

        recipes = self.searcher.search_recipes(self.polish_ingredients)
        print(recipes)
        if not recipes:
            self.recipes_text.insert(tk.END, "Nie znaleziono przepisów zawierających wszystkie podane składniki.")
            return

        if self.result_translation != 'pl':
            self.recipes_text.delete(1.0, tk.END)

        for recipe in recipes:
            name = recipe[0]
            # instructions = recipe[1]
            all_ingredients = recipe[2]
            matched_count = recipe[3]

            if self.result_translation == 'pl':
                self.display_result(name, all_ingredients, matched_count, self.polish_ingredients, "Pasujące składniki", "Wszystkie składniki")
            else:
                header1 = GoogleTranslator(source=self.detected_lang, target=self.result_translation).translate("Pasujące składniki")
                header2 = GoogleTranslator(source=self.detected_lang, target=self.result_translation).translate(
                    "Wszystkie składniki")
                translated_name = GoogleTranslator(source=self.detected_lang, target=self.result_translation).translate(name)
                translated_ingredients = GoogleTranslator(source=self.detected_lang, target=self.result_translation).translate(
                    all_ingredients)
                self.display_result(translated_name, translated_ingredients, matched_count, self.polish_ingredients, header1, header2)

        self.status_label.config(text=f"Status: Znaleziono {len(recipes)} przepisów")

    def display_result(self, name, all_ingredients, matched_count, ingredients, header1, header2):
        self.recipes_text.insert(tk.END, f"🍲 {name}\n", 'title')
        self.recipes_text.insert(
            tk.END,
            f"✅ {header1}: {matched_count}/{len(all_ingredients.split(", "))}\n"
        )
        self.recipes_text.insert(
            tk.END,
            f"📋 {header2}: {all_ingredients}\n"
        )
        self.recipes_text.insert(tk.END, "-" * 80 + "\n", 'divider')

if __name__ == "__main__":
    root = tk.Tk()
    app = RecipeApp(root)
    root.mainloop()