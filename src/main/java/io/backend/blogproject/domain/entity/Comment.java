package io.backend.blogproject.domain.entity;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;


@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentStatus commentStatus;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "parrent_id")
    private Comment child_id;

    @OneToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Comment parentId;

    private Comment(
            String content
    ){
        this.content = content;
        this.commentStatus = CommentStatus.ACTIVATED;
        createdAt = LocalDateTime.now();
    }

    private void setComment(Comment parentComment){
        this.parentId = parentComment;
    }

    public static Comment createComment(
        String content,
        Comment parentComment
    ){
        if (Strings.isEmpty(content)) throw new RuntimeException(ErrorCode.NO_COMMENT.message);

        Comment comment = new Comment(content);

        if(parentComment != null) comment.setComment(parentComment);

        return comment;
    }

    public void delete(){
        if(this.commentStatus.equals(CommentStatus.REMOVED)) throw new RuntimeException(ErrorCode.ALEADY_DELETED.message);
        this.commentStatus = CommentStatus.REMOVED;
    }
}
