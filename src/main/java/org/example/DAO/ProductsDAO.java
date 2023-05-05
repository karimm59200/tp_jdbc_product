package org.example.DAO;

import org.example.models.Products;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {

    protected Connection _connection;
    protected PreparedStatement statement;
    protected String request;
    protected ResultSet resultSet;


    public ProductsDAO(Connection connection) {
        this._connection = connection;
    }

    public boolean save(Products products) throws SQLException {
        request = "INSERT INTO products (name, price, quantity, description) values (?,?,?,?)";
        statement = _connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, products.getName());
        statement.setFloat(2, products.getPrice());
        statement.setInt(3, products.getQuantity());
        statement.setString(4, products.getDescription());
        int nbRow = statement.executeUpdate();
        resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            products.setIdProduct(resultSet.getInt(1));
        }
        return nbRow == 1;
    }

    public Products getByIdProduct(int id) throws SQLException {
        Products products = null;
        request = "SELECT * FROM products where id = ?";
        statement = _connection.prepareStatement(request);
        statement.setInt(1, products.getIdProduct());
        if (resultSet.next()) {
            products = new Products(resultSet.getInt("idProduct"),
                    resultSet.getString("name"),
                    resultSet.getFloat("price"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("description"));
        }
        return products;
    }

    public List<Products> getAllProductsAction() throws SQLException {
        List<Products> result = new ArrayList<>();
        request = "select * from products";
        statement = _connection.prepareStatement(request);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Products p = new Products(resultSet.getInt("idProduct"), resultSet.getString("name"), resultSet.getFloat("price"), resultSet.getInt("quantity"), resultSet.getString("description"));
            result.add(p);
        }
        return result;

    }

    public Products getById(int idProduct) throws SQLException {
        Products products = null;
        request = "SELECT * FROM products where idProduct = ?";
        statement = _connection.prepareStatement(request);
        statement.setInt(1, idProduct);
        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            products = new Products(resultSet.getInt("idProduct"),
                    resultSet.getString("name"),
                    resultSet.getFloat("price"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("description"));
        }
        return products;
    }

    public boolean delete(int idProduct) throws SQLException {
        request = "DELETE FROM products WHERE idProduct = ?";
        try {
            statement = _connection.prepareStatement(request);
            statement.setInt(1, idProduct);
            int nbRows = statement.executeUpdate();
            if (nbRows == 1) {
                return true;
            } else {
                System.out.println("Le produit  n'existe pas");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Products products) throws SQLException {
        request = "UPDATE products SET name = ?, price = ?, quantity = ? , description = ?  WHERE idProduct = ?"   ;
        statement = _connection.prepareStatement(request);
        statement.setString(1, products.getName());
        statement.setFloat(2, products.getPrice());
        statement.setInt(3, products.getQuantity());
        statement.setString(4, products.getDescription());
        statement.setInt(5, products.getIdProduct());
        int nbRows = statement.executeUpdate();
        if (nbRows == 1) {
            System.out.println("Le produit a bien été modifié");
        } else {
            System.out.println("Le produit n'existe pas");
        }
        return false;
    }
}

