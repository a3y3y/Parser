package org.perekladov.repository;

import org.perekladov.dto.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.perekladov.util.AbstractConnection.getConnection;


public class ProductRepository {

    private String schema;

    public ProductRepository(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }

    public boolean createTable() {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + schema + ".products " +
                "(art INT, name VARCHAR(250), url VARCHAR(250)); CREATE INDEX art_index ON " +
                schema + ".products(art)";
        try (PreparedStatement stmt = getConnection().prepareStatement(createTableStatement)) {
            if (stmt.execute()) {
                return true;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return false;
    }

    public boolean createSchema() {
        String createTableStatement = "CREATE SCHEMA IF NOT EXISTS " + schema;
        try (PreparedStatement stmt = getConnection().prepareStatement(createTableStatement)) {
            if (stmt.execute()) {
                return true;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return false;
    }

    public boolean save(Product product) {
        String saveStatement = "INSERT INTO " + schema + ".products (art, name, url) VALUES (?,?,?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(saveStatement)) {
            stmt.setInt(1, product.getArt());
            stmt.setString(2, product.getName());
            stmt.setString(
                    3, product.getUrl());
            if (stmt.execute()) {
                return true;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return false;
    }

    public Product findByArt(int art) {
        String findByArtStatement = "SELECT*FROM " + schema + ".products WHERE art=?";
        Product product = null;
        try (PreparedStatement stmt = getConnection().prepareStatement(findByArtStatement)) {
            stmt.setInt(1, art);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                product = new Product();
                product.setArt(art);
                product.setName(rs.getString("name"));
                product.setUrl(rs.getString("url"));
            }
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return product;
    }

    public Product update(int art, Product product) {
        String updateStatement = "UPDATE " + schema + ".products SET url=?, name=? WHERE art=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(updateStatement)) {
            stmt.setString(1, product.getUrl());
            stmt.setString(2, product.getName());
            stmt.setInt(3, art);
            stmt.execute();
        } catch (SQLException | NumberFormatException e) {
            e.getMessage();
        }
        return product;
    }

    public List<Product> findAll() {
        String findAllStatement = "SELECT*FROM " + schema + ".products";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(findAllStatement)) {
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                Product product = new Product();
                product.setArt(rs.getInt("art"));
                product.setName(rs.getString("name"));
                product.setUrl(rs.getString("url"));
                products.add(product);
            }
            rs.close();
            return products;
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }
}
