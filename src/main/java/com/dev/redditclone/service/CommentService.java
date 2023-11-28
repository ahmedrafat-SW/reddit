package com.dev.redditclone.service;

import com.dev.redditclone.dto.CommentDto;
import com.dev.redditclone.exception.PostNotFoundException;
import com.dev.redditclone.mapper.CommentMapper;
import com.dev.redditclone.model.Comment;
import com.dev.redditclone.model.NotificationEmail;
import com.dev.redditclone.model.User;
import com.dev.redditclone.repository.CommentRepository;
import com.dev.redditclone.repository.PostRepository;
import com.dev.redditclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final String POST_URL = "http://localhost:8080/api/by-postId/";

    public CommentDto saveComment(CommentDto dto) {
        var user = this.userRepository.findUserByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with username: "+dto.getUsername()));
        var post = this.postRepository.findPostByPostId(dto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post with id: "+dto.getPostId()+" doesn't exist."));
        Comment comment = this.commentMapper.map(dto, user, post);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL+post.getPostId());
        sendCommentNotification(user, message);
        Comment saved = this.commentRepository.save(comment);
        dto.setId(saved.getId());
        dto.setCreatedDate(commentMapper.getDuration(saved));
        return dto;
    }

    public List<CommentDto> getPostCommentsByPost(long id) {
        var post = this.postRepository.findPostByPostId(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id: "+id+" doesn't exist."));
        return this.commentRepository.findAllByPost(post)
                .stream().map(commentMapper::mapCommentToDto).collect(toList());
    }

    public List<CommentDto> getCommentsByUser(String name) {
        var user = this.userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: "+name+" doesn't exist."));
        return this.commentRepository.findAllByUser(user)
                .stream().map(commentMapper::mapCommentToDto).collect(toList());
    }

    private void sendCommentNotification(User user, String message){
        String msg = this.mailContentBuilder.build(message);
        NotificationEmail notificationEmail =
                new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(),message);
        this.mailService.sendMail(notificationEmail);
    }

}
