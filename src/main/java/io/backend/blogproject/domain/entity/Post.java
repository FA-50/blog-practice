package io.backend.blogproject.domain.entity;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "Post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long postId;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name="viewed_cnt", nullable = false)
    private Long viewedCnt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Visibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();


//
//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable =true)
//    private Category category;

    @Builder
    public Post(String title, String content, Visibility visibility /*, Category category*/ ) {
        this.title = title;
        this.content = content;
        this.visibility = visibility;
        //this.category = category;
        this.viewedCnt = 0L;
        this.status = status.ACTIVATED;
    }

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.viewedCnt == null) {
            this.viewedCnt = 0L;
        }

        if (this.visibility == null) {
            this.visibility = Visibility.PUBLIC;
        }

        if (this.status == null) {
            this.status = Status.ACTIVATED;
        }
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content, Visibility visibility /*, Category category*/){
        this.title = title;
        this.content = content;
        this.visibility = visibility;
        //this.category = category;
    }

    public void mappedByComment(Comment comment){
        this.comments.add(comment);
    }

    public void increaseViewCount(){
        this.viewedCnt++;
    }

    public void remove(){
        this.status = Status.REMOVED;
    }

    public boolean isRemoved() {
        return this.status == Status.REMOVED;
    }

    public boolean isActivated() {
        return this.status == Status.ACTIVATED;
    }
}
