package org.perekladov.dto;

import java.math.BigDecimal;

public class Product {
    private String url;
    private String name;
    private BigDecimal discountPrice;
    private BigDecimal price;
    private BigDecimal discountPriceCompetitor;
    private BigDecimal priceCompetitor;
    private String availability = "";
    private int art;
    private int rowNumberXlsx;

    public BigDecimal getDiscountPriceCompetitor() {
        return discountPriceCompetitor;
    }

    public void setDiscountPriceCompetitor(BigDecimal discountPriceCompetitor) {
        this.discountPriceCompetitor = discountPriceCompetitor;
    }

    public BigDecimal getPriceCompetitor() {
        return priceCompetitor;
    }

    public void setPriceCompetitor(BigDecimal priceCompetitor) {
        this.priceCompetitor = priceCompetitor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public int getRowNumberXlsx() {
        return rowNumberXlsx;
    }

    public void setRowNumberXlsx(int rowNumberXlsx) {
        this.rowNumberXlsx = rowNumberXlsx;
    }

    @Override
    public String toString() {
        return "Product{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", discountPrice=" + discountPrice +
                ", price=" + price +
                ", discountPriceCompetitor=" + discountPriceCompetitor +
                ", priceCompetitor=" + priceCompetitor +
                ", availability='" + availability + '\'' +
                ", art=" + art +
                ", rowNumberXlsx=" + rowNumberXlsx +
                '}';
    }
}
