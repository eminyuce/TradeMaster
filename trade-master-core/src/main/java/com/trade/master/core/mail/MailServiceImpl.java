package com.trade.master.core.mail;

import com.trade.master.core.entity.BotUser;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

/**
 * Created by huseyina on 4/9/2017.
 */
@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;
    private String from;

    @Autowired
    private TemplateEngine templateEngine;


    public MailServiceImpl(@Value("${spring.mail.from}") String from) {
        this.from = from;
    }

    @Override
    public void sendMail(String to, String header, String body, boolean isHtml) {
        MimeMessage mail = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo(to);
            helper.setFrom(from);
            helper.setSubject(header);

            helper.setText(body, isHtml);

            javaMailSender.send(mail);
        } catch (jakarta.mail.MessagingException e) {
            logger.error("Error while sending mail", e);
        }
    }

    @Override
    public void sendMail(BotUser user, String header, String body, boolean isHtml) {
        if (user.isEmailNotification()) {
            this.sendMail(user.getUserEmail(), header, body, isHtml);
        }
    }
}
