package org.example;

import org.example.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;

public class Main {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Kreiranje SessionFactory iz hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void main(String[] args) {
        // Add a new product
        addProduct(new Product("Laptop", new BigDecimal("1500.0")));

        // Update an existing product
        updateProduct(1L, "Smartphone", new BigDecimal("800.0"));

        // Delete a product
        deleteProduct(2L);

        // Close the SessionFactory
        sessionFactory.close();
    }

    public static void addProduct(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void updateProduct(Long productId, String newName, BigDecimal newPrice) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, productId);
            if (product != null) {
                product.setName(newName);
                product.setPrice(newPrice);
                session.update(product);
                transaction.commit();
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Product not found!");
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void deleteProduct(Long productId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, productId);
            if (product != null) {
                session.delete(product);
                transaction.commit();
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product not found!");
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
