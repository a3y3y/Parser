package org.perekladov.rep;

import org.perekladov.dto.Product;

import java.util.List;

public interface IProductParser {
    public Product readByUrl(String url);
    public List<Product> readAllByCategory(String categoryName);
}
