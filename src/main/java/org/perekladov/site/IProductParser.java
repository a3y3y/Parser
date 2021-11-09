package org.perekladov.site;

import org.perekladov.dto.Product;

import java.util.List;

public interface IProductParser {
    public Product readByUrl(String url);
}
