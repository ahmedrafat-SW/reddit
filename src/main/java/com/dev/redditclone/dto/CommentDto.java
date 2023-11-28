package com.dev.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;
    private long postId;
    private String text;
    private String createdDate;
    private String username;

}
