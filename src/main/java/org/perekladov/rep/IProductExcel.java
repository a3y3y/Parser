package org.perekladov.rep;

import org.perekladov.dto.Product;

import java.util.List;

public interface IProductExcel {
    public void createXlsxTable(List<Product> productList);
    public List<Product> readAll(String fileName, int priceCell, int discountCell);
    public void updateAll(String filename, List<Product> productList, int priceCell, int discountCell);
}
