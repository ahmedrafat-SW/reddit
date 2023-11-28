package com.dev.redditclone.mapper;

import com.dev.redditclone.dto.CommentDto;
import com.dev.redditclone.model.Comment;
import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "createdDate", expression = "java(getDuration(comment))")
    CommentDto mapCommentToDto(Comment comment);

    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "CreatedDate", expression = "java(java.time.Instant.now())")
    Comment map(CommentDto dto, User user, Post post);

    default String getDuration(Comment comment){
        return com.github.marlonlom.utilities.timeago.
                TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }
}
