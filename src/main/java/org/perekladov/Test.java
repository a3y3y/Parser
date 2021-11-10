package org.perekladov;

import org.perekladov.dto.Product;
import org.perekladov.repository.ProductRepository;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepository("ksk");
        ProductRepository productRepository1 = new ProductRepository("oma");
        List<Product> ksk = productRepository.findAll();
        List<Product> oma = productRepository1.findAll();
        System.out.println(ksk);
        System.out.println(oma);
    }
}
