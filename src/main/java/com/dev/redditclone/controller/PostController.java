package com.dev.redditclone.controller;

import com.dev.redditclone.dto.PostRequest;
import com.dev.redditclone.dto.PostResponse;
import com.dev.redditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private PostService postService;


    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        PostResponse post = this.postService.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        var posts = this.postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostBy(@PathVariable int id){
        var post = this.postService.getPostBy(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable int id){
        var posts = this.postService.getPostsBySubredditId(id);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String name){
        var posts = this.postService.getPostsByUsername(name);
        return ResponseEntity.ok(posts);
    }

}
