package br.edu.ifsc.crudhibernate.dao;

import br.edu.ifsc.crudhibernate.model.Categoria;
import br.edu.ifsc.crudhibernate.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO da entidade Categoria.
 * Métodos: salvar, atualizar, excluir, buscar por ID e listar todos.
 */
public class CategoriaDAO {

    public void salvar(Categoria categoria) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizar(Categoria categoria) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(categoria);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void excluir(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Categoria categoria = em.find(Categoria.class, id);
            if (categoria != null) {
                em.remove(categoria);
            }
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Categoria buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public List<Categoria> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c ORDER BY c.nome", Categoria.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
