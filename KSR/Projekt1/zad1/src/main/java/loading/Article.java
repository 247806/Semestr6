package loading;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Article {
    private String body;
    private String place;
    private List<Object> features;
    private String predictedPlace = null;

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

