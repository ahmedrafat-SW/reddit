package com.dev.redditclone.controller;

import com.dev.redditclone.dto.CommentDto;
import com.dev.redditclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment){
        var created = this.commentService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/by-postId/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable long id){
        var comments = this.commentService.getPostCommentsByPost(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<CommentDto>> getPostCommentsByUser(@PathVariable String name){
        var comments = this.commentService.getCommentsByUser(name);
        return ResponseEntity.ok(comments);
    }

}
