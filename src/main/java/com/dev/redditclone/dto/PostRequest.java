package com.dev.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private int id;
    private String name;
    private String subredditName;
    private String url;
    private String description;
}
