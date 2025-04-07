package ksr.loading;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

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
        return new ToStringBuilder(this)
                .append("body", body)
                .append("place", place)
                .append("features", features)
                .append("predictedPlace", predictedPlace)
                .toString();
    }
}

