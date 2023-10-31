package com.dev.redditclone.mapper;

import com.dev.redditclone.dto.SubredditDto;
import com.dev.redditclone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(subreddit.getPosts().size())")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
