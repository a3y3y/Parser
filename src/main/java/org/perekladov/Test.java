package org.perekladov;

import org.perekladov.dto.Product;
import org.perekladov.site.ParserOma;

public class Test {
    public static void main(String[] args) {
        ParserOma productParser = new ParserOma();
        Product product = productParser.readByUrl("https://www.oma.by/laminat-expert-choice-8075-32-klass-1285kh192kh8-mm-s-0-24672-2-258343-p");
        Product product1 = productParser.readByUrl("https://www.oma.by/laminat-classen-natural-prestige-dub-kolorado-26387-2-202024-p");
        System.out.println(product1);
        System.out.println(product);
    }
}
