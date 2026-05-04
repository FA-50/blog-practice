package io.backend.blogproject.domain.entity;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Status;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private Status status;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "parrent_id")
    private Comment child_id;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Comment parentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    private Comment(
            Post post,
            String content
    ){
        this.post = post;
        this.content = content;
        this.status = Status.ACTIVATED;
        createdAt = LocalDateTime.now();
    }

    private void setParentComment(Comment parentComment){
        this.parentId = parentComment;
    }

    public static Comment createComment(
        Post post,
        String content,
        Comment parentComment
    ){
        if (post == null) throw new RuntimeException(ErrorCode.NO_POST.message);
        if (Strings.isEmpty(content)) throw new RuntimeException(ErrorCode.NO_COMMENT.message);

        Comment comment = new Comment(post,content);

        post.mappedByComment(comment);

        if(parentComment != null) comment.setParentComment(parentComment);

        return comment;
    }

    public void delete(){
        if (this.status.equals(Status.REMOVED)) throw new RuntimeException(ErrorCode.ALREADY_DELETED.message);
        this.status = Status.REMOVED;
    }



}
