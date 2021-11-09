package org.perekladov.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.perekladov.dto.Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Excel {


    public List<Product> readXlsxFile(File file) {
        List<Product> productList = new ArrayList<>();
        int priceCellNumber;
        int discountCellNumber;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row headRow = sheet.getRow(3);
            Cell priceCell = headRow.getCell(3);

            if (!"Стройпрайс".equals(priceCell.getStringCellValue())){
                priceCellNumber = 5;
                discountCellNumber = 4;
            } else {
                priceCellNumber = 4;
                discountCellNumber = 3;
            }

            for (int rowNumber = 0; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
                Row row = sheet.getRow(rowNumber);
                if (row != null && row.getCell(1) != null &&
                        row.getCell(1).getStringCellValue().matches("[1-9]{1}[0-9]{3,}")) {
                    Product product = new Product();
                    if (row.getCell(0) != null) {
                        product.setUrl(row.getCell(0).getStringCellValue());
                    }
                    try {
                        product.setArt(Integer.parseInt(row.getCell(1).getStringCellValue()));
                    } catch (NumberFormatException e){
                        e.getMessage();
                    }
                    if (row.getCell(priceCellNumber) != null) {
                        product.setPrice(BigDecimal.valueOf((row.getCell(priceCellNumber).getNumericCellValue())));
                    }
                    if (row.getCell(discountCellNumber) != null) {
                        product.setDiscountPrice(BigDecimal.valueOf((row.getCell(discountCellNumber).getNumericCellValue())));
                    }
                    if (row.getCell(2) != null) {
                        product.setName(row.getCell(2).getStringCellValue());
                    }
                    product.setRowNumberXlsx(row.getRowNum());
                    productList.add(product);
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return productList;
    }

    public void writeListToXlsx(List<Product> productList, File file){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Product product : productList) {
                Row row = sheet.getRow(product.getRowNumberXlsx());
                if(!(product.getUrl() == null || "".equals(product.getUrl()))) {
                    if (row.getCell(0) == null) {
                        row.createCell(0).setCellValue(product.getUrl());
                    } else {
                        row.getCell(0).setCellValue(product.getUrl());
                    }
                    if (product.getDiscountPriceKsk() != null) {
                        if (row.getCell(6) == null) {
                            row.createCell(6).setCellValue(product.getDiscountPriceKsk().doubleValue());
                        } else {
                            row.getCell(6).setCellValue(product.getDiscountPriceKsk().doubleValue());
                        }
                    }
                    if (product.getPriceKsk() != null) {
                        if (row.getCell(7) == null) {
                            row.createCell(7).setCellValue(product.getPriceKsk().doubleValue());
                        } else {
                            row.getCell(7).setCellValue(product.getPriceKsk().doubleValue());
                        }
                    }
                    if(product.getAvailability() != null) {
                        row.createCell(8).setCellValue(product.getAvailability());
                    }
                }
            }
            inputStream.close();
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void updateAll(String fileName, List<Product> productList, int priceCell, int discountCell) {
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Product product : productList) {
                Row row = sheet.getRow(product.getRowNumberXlsx());
                if (row.getCell(discountCell) == null) {
                    row.createCell(discountCell).setCellValue(product.getDiscountPrice().doubleValue());
                } else {
                    row.getCell(discountCell).setCellValue(product.getDiscountPrice().doubleValue());
                }
                if (row.getCell(priceCell) == null) {
                    row.createCell(priceCell).setCellValue(product.getPrice().doubleValue());
                } else {
                    row.getCell(priceCell).setCellValue(product.getPrice().doubleValue());
                }
                row.createCell(8).setCellValue(product.getAvailability());
            }
            inputStream.close();
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
