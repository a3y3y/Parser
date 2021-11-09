package org.perekladov.repository;

import org.perekladov.dto.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.perekladov.util.AbstractConnection.getConnection;

public class ProductRepository {

    private final String createTableStatement = "CREATE TABLE IF NOT EXISTS products " +
            "(art INT, name VARCHAR(250), url VARCHAR(250))";
    private final String saveStatement = "INSERT INTO products (art, name, url) VALUES (?,?,?)";
    private final String findByArtStatement = "SELECT*FROM products WHERE art=?";
    private final String updateStatement = "UPDATE products SET url=?, name=? WHERE art=?";

    public boolean createTable() {
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
}
