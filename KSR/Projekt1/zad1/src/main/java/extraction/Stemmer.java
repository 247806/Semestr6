package extraction;

import opennlp.tools.stemmer.PorterStemmer;
import java.io.IOException;
import java.io.StringReader;

public class Stemmer {
    PorterStemmer stemmer = new PorterStemmer();

    public String stem(String text) {
        StringBuilder stemmedText = new StringBuilder();
        for (String word : text.split("\\s+")) {
            stemmedText.append(stemmer.stem(word)).append(" ");
        }
        return stemmedText.toString();
    }
}
