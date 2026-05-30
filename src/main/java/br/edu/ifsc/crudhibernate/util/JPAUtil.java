package br.edu.ifsc.crudhibernate.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária que centraliza a criação do EntityManager.
 * O nome "crudPU" deve ser o mesmo definido no persistence.xml.
 */
public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("crudPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
