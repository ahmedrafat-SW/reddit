package com.dev.redditclone.mapper;


import com.dev.redditclone.dto.PostRequest;
import com.dev.redditclone.dto.PostResponse;
import com.dev.redditclone.model.*;
import com.dev.redditclone.repository.CommentRepository;
import com.dev.redditclone.repository.VoteRepository;
import com.dev.redditclone.service.AuthenticationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.dev.redditclone.model.VoteType.UPVOTE;
import static com.dev.redditclone.model.VoteType.DOWNVOTE;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthenticationService authService;


    @Mapping(target = "username", expression = "java(post.getUser().getUsername())")
    @Mapping(target = "subredditName", expression = "java(post.getSubreddit().getName())")
    @Mapping(target = "id", source = "post.postId")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "voteCount", expression = "java(post.getVoteCount())")
    @Mapping(target = "upVote", expression = "java(isVotedUp(post))")
    @Mapping(target = "downVote", expression = "java(isVotedDown(post))")
    public abstract PostResponse mapToPostResponse(Post post);

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "postName", source = "postRequest.name")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "postId", ignore = true)
    public abstract Post mapToPost(PostRequest postRequest, Subreddit subreddit, User user);


    public Integer getCommentCount(Post post){
        return this.commentRepository.findAllByPost(post).size();
    }

    public String getDuration(Post post){
        return com.github.marlonlom.utilities.timeago.
                TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    public boolean isVotedUp(Post post){
        return checkVoteType(post, UPVOTE);
    }

    public boolean isVotedDown(Post post){
        return checkVoteType(post, DOWNVOTE);
    }


    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

}
