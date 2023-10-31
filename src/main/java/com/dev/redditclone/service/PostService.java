package com.dev.redditclone.service;


import com.dev.redditclone.dto.PostRequest;
import com.dev.redditclone.dto.PostResponse;
import com.dev.redditclone.exception.PostNotFoundException;
import com.dev.redditclone.exception.SubredditNotFoundException;
import com.dev.redditclone.mapper.PostMapper;
import com.dev.redditclone.model.Post;
import com.dev.redditclone.model.Subreddit;
import com.dev.redditclone.model.User;
import com.dev.redditclone.repository.PostRepository;
import com.dev.redditclone.repository.SubredditRepository;
import com.dev.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private PostRepository postRepository;
    private PostMapper postMapper;
    private AuthenticationService authService;
    private UserRepository userRepository;
    private SubredditRepository subredditRepository;

    public PostResponse savePost(PostRequest postRequest) {
        Subreddit subreddit = this.subredditRepository.findSubredditByName(postRequest.getSubredditName())
                .orElseThrow(() ->
                        new SubredditNotFoundException("Can't find subreddit with name: "+ postRequest.getSubredditName()));
        String username =  authService.getCurrentUser().getUsername();
        User user = this.userRepository.findUserByUsername(username).get();

        Post post = postMapper.mapToPost(postRequest, subreddit, user);
        post = this.postRepository.save(post);
        return postMapper.mapToPostResponse(post);
    }

    public List<PostResponse> getAllPosts() {
        List<Post> allPosts = this.postRepository.findAll();
        return allPosts.stream().map(postMapper::mapToPostResponse).collect(toList());
    }

    public PostResponse getPostBy(long id) {
        Post post = this.postRepository.findPostByPostId(id)
                .orElseThrow(() -> new PostNotFoundException("Can't find post with name"));
        return postMapper.mapToPostResponse(post);
    }


    public List<PostResponse> getPostsBySubredditId(int id) {
        Subreddit subreddit = this.subredditRepository.findSubredditById(id)
                .orElseThrow(()-> new SubredditNotFoundException("Can't find subreddit with id: " + id));
        List<Post> subredditPosts = this.postRepository.findAllBySubreddit(subreddit)
                .orElseThrow(()-> new PostNotFoundException("this subreddit doesn't contain any posts"));
        return subredditPosts.stream().map(postMapper::mapToPostResponse).collect(toList());
    }

    public List<PostResponse> getPostsByUsername(String name) {
        User user = this.userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with username: "+name));
        List<Post> userPosts = this.postRepository.findAllByUser(user)
                .orElseThrow(() -> new PostNotFoundException("user with name ( "+name+" ) doesn't have any posts"));
        return userPosts.stream().map(postMapper::mapToPostResponse).collect(toList());
    }
}
