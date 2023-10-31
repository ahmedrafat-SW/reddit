package com.dev.redditclone.repository;

import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.Subreddit;
import com.dev.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostByPostId(Long id);
    Optional<List<Post>> findAllByUser(User user);
    Optional<List<Post>> findAllBySubreddit(Subreddit subreddit);
}
