package org.perekladov.service;

import org.perekladov.dto.Product;
import org.perekladov.excel.Excel;
import org.perekladov.repository.ProductRepository;
import org.perekladov.site.ParserKsk;
import org.perekladov.site.ParserOma;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    Excel excel = new Excel();
    ProductRepository productRepository = new ProductRepository();
    ParserOma parserKsk = new ParserOma();

    public void updateXlsxWithDatabaseAndSite(File file) {
        List<Product> productsFromXlsx = excel.readXlsxFile(file);
        List<Product> updatedProducts = new ArrayList<>();
        for (Product product : productsFromXlsx) {
            Product databaseProduct = productRepository.findByArt(product.getArt());
            compareProductWithDatabaseAndAddToList(product, databaseProduct, updatedProducts);
        }
        excel.writeListToXlsx(updatedProducts, file);
    }


    private void compareProductWithDatabaseAndAddToList(Product product, Product databaseProduct,
                                                        List<Product> updatedProducts) {
        String productUrl = product.getUrl();
        if (databaseProduct == null) {
            if (productUrl != null && !"".equals(productUrl)) {
                productRepository.save(product);
                addKskPricesToProduct(product);
            }
        } else {
            if (productUrl == null || "".equals(productUrl)) {
                product.setUrl(databaseProduct.getUrl());
            } else {
                if (!productUrl.equals(databaseProduct.getUrl())) {
                    databaseProduct.setUrl(productUrl);
                    databaseProduct.setName(product.getName());
                    productRepository.update(databaseProduct.getArt(), databaseProduct);
                }
            }
            addKskPricesToProduct(product);
        }
        updatedProducts.add(product);
    }

    private void addKskPricesToProduct(Product product) {
        Product productKsk = parserKsk.readByUrl(product.getUrl());
        product.setPriceKsk(productKsk.getPriceKsk());
        product.setDiscountPriceKsk(productKsk.getDiscountPriceKsk());
        product.setAvailability(productKsk.getAvailability());
    }

}
