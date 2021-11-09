package org.perekladov.dto;

import java.math.BigDecimal;

public class Product {
    private String url;
    private String name;
    private BigDecimal discountPrice;
    private BigDecimal price;
    private BigDecimal discountPriceKsk;
    private BigDecimal priceKsk;
    private String availability = "";
    private int art;
    private int rowNumberXlsx;

    public Product() {
    }

    public Product(String url, String name, BigDecimal discountPrice, BigDecimal price, String availability) {
        this.url = url;
        this.name = name;
        this.discountPrice = discountPrice;
        this.price = price;
        this.availability = availability;
    }

    public BigDecimal getDiscountPriceKsk() {
        return discountPriceKsk;
    }

    public void setDiscountPriceKsk(BigDecimal discountPriceKsk) {
        this.discountPriceKsk = discountPriceKsk;
    }

    public BigDecimal getPriceKsk() {
        return priceKsk;
    }

    public void setPriceKsk(BigDecimal priceKsk) {
        this.priceKsk = priceKsk;
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
                ", discountPriceKsk=" + discountPriceKsk +
                ", priceKsk=" + priceKsk +
                ", availability='" + availability + '\'' +
                ", art=" + art +
                ", rowNumberXlsx=" + rowNumberXlsx +
                '}';
    }
}
