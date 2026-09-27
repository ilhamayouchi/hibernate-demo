package com.example;

import com.example.model.Produit;
import org.h2.tools.Server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

public class App {
    public static void main(String[] args) {

        // Démarrage de la console Web H2
        try {
            Server.createWebServer("-web", "-webPort", "8082").start();
            System.out.println("Console H2 disponible sur : http://localhost:8082");
        } catch (Exception e) {
            System.out.println("Erreur lors du démarrage de la console H2");
            e.printStackTrace();
        }

        // Création de l'EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-demo");

        // Insertion de produits
        insererProduits(emf);

        // Lecture des produits
        lireProduits(emf);

        // Mise à jour du prix d'un produit
        mettreAJourPrix(emf, 2L, new BigDecimal("450.00"));

        // Relecture pour vérifier la mise à jour
        lireProduits(emf);

        // Suppression d'un produit
        supprimerProduit(emf, 3L);

        // Relecture pour vérifier la suppression
        lireProduits(emf);

        // Garder l'application ouverte pour consulter la console H2
        System.out.println("\nAppuyez sur Entrée pour quitter...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fermeture de l'EntityManagerFactory
        emf.close();
    }

    private static void insererProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Produit p1 = new Produit("Laptop", new BigDecimal("999.99"));
            Produit p2 = new Produit("Smartphone", new BigDecimal("499.99"));
            Produit p3 = new Produit("Tablette", new BigDecimal("299.99"));

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
            System.out.println("Produits insérés avec succès !");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void lireProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class)
                    .getResultList();

            System.out.println("\nListe des produits :");
            for (Produit produit : produits) {
                System.out.println(produit);
            }
        } finally {
            em.close();
        }
    }

    // Exercice 1 : mettre à jour le prix d'un produit
    private static void mettreAJourPrix(EntityManagerFactory emf, Long id, BigDecimal nouveauPrix) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Produit produit = em.find(Produit.class, id);
            if (produit != null) {
                produit.setPrix(nouveauPrix);
                em.getTransaction().commit();
                System.out.println("\nPrix du produit ID=" + id + " mis à jour : " + nouveauPrix);
            } else {
                em.getTransaction().rollback();
                System.out.println("\nProduit ID=" + id + " introuvable, mise à jour annulée.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Exercice 2 : supprimer un produit
    private static void supprimerProduit(EntityManagerFactory emf, Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Produit produit = em.find(Produit.class, id);
            if (produit != null) {
                em.remove(produit);
                em.getTransaction().commit();
                System.out.println("\nProduit ID=" + id + " supprimé avec succès.");
            } else {
                em.getTransaction().rollback();
                System.out.println("\nProduit ID=" + id + " introuvable, suppression annulée.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}