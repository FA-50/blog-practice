package io.backend.blogproject.repository;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.domain.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private final EntityManagerFactory emf;

    public CategoryRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void save(Category category) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(category);
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

    public Optional<Category> findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            Category category = em.find(Category.class, id);
            return Optional.ofNullable(category);
        } finally {
            em.close();
        }
    }

    public List<Category> findAllActivated() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "select c from Category c where c.status = :status",
                            Category.class
                    )
                    .setParameter("status", Status.ACTIVATED)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Category category) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(category);
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

    public void delete(Category category) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(em.contains(category) ? category : em.merge(category));
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
}