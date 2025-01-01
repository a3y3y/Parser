package org.perekladov.site;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.perekladov.dto.Product;

import java.io.IOException;
import java.math.BigDecimal;

public class ParserBild implements Parser{

    @Override
    public Product readByUrl(String url) {
        Document doc;
        Product product = new Product();
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Chrome/91.0.4472.77")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException | IllegalArgumentException e ) {
            return product;
        }
        Element currentPriceEl = doc.getElementsByClass("current-price").first();
        Element regularPrice = doc.getElementsByClass("regular-price").first();
        Element availability = doc.getElementsByClass("stock").first();
        if (currentPriceEl != null) {
            String currentPrice = currentPriceEl.text().replaceAll("\\s.*", "");
            if (regularPrice != null) {
                product.setPriceCompetitor(new BigDecimal(regularPrice.getElementsByClass("line-through").first().text()));
                product.setDiscountPriceCompetitor(new BigDecimal(currentPrice));
            } else {
                product.setPriceCompetitor(new BigDecimal(currentPrice));
            }
        }
        if (availability != null) product.setAvailability(availability.getElementsByTag("span").first().text());

        return product;
    }
}
