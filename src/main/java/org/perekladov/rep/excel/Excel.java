package org.perekladov.rep.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.perekladov.dto.Product;
import org.perekladov.rep.IProductExcel;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Excel implements IProductExcel {

    @Override
    public void createXlsxTable(List<Product> productList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet");
        String[] columns = {"URL", "Наименование", "Дисконт",
                "Цена", "Доступность"};
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight((short) 700);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        int rowNum = 1;
        for (Product product : productList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getUrl());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getDiscountPrice().doubleValue());
            row.createCell(3).setCellValue(product.getPrice().doubleValue());
            row.createCell(4).setCellValue(product.getAvailability());
        }
        try {
            FileOutputStream fileOut = new FileOutputStream("target.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> readAll(String fileName, int priceCell, int discountCell) {
        List<Product> productList = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int rowNumber = 0; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
                Row row = sheet.getRow(rowNumber);
                if (row != null && row.getCell(1) != null &&
                        row.getCell(1).getStringCellValue().matches("[1-9]{1}[0-9]{3,}")) {
                    Product product = new Product();
                    if (row.getCell(0) != null) {
                        product.setUrl(row.getCell(0).getStringCellValue());
                    }

                    product.setArt(Integer.parseInt(row.getCell(1).getStringCellValue()));
                    if (row.getCell(priceCell) != null) {
                        product.setPrice(BigDecimal.valueOf((row.getCell(priceCell).getNumericCellValue())));
                    }
                    if (row.getCell(discountCell) != null) {
                        product.setDiscountPrice(BigDecimal.valueOf((row.getCell(discountCell).getNumericCellValue())));
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

    @Override
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
