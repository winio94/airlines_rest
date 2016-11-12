package com.service;

import org.springframework.mail.javamail.JavaMailSender;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static javax.mail.Message.RecipientType.TO;

/**
 * Created by Michał on 2016-11-12.
 */
@Named
public class MailSender {

    @Inject
    private JavaMailSender javaMailSender;

    public void send(InternetAddress address, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setSubject(subject);
        mimeMessage.setRecipient(TO, address);
        mimeMessage.setText(body);

        javaMailSender.send(mimeMessage);
    }
}
