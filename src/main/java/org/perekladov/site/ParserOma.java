package org.perekladov.site;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.perekladov.dto.Product;

import java.io.IOException;
import java.math.BigDecimal;

public class ParserOma implements Parser{

    private final String cityId;

    public ParserOma(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public Product readByUrl(String url) {
        Document doc = null;
        Product product = new Product();
        try {
            doc = Jsoup.connect(url + "?CITY_ID=14" + cityId)
                    .userAgent("Chrome/91.0.4472.77")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException | IllegalArgumentException e) {
            e.getMessage();
            return product;
        }
        Elements elements = doc.getElementsByClass("price-and-avail");
        if (elements.size() == 0) {
            return product;
        } else {
            String prod = elements.first().toString();
            Document prod1 = Jsoup.parse(prod);
            Elements el = prod1.getElementsByClass("product-info-box_price strong-price 1");

            String priceElement = null;
            if (el.size() > 0) {
                priceElement = el.first().text().replaceAll("/.+", "")
                        .trim().replaceAll(",", ".");
                product.setPriceKsk(new BigDecimal(priceElement));
            } else {
                Elements el1 = prod1.getElementsByClass("product-info-box_price strong-price red");
                if (el1.size() > 0) {
                    priceElement = el1.first().text().replaceAll("/.+", "")
                            .trim().replaceAll(",", ".");
                    product.setDiscountPriceKsk(new BigDecimal(priceElement));
                }
            }
            return product;
        }
    }
}
