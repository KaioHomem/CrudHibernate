package br.edu.ifsc.crudhibernate.dao;

import br.edu.ifsc.crudhibernate.model.Produto;
import br.edu.ifsc.crudhibernate.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO da entidade Produto.
 * Inclui o método listarPorCategoria, usado para mostrar
 * somente os produtos da categoria selecionada.
 */
public class ProdutoDAO {

    public void salvar(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(produto);
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

    public void atualizar(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(produto);
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
            Produto produto = em.find(Produto.class, id);
            if (produto != null) {
                em.remove(produto);
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

    public Produto buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Produto> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produto p ORDER BY p.nome", Produto.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista apenas os produtos vinculados a uma categoria específica.
     */
    public List<Produto> listarPorCategoria(Integer categoriaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                        "SELECT p FROM Produto p WHERE p.categoria.id = :catId ORDER BY p.nome",
                        Produto.class)
                     .setParameter("catId", categoriaId)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
