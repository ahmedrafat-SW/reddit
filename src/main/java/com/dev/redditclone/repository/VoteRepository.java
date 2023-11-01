package com.dev.redditclone.repository;

import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.User;
import com.dev.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);
    Optional<Vote> findVoteByPost(Post post);

}
