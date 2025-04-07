package utils;

import java.util.List;

public class ArticleData {
    private String place;
    private List<Object> features;

    public ArticleData() {} // wymagany do deserializacji

    public ArticleData(String place, List<Object> features) {
        this.place = place;
        this.features = features;
    }

    public String getPlace() {
        return place;
    }

    public List<Object> getFeatures() {
        return features;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setFeatures(List<Object> features) {
        this.features = features;
    }
}
