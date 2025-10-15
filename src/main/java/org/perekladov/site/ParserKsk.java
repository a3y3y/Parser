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
        if(doc.getElementsByClass("price-prod").first() == null){
            return product;
        }
        String prod = doc.getElementsByClass("price-prod").first().toString();
        Document prod1 = Jsoup.parse(prod);
        Element priceElementSale = prod1.getElementsByClass("price-cross").first();
        Element discountPriceElementSale = prod1.getElementsByClass("price-orange").first();
        Element priceElement = prod1.getElementsByClass("price-gray").first();
        Element discountPriceElement = prod1.getElementsByClass("price-black").first();
        Element pd = doc.getElementsByClass("name-h1").first();
        Document doc1 = Jsoup.parse(pd.toString());
        Element productNameElement = doc1.select("h1").first();
        Element cartButton = doc.getElementsByClass("flex button-prod button-cart").first();
        String availability = "нет";
        if (cartButton != null) {
            Element span = cartButton.getElementsByTag("span").first();
            availability = "В корзину".equals(span.text()) ? "есть" : "заказ";
        }
        product.setAvailability(availability);
        product.setName(productNameElement.text());
        if(priceElementSale == null) {
            if (priceElement != null) {
                product.setPriceCompetitor(new BigDecimal(priceElement.text()
                        .replaceAll("\\s+[/,\\D]+[.]?", "")
                        .trim()));
            }
        } else {
            product.setPriceCompetitor(new BigDecimal(priceElementSale.text()
                    .replaceAll("\\s+[/,\\D]+[.]?", "")
                    .trim()));
        }
        if(discountPriceElementSale == null) {
            if (discountPriceElement != null) {
                product.setDiscountPriceCompetitor(new BigDecimal(discountPriceElement.text()));
            }
        } else {
            product.setDiscountPriceCompetitor(new BigDecimal(discountPriceElementSale.text()
                    .replaceAll("\\s+[/,\\D]+[.]?", "")
                    .trim()));
        }
        return product;
    }
}
