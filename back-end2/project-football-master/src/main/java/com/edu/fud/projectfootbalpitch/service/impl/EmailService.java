package com.edu.fud.projectfootbalpitch.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    public boolean sendEmail(String subject,String message,String to){
        boolean f = false;
        final String username = "zak.shiro@gmail.com";
        final String password = "vfrfhccefkwfgdco";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress("zak.shiro@gmail.com"));
            message1.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message1.setSubject(subject);
            message1.setContent(message,"text/html; charset=UTF-8");
//            message1.setText("Dear Mail Crawler,"
//                    + "\n\n Please do not spam my email!");

            Transport.send(message1);

            System.out.println("Done");
            f = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return f;
    }
}
