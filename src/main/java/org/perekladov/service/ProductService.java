package org.perekladov.service;

import org.perekladov.dto.Product;
import org.perekladov.excel.Excel;
import org.perekladov.repository.ProductRepository;
import org.perekladov.site.Parser;
import org.perekladov.site.ParserBild;
import org.perekladov.site.ParserKsk;
import org.perekladov.site.ParserOma;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    Excel excel = new Excel();
    ProductRepository productRepository;
    Parser parser;

    public ProductService(Parser parser) {
        if (parser instanceof ParserOma) {
            this.productRepository = new ProductRepository("oma");
        }
        if (parser instanceof ParserKsk) {
            this.productRepository = new ProductRepository("ksk");
        }
        if (parser instanceof ParserBild) {
            this.productRepository = new ProductRepository("bild-shop");
        }
        productRepository.createSchema();
        productRepository.createTable();
        this.parser = parser;
    }

    public void updateXlsxWithDatabaseAndSite(File file) {
        List<Product> productsFromXlsx = excel.readXlsxFile(file);
        List<Product> updatedProducts = new ArrayList<>();
        int listSize = productsFromXlsx.size();
        int x = listSize / 30;
        double i = 1;
        int r = 1;
        for (Product product : productsFromXlsx) {
            Product databaseProduct = productRepository.findByArt(product.getArt());
            compareProductWithDatabaseAndAddToList(product, databaseProduct, updatedProducts);
            double d = i / x;
            if (isInteger(d) && (1 <= d || d <= 10)) {
                if (r <= 30) {
                    System.out.print("=");
                    r++;
                }
            }
            i++;
        }

        excel.writeListToXlsx(updatedProducts, file);
    }

    public File[] getFilesList(String filesPath) {
        File f = new File(filesPath);
        File[] matchingFiles = f.listFiles((f1, name) -> name.endsWith(".xlsx"));
        if (matchingFiles == null || matchingFiles.length == 0) {
            return null;
        } else {
            return matchingFiles;
        }
    }

    private void compareProductWithDatabaseAndAddToList(Product product, Product databaseProduct,
                                                        List<Product> updatedProducts) {
        String productUrl = product.getUrl();
        if (databaseProduct == null) {
            if (productUrl != null && !productUrl.matches("\\d+|^$") && isUrlCorrect(product, productRepository)) {
                productRepository.save(product);
                addCompetitorPricesToProduct(product);
            }
        } else {
            if (productUrl == null || productUrl.matches("\\d+|^$")) {
                product.setUrl(databaseProduct.getUrl());
            } else {
                if (!productUrl.equals(databaseProduct.getUrl())) {
                    databaseProduct.setUrl(productUrl);
                    databaseProduct.setName(product.getName());
                    productRepository.update(databaseProduct.getArt(), databaseProduct);
                }
            }
            addCompetitorPricesToProduct(product);
        }
        updatedProducts.add(product);
    }

    private void addCompetitorPricesToProduct(Product product) {
        Product productKsk = parser.readByUrl(product.getUrl());
        product.setPriceCompetitor(productKsk.getPriceCompetitor());
        product.setDiscountPriceCompetitor(productKsk.getDiscountPriceCompetitor());
        product.setAvailability(productKsk.getAvailability());
    }

    private boolean isInteger(double d) {
        return d % 1 == 0;
    }

    private boolean isUrlCorrect(Product product, ProductRepository productRepository) {
        return product.getUrl() != null && product.getUrl().contains(productRepository.getSchema() + ".by");
    }

}
