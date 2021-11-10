package org.perekladov.site;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.perekladov.dto.Product;

import java.io.IOException;
import java.math.BigDecimal;

public class ParserKsk implements Parser{

    @Override
    public Product readByUrl(String url) {
        Document doc = null;
        Product product = new Product();
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Chrome/91.0.4472.77")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException | IllegalArgumentException e ) {
            e.getMessage();
            return product;
        }
        if(doc.getElementsByClass("price-prod").first() == null){
            return product;
        }
        String prod = doc.getElementsByClass("price-prod").first().toString();
        Document prod1 = Jsoup.parse(prod);
        Element priceElementSale = prod1.getElementsByClass("price-cross").first();
        Element discountPriceElementSale = prod1.getElementsByClass("price-orange").first();
        Element priceElement = prod1.getElementsByClass("price-gray").first();
        Element discountPriceElement = prod1.getElementsByClass("price-black").first();
        Element pd = doc.getElementsByClass("col-sm-8 col-xl-8").first();
        Document doc1 = Jsoup.parse(pd.toString());
        Element productNameElement = doc1.select("h1").first();
        Element stock = doc.getElementsByClass("prod-stock").first();
        Document stock1 = Jsoup.parse(stock.toString());
        Element available = stock1.getElementsByClass("fa fa-check-circle-o green").first();
        Element unavailable = stock1.getElementsByClass("fa fa-times-circle-o red").first();
        Element order = stock1.getElementsByClass("fa fa-clock-o orange").first();
        if (available != null) {
            product.setAvailability("есть");
        } else if (unavailable != null) {
            product.setAvailability("нет");
        } else if (order != null) {
            product.setAvailability("заказ");
        }
        product.setName(productNameElement.text());
        if(priceElementSale == null) {
            if (priceElement != null) {
                product.setPriceKsk(new BigDecimal(priceElement.text()
                        .replaceAll("\\s+[/,\\D]+[.]?", "")
                        .trim()));
            }
        } else {
            product.setPriceKsk(new BigDecimal(priceElementSale.text()
                    .replaceAll("\\s+[/,\\D]+[.]?", "")
                    .trim()));
        }
        if(discountPriceElementSale == null) {
            if (discountPriceElement != null) {
                product.setDiscountPriceKsk(new BigDecimal(discountPriceElement.text()));
            }
        } else {
            product.setDiscountPriceKsk(new BigDecimal(discountPriceElementSale.text()
                    .replaceAll("\\s+[/,\\D]+[.]?", "")
                    .trim()));
        }
        return product;
    }
}
