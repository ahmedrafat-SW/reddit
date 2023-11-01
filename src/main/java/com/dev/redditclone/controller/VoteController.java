package com.dev.redditclone.controller;

import com.dev.redditclone.dto.VoteDto;
import com.dev.redditclone.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;


    @PostMapping
    public ResponseEntity<?> vote(@RequestBody VoteDto vote){
        this.voteService.vote(vote);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
