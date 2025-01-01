package org.perekladov.view;

import org.perekladov.service.ProductService;
import org.perekladov.site.Parser;
import org.perekladov.site.ParserBild;
import org.perekladov.site.ParserKsk;
import org.perekladov.site.ParserOma;

import java.io.File;
import java.util.Scanner;

public class ConsoleView {
    public Parser getParser() {
        while (true) {
            System.out.println("Что будем мониторить?");
            System.out.println("1. KSK");
            System.out.println("2. OMA");
            System.out.println("3. Билд");
            Parser parser = null;
            Scanner sc = new Scanner(System.in);
            String value = sc.nextLine();
            if ("1".equals(value)) {
                System.out.println("Будем мониторить КСК");
                return new ParserKsk();
            }
            if ("2".equals(value)) {
                System.out.println("Будем мониторить ОМА");
                return new ParserOma("14471");
            }
            if ("3".equals(value)) {
                System.out.println("Будем мониторить Билд");
                return new ParserBild();
            } else {
                System.out.println("Введи 1, 2 или 3 что непонятно?");
            }
        }
    }

    public void runParser(Parser parser) {
        ProductService productService = new ProductService(parser);
        File[] matchingFiles = productService.getFilesList("../../файлы");
        if (matchingFiles == null) {
            System.out.println("В папке файлы нечего обрабатывать.");
        } else {
            for (File matchingFile : matchingFiles) {
                System.out.println("\nОбрабатываю файл " + matchingFile.getName());
                productService.updateXlsxWithDatabaseAndSite(matchingFile);
            }
            System.out.println("\nВсе вроде готово, че ждешь?");
            System.out.println("\nИли продолжим?");
        }
    }
}
