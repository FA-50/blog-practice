package io.backend.blogproject.domain.entity;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;


@Entity
@Getter
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

    @OneToOne(mappedBy = "parentId")
    private Comment childId; //

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Comment parentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;


    private Comment(
            Post post,
            String content,
            Members member
    ){
        this.post = post;
        this.content = content;
        this.member = member;
        this.status = Status.ACTIVATED;
        createdAt = LocalDateTime.now();
    }

    private void setParentComment(Comment parentComment){
        this.parentId = parentComment;
    }

    private void setChildComment(Comment childComment){
        this.childId = childComment;
    }

    public static Comment createComment(
        Post post,
        String content,
        Members member
    ){
        validateCreation(post,content);

        Comment comment = new Comment(post, content, member);

        comment.setParentComment(null);

        post.mappedByComment(comment);

        member.addComment(comment);

        return comment;
    }

    public static Comment replyComment(
            Post post,
            String content,
            Comment parentComment,
            Members member
    ){
        validateCreation(post,content);
        if(parentComment == null) throw new RuntimeException(ErrorCode.NO_PARENT_COMMENT.message);

        Comment comment = new Comment(post, content, member);

        post.mappedByComment(comment);

        comment.setParentComment(parentComment);
        parentComment.setChildComment(comment);
        member.addComment(comment);

        return comment;
    }

    public void update(String content) {
        if (Strings.isEmpty(content)) {
            throw new RuntimeException(ErrorCode.NO_COMMENT.message);
        }
        this.content = content;
    }

    public boolean isWrittenBy(Long memberId) {
        return member != null && member.getMemberId().equals(memberId);
    }

    public boolean belongsTo(Long postId) {
        return post != null && post.getPostId().equals(postId);
    }

    public void delete(){
        if (this.status.equals(Status.REMOVED)) throw new RuntimeException(ErrorCode.ALREADY_DELETED.message);
        this.status = Status.REMOVED;
    }

    public static void validateCreation(
            Post post,
            String content
    ){
        if (post == null) throw new RuntimeException(ErrorCode.NO_POST.message);
        if (Strings.isEmpty(content)) throw new RuntimeException(ErrorCode.NO_COMMENT.message);
    }

}
