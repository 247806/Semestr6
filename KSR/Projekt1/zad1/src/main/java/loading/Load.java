package loading;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Load {

    // Wzorce dla artykułów, tytułów, treści i miejsc
    private static final Pattern REUTERS_PATTERN = Pattern.compile("<REUTERS.*?>(.*?)</REUTERS>", Pattern.DOTALL);
    private static final Pattern TITLE_PATTERN = Pattern.compile("<TITLE>(.*?)</TITLE>", Pattern.DOTALL);
    private static final Pattern BODY_PATTERN = Pattern.compile("<BODY>(.*?)</BODY>", Pattern.DOTALL);
    private static final Pattern PLACES_PATTERN = Pattern.compile("<PLACES>(.*?)</PLACES>", Pattern.DOTALL);
    private static final Pattern PLACE_PATTERN = Pattern.compile("<D>(.*?)</D>");

    public static List<Article> loadReutersArticles(String directoryPath) throws IOException {
        List<Article> articles = new ArrayList<>();
        Set<String> allowedPlaces = Set.of("canada", "japan", "uk", "usa", "france", "west-germany");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.sgm")) {
            for (Path file : stream) {
                String content = Files.readString(file, StandardCharsets.ISO_8859_1);

                // Dopasowanie poszczególnych artykułów
                Matcher reutersMatcher = REUTERS_PATTERN.matcher(content);
                while (reutersMatcher.find()) {
                    String articleContent = reutersMatcher.group(1);

                    String body = extractTagContent(articleContent, BODY_PATTERN);
                    List<String> places = extractPlaces(articleContent);

                    if (body != null && !body.isEmpty() && places.size() == 1 && allowedPlaces.contains(places.getFirst())) {
                        articles.add(new Article(body, places.getFirst()));                    }
                }
            }
        }
        return articles;
    }

    // Metoda pomocnicza do ekstrakcji pojedynczych tagów (TITLE, BODY)
    private static String extractTagContent(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    // Metoda do ekstrakcji wszystkich miejsc <D> z <PLACES>
    private static List<String> extractPlaces(String text) {
        List<String> places = new ArrayList<>();
        Matcher placesMatcher = PLACES_PATTERN.matcher(text);
        if (placesMatcher.find()) {
            String placesContent = placesMatcher.group(1);
            Matcher placeMatcher = PLACE_PATTERN.matcher(placesContent);
            while (placeMatcher.find()) {
                places.add(placeMatcher.group(1).trim());
            }
        }
        return places;
    }
}