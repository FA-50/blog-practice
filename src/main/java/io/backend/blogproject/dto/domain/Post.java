package io.backend.blogproject.dto.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "Post")
@NoArgsConstructor
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
//
//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable =false)
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
