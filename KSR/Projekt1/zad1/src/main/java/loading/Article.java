package loading;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public class Article {
    String body;
    String place;
    Map<String, Object> features;

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

