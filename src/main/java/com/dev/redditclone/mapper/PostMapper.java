package com.dev.redditclone.mapper;


import com.dev.redditclone.dto.PostRequest;
import com.dev.redditclone.dto.PostResponse;
import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.Subreddit;
import com.dev.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "username", expression = "java(post.getUser().getUsername())")
    @Mapping(target = "subredditName", expression = "java(post.getSubreddit().getName())")
    @Mapping(target = "id", source = "post.postId")
    PostResponse mapToPostResponse(Post post);

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "postName", source = "postRequest.name")
    @Mapping(target = "description", source = "postRequest.description")
    Post mapToPost(PostRequest postRequest, Subreddit subreddit, User user);

}
