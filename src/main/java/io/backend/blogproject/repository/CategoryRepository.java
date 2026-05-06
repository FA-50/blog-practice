package io.backend.blogproject.repository;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.domain.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManagerFactory emf;

    public Category save(Category category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(category);

            tx.commit();

            return category;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("카테고리 저장에 실패했습니다.", e);
        } finally {
            em.close();
        }
    }
}