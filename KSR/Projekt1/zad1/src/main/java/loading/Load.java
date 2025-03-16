package loading;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Load {

    private static final Pattern REUTERS_PATTERN = Pattern.compile("<REUTERS(.*?)>(.*?)</REUTERS>", Pattern.DOTALL);
    private static final Pattern BODY_PATTERN = Pattern.compile("<BODY>(.*?)</BODY>", Pattern.DOTALL);
    private static final Pattern PLACES_PATTERN = Pattern.compile("<PLACES>(.*?)</PLACES>", Pattern.DOTALL);
    private static final Pattern PLACE_PATTERN = Pattern.compile("<D>(.*?)</D>");
    private static final Pattern SPLIT_PATTERN = Pattern.compile("LEWISSPLIT=\"(TRAIN|TEST)\"");

    public static List<Article> loadReutersArticles(String directoryPath, String splitType) throws IOException {
        List<Article> articles = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.sgm")) {
            for (Path file : stream) {
                String content = Files.readString(file, StandardCharsets.ISO_8859_1);

                Matcher reutersMatcher = REUTERS_PATTERN.matcher(content);
                while (reutersMatcher.find()) {
                    String header = reutersMatcher.group(1);
                    String articleContent = reutersMatcher.group(2);

                    Matcher splitMatcher = SPLIT_PATTERN.matcher(header);
                    if (splitMatcher.find()) {
                        String articleSplit = splitMatcher.group(1);
                        if (!articleSplit.equalsIgnoreCase(splitType)) {
                            continue;
                        }
                    }

                    String body = extractTagContent(articleContent, BODY_PATTERN);
                    List<String> places = extractPlaces(articleContent);

                    if (body != null && !body.isEmpty() && places.size() == 1) {
                        articles.add(new Article(body, places.getFirst()));
                    }
                }
            }
        }
        return articles;
    }

    private static String extractTagContent(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

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

