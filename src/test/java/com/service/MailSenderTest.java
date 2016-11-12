package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

/**
 * Created by Michał on 2016-11-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class MailSenderTest {

    private static final String DEFAULT_BODY = "defaultBody";
    private static final String DEFAULT_SUBJECT = "defaultSubject";
    private final InternetAddress DEFAULT_ADDRESS = mock(InternetAddress.class);
    private final MimeMessage mimeMessage = mock(MimeMessage.class);

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailSender mailSender;

    @Test
    public void shouldSendMessageForGivenArguments() throws Exception {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailSender.send(DEFAULT_ADDRESS, DEFAULT_SUBJECT, DEFAULT_BODY);

        verify(javaMailSender).send(any(MimeMessage.class));
    }
}