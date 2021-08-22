package org.perekladov.rep.site.ksk;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.perekladov.dto.Product;
import org.perekladov.rep.IProductParser;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParserKsk implements IProductParser {

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
            e.printStackTrace();
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
                product.setPrice(new BigDecimal(priceElement.text()
                        .replaceAll("\\s+[/,\\D]+[.]?", "")
                        .trim()));
            }
        } else {
            product.setPrice(new BigDecimal(priceElementSale.text()
                    .replaceAll("\\s+[/,\\D]+[.]?", "")
                    .trim()));
        }
        if(discountPriceElementSale == null) {
            if (discountPriceElement != null) {
                product.setDiscountPrice(new BigDecimal(discountPriceElement.text()));
            }
        } else {
            product.setDiscountPrice(new BigDecimal(discountPriceElementSale.text()
                    .replaceAll("\\s+[/,\\D]+[.]?", "")
                    .trim()));
        }
        product.setUrl(url);
        return product;
    }

    @Override
    public List<Product> readAllByCategory(String categoryUrl) {
        Document doc = null;
        List<Product> productList = new ArrayList<>();
        String pagination = "";
        int pageCount = 2;
        while (true) {
            try {
                doc = Jsoup.connect(categoryUrl + pagination)
                        .userAgent("Chrome/91.0.4472.77")
                        .referrer("http://www.google.com")
                        .get();
            }catch (HttpStatusException e){
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (doc == null) {
                break;
            } else {
                List<String> productLinks = doc.getElementsByClass("name")
                        .select("a").eachAttr("href");
                if(productLinks == null){
                    break;
                }
                List<String> productNames = doc.getElementsByClass("name").eachText();
                List<String> priceElements = doc.getElementsByClass("price").eachText();


                List<String> stock = doc.getElementsByClass("c-icon")
                        .select("i").eachAttr("class");

                for (int i = 0; i < productLinks.size(); i++) {
                    Product product = new Product();
                    product.setUrl(productLinks.get(i));
                    product.setName(productNames.get(i));
                    String[] str = priceElements.get(i).replaceAll("[/,а-я]+[.]?","")
                            .trim().split("\\s+");
                    product.setDiscountPrice(new BigDecimal(str[0]));
                    product.setPrice(new BigDecimal(str[1]));
                    String string = stock.get(i);
                    switch(string){
                        case "fa fa-check-circle-o green":
                            product.setAvailability("есть");
                            break;
                        case "fa fa-times-circle-o red":
                            product.setAvailability("нет");
                            break;
                        case "fa fa-clock-o orange":
                            product.setAvailability("заказ");
                            break;
                        default:
                            break;
                    }
                    productList.add(product);
                }
                doc = null;
                pagination = "?page=" + pageCount++;
            }
        }
        return productList;
    }
}
