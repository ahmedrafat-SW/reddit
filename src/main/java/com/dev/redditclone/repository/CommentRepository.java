package com.dev.redditclone.repository;

import com.dev.redditclone.model.Comment;
import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);
    List<Comment> findAllByPost(Post post);

}
