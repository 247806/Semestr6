package loading;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Article {
    String body;
    String place;

    public Article(String body, String place) {
        this.body = body;
        this.place = place;
    }



    @Override
    public String toString() {
        return "\nPlace: " + place +
                "\nBody: " + body;
    }
}

