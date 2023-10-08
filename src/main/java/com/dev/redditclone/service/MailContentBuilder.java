package com.dev.redditclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message){
        Context thymeleafCtx = new Context();
        thymeleafCtx.setVariable("message", message);
        return this.templateEngine.process("mailTemplate", thymeleafCtx);
    }
}
