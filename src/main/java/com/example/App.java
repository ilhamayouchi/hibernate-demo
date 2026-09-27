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

        try {
            Server.createWebServer("-web", "-webPort", "8082").start();
            System.out.println("H2 : http://localhost:8082");
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hibernate-demo");

        insererProduits(emf);

        lireProduits(emf);

        mettreAJourPrix(emf, 2L, new BigDecimal("450.00"));

        lireProduits(emf);

        supprimerProduit(emf, 3L);

        lireProduits(emf);

        

        emf.close();
    }

    private static void insererProduits(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        em.persist(new Produit("Laptop", new BigDecimal("999.99")));
        em.persist(new Produit("Smartphone", new BigDecimal("499.99")));
        em.persist(new Produit("Tablette", new BigDecimal("299.99")));

        em.getTransaction().commit();

        em.close();

        System.out.println("Produits insérés.");
    }

    private static void lireProduits(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();

        List<Produit> produits = em.createQuery(
                "SELECT p FROM Produit p", Produit.class
        ).getResultList();

        System.out.println("\nListe des produits :");

        for (Produit p : produits) {
            System.out.println(p);
        }

        em.close();
    }

    private static void mettreAJourPrix(EntityManagerFactory emf,
                                        Long id,
                                        BigDecimal nouveauPrix) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Produit p = em.find(Produit.class, id);

        if (p != null) {
            p.setPrix(nouveauPrix);
            System.out.println("Prix modifié.");
        } else {
            System.out.println("Produit introuvable.");
        }

        em.getTransaction().commit();

        em.close();
    }

    private static void supprimerProduit(EntityManagerFactory emf, Long id) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Produit p = em.find(Produit.class, id);

        if (p != null) {
            em.remove(p);
            System.out.println("Produit supprimé.");
        } else {
            System.out.println("Produit introuvable.");
        }

        em.getTransaction().commit();

        em.close();
    }
}
