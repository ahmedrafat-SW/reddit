package com.dev.redditclone.service;

import com.dev.redditclone.exception.SpringRedditException;
import com.dev.redditclone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final MailContentBuilder mailContentBuilder;
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setFrom("reddit@mail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Verification mail sent.");
        }catch (MailException e){
            throw new SpringRedditException("Exception Occurred when sending mail to "+ notificationEmail.getRecipient());
        }

    }




}
