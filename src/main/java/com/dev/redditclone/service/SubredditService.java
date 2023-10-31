package com.dev.redditclone.service;

import com.dev.redditclone.dto.SubredditDto;
import com.dev.redditclone.exception.SpringRedditException;
import com.dev.redditclone.exception.SubredditNotFoundException;
import com.dev.redditclone.mapper.SubredditMapper;
import com.dev.redditclone.model.Subreddit;
import com.dev.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class SubredditService {

    private SubredditRepository subredditRepository;
    private AuthenticationService authService;
    private SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto createSubreddit(SubredditDto subredditDto) {
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        subreddit.setCreatedDate(Instant.now());
        subreddit.setUser(authService.getCurrentUser());
        subreddit = this.subredditRepository.save(subreddit);
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubredditBy(int id) {
        Subreddit subreddit = this.subredditRepository.findSubredditById(id)
                .orElseThrow(()->
                        new SubredditNotFoundException("Can't find subreddit with id: "+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
    @Transactional(readOnly = true)
    public List<SubredditDto> getAllSubreddits(){
        List<Subreddit> subreddits = this.subredditRepository.findAll();
        return subreddits.stream().map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

}
