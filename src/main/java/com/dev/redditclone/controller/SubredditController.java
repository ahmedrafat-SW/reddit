package com.dev.redditclone.controller;

import com.dev.redditclone.dto.SubredditDto;
import com.dev.redditclone.service.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {
    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto){
        SubredditDto subreddit = this.subredditService.createSubreddit(subredditDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(subreddit);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubredditById(@PathVariable int id){
        SubredditDto subreddit = this.subredditService.getSubredditBy(id);
        if (subreddit == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Can't find this subreddit");
        }
        return ResponseEntity.ok(subreddit);
    }
    @GetMapping
    public List<SubredditDto> getAllSubreddits(){
        return this.subredditService.getAllSubreddits();
    }

}
