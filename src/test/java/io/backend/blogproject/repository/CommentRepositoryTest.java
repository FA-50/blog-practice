package io.backend.blogproject.repository;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Comment;
import io.backend.blogproject.domain.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManagerFactory emf;
    Comment comment = null;

    Post post;

    @BeforeEach
    void setUp(){
        post = new Post(
                "제목",
                "내용",
                Visibility.PUBLIC
        );

        try(
                EntityManager em = emf.createEntityManager()
        ) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(post);

            comment = Comment.createComment(
                    post,
                    "댓글 내용"
            );
            em.persist(comment);
            System.out.println(comment.getId());
            tx.commit();
        }
    }

    @Test
    void 댓글_조회_테스트(){
        List<Comment> commentsByPostId = commentRepository.findCommentsByPostId(post.getPostId());
        Assertions.assertEquals(commentsByPostId.get(0).getContent(), comment.getContent());
    }

    @Test
    void 댓글_조회_실패_테스트__댓글_삭제됨(){
        try(
                EntityManager em = emf.createEntityManager();
         ){
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            comment.delete();
            em.merge(comment);
            tx.commit();
        };

        List<Comment> commentsByPostId = commentRepository.findCommentsByPostId(post.getPostId());
        Assertions.assertEquals(0, commentsByPostId.size());
    }

    @Test
    void 댓글_생성_성공(){
        commentRepository.createComment(
                post,
                "생성된댓글"
        );

        List<Comment> founded = commentRepository.findCommentsByPostId(post.getPostId());
        Assertions.assertEquals(founded.get(1).getContent(),"생성된댓글");
    }

    @Test
    void 대댓글_생성_성공(){
        commentRepository.replyComment(
                post,
                comment,
                "생성된댓글"
        );

        Comment founded = commentRepository.findCommentByCommentId(comment.getId());
        Assertions.assertEquals(founded.getChildId().getContent(), "생성된댓글");
    }

    @Test
    void 댓글_삭제_성공(){
        commentRepository.deleteComment(
                comment
        );

        assertThatThrownBy(
                ()-> commentRepository.findCommentByCommentId(comment.getId())
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(ErrorCode.UNABLE_TO_FIND_COMMENT.message);
    }
}