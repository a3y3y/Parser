package org.perekladov.service;

import org.perekladov.dto.Product;
import org.perekladov.rep.IProductExcel;
import org.perekladov.rep.IProductParser;
import org.perekladov.rep.excel.Excel;


import java.util.ArrayList;
import java.util.List;

public class ProductService {

    IProductExcel excel = new Excel();

    public void refreshPricesFromSite(String targetFile, IProductParser parser) {
        List<Product> oldList = excel.readAll(targetFile, 7, 6);
        List<Product> newList = new ArrayList<>();
        for (Product product : oldList) {
            if (product.getUrl() != null && product.getUrl().startsWith("htt")) {
                Product product1 = parser.readByUrl(product.getUrl());
                product.setPrice(product1.getPrice());
                product.setDiscountPrice(product1.getDiscountPrice());
                product.setAvailability(product1.getAvailability());
                newList.add(product);
            }
        }
        excel.updateAll(targetFile, newList, 7, 6);
    }

    public void refreshPricesFromXlsx(String source, String target) {
        List<Product> oldList = excel.readAll(source, 4, 3);
        List<Product> newList = excel.readAll(target, 5, 4);
        for (Product product : newList) {
            for (Product product1 : oldList) {
                if(product.getArt() == product1.getArt()){
                    product.setPrice(product1.getPrice());
                    product.setDiscountPrice(product1.getDiscountPrice());
                }
            }
        }
        excel.updateAll(target,newList,5,4);
    }

    public void createXlsxFileFromSiteByCategory(IProductParser parser, String categoryUrl){
        List<Product> products = parser.readAllByCategory(categoryUrl);
        excel.createXlsxTable(products);
    }
}
