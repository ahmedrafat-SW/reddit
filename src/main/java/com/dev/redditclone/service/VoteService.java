package com.dev.redditclone.service;

import com.dev.redditclone.dto.VoteDto;
import com.dev.redditclone.exception.PostNotFoundException;
import com.dev.redditclone.exception.SpringRedditException;
import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.Vote;
import com.dev.redditclone.repository.PostRepository;
import com.dev.redditclone.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dev.redditclone.model.VoteType.UPVOTE;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final  AuthenticationService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());
        validateVoteTypeAndCount(voteDto, voteByPostAndUser, post);
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private void validateVoteTypeAndCount(VoteDto voteDto, Optional<Vote> voteByPostAndUser, Post post) {
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
