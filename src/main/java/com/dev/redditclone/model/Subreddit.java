package com.dev.redditclone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@SequenceGenerator(name = "subreddit_seq",
        sequenceName = "subreddit_seq",
        allocationSize = 1)
public class Subreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subreddit_seq")
    private Long id;
    @NotBlank(message = "Subreddit name shouldn't be null")
    private String name;
    @NotBlank(message = "Description name should not be null")
    private String description;
    private Instant createdDate;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Post> posts;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Subreddit subreddit = (Subreddit) o;
        return getId() != null && Objects.equals(getId(), subreddit.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

