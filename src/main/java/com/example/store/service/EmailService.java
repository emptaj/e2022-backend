package com.example.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String domain = "mail@e-hurtownix.com";
    private final JavaMailSender mailSender;

    @Async
    void send(String to, String subject, String emailContent) {
        try {
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");
            helper.setText(emailContent, true);
            helper.setSubject(subject);
            helper.setFrom(domain);
            helper.setTo(to);
            mailSender.send(mimeMailMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send mail");
        }
    }

    public String buildConfirmationMail(String name, String link) {
        String html = "<html>\n" +
                "    <body>\n<center>\n" +
                "        <h1>Hello, %s</h1>\n" +
                "<p>There is your confimation link: <a href='%s'> CLICK TO CONFIRM </a></p>\n" +
                "Link will expire in 20 minutes\n" +
                "See you soon!" +
                "    </center></body>\n" +
                "</html>\n";

        return String.format(html, name, link);

    }
}
