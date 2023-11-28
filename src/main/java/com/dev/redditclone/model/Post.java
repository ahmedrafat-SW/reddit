package com.dev.redditclone.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.Instant;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "post_seq", sequenceName = "post_seq",allocationSize = 1)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    private Long postId;
    @NotBlank(message = "Post name should not be null")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob
    @Column(columnDefinition = "text")
    private String description;
    private Integer voteCount;
    private Instant createdDate;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subredditId", referencedColumnName = "id")
    private Subreddit subreddit;

}
