package org.example;

import org.example.DAO.ProductsDAO;
import org.example.models.Products;
import org.example.utils.DataBaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IHM {

    Scanner scanner;
    String choix;

    private ProductsDAO productsDAO;

    private Connection connection;

    private static String request;


    private static PreparedStatement statement;


    public IHM() {
        scanner = new Scanner(System.in);
    }

    public void start() throws SQLException {

        do {
            menu();
            choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    createProductsAction();
                    break;
                case "2":
                    getAllProducts();
                    break;
                case "3":
                    getProductAction();
                    break;
                case "4":
                    updateProductAction();
                    break;
                case "5":
                    deleteProductAction();
                    break;
            }

        } while (!choix.equals("6"));

    }

    private void menu() {
        System.out.println("1. Enregistrer un produit.");
        System.out.println("2. Afficher la liste des produits.");
        System.out.println("3. Afficher un produit.");
        System.out.println("4. Mise à jour d'un produit.");
        System.out.println("5. Supprimer un produit.");
        System.out.println("6. Exit");
    }

    private Products createProductsAction() {
        Products products = null;
        System.out.println("Donnez moi toutes les informations concernant le produit.");
        System.out.println("Nom: ");
        String name = scanner.nextLine();
        System.out.println("prix");
        float price = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Quantité: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Description: ");
        String description = scanner.nextLine();
        products = new Products(name, price, quantity, description);

        try {
            connection = new DataBaseManager().getConnection();
            connection.setAutoCommit(false);
            productsDAO = new ProductsDAO(connection);
            if (productsDAO.save(products)) {
                System.out.println("Produit ajouté " + products.getIdProduct());
                connection.commit();
            } else {
                products = null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            products = null;
        }

        //System.out.println(products);
        return products;
    }

    private void getAllProducts() {
        try {
            connection = new DataBaseManager().getConnection();
            productsDAO = new ProductsDAO(connection);
            productsDAO.getAllProductsAction().forEach(e -> System.out.println(e));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private void getProductAction() {

        Products products = null;
        System.out.println("Merci de saisir l'id du produit recherché: ");
        int idProducts = scanner.nextInt();
        scanner.nextLine();

        try {
            connection = new DataBaseManager().getConnection();
            productsDAO = new ProductsDAO(connection);
            products = productsDAO.getById(idProducts);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (products != null) {
            System.out.println(products);
        }
    }

    private void updateProductAction() throws SQLException {
        Products products = null;
        System.out.println("Saississez l'id du produit  à modifier");
        int idProducts = scanner.nextInt();
        scanner.nextLine();
        connection = new DataBaseManager().getConnection();
        productsDAO = new ProductsDAO(connection);
        products = productsDAO.getById(idProducts);
        System.out.println("Saississez le nom du produit ");
        System.out.println("Nom actuel : " + products.getName());
        String name = scanner.nextLine();
        System.out.println("Saississez le prix ");
        System.out.println("Prix actuel : " + products.getPrice());
        Float price = scanner.nextFloat();
        scanner.nextLine();
        System.out.println("Saississez la quantité");
        System.out.println("Quantité actuelle : " + products.getQuantity());
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Saississez la description");
        System.out.println("Description : " + products.getDescription());
        String description = scanner.nextLine();
        products = new Products(idProducts, name, price, quantity, description);
        productsDAO.update(products);
    }

    private void deleteProductAction() {
        System.out.println("merci de saisir l'id du produit à supprimer: ");
        int idProduct = scanner.nextInt();
        scanner.nextLine();
        try {
            connection = new DataBaseManager().getConnection();
            connection.setAutoCommit(false);
            productsDAO = new ProductsDAO(connection);
            if (productsDAO.delete(idProduct)) {
                System.out.println("Le produit  a été supprimée avec succès");
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
