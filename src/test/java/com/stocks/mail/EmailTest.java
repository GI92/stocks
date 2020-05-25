package com.stocks.mail;

import com.icegreen.greenmail.util.GreenMailUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EmailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

    @Before
    public void before() throws Throwable {
        smtpServerRule.before();
    }

    @Test
    public void mailTest() throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@sender.com");
        message.setTo("test@receiver.com");
        message.setSubject("test subject");
        message.setText("test message");

        try {
            smtpServerRule.before();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        mailSender.send(message);

        MimeMessage[] messages = smtpServerRule.getMessages();
        assertEquals(1, messages.length);
        assertEquals("test subject", messages[0].getSubject());
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        assertEquals("test message", body);
    }

    @After
    public void after() {
        smtpServerRule.after();
    }
}
