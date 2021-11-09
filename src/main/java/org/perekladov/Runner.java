package org.perekladov;

import org.perekladov.repository.ProductRepository;
import org.perekladov.service.ProductService;

import java.io.File;
import java.io.FilenameFilter;


public class Runner {

    public static void main(String[] args) {
        File f = new File("../../файлы");
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File f, String name) {
                return name.endsWith(".xlsx");
            }
        });
        if (!(matchingFiles == null || matchingFiles.length == 0)) {
            for (int i = 0; i < matchingFiles.length; i++) {
                System.out.println("Читаю файл " + matchingFiles[i].getName());
                ProductRepository productRepository = new ProductRepository();
                productRepository.createTable();
                ProductService productService = new ProductService();
                System.out.println("Записываю в файл " + matchingFiles[i].getName());
                productService.updateXlsxWithDatabaseAndSite(matchingFiles[i]);
            }
            System.out.println("Все вроде готово, че ждешь?");
        } else {
            System.out.println("В папке файлы нечего обрабатывать.");
        }
        while (true) {

        }
    }
}
