package loading;

import java.util.List;
import java.util.ArrayList;

public class Article {
    String title;
    String body;
    String place;

    public Article(String title, String body, String place) {
        this.title = title;
        this.body = body;
        this.place = place;
    }

    @Override
    public String toString() {
        return "Title: " + title +
                "\nPlace: " + place +
                "\nBody: " + body;
    }
}

