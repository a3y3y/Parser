package org.perekladov.site;

import org.perekladov.dto.Product;

public interface Parser {
    Product readByUrl(String url);
}
