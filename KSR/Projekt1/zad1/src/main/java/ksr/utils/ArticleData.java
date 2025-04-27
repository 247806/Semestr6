package ksr.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ArticleData {
    private String place;
    private List<Object> features;

    public ArticleData() {}

    public ArticleData(String place, List<Object> features) {
        this.place = place;
        this.features = features;
    }

}
