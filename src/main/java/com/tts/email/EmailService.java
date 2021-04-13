package com.tts.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;

public class EmailService
{
    @Value("mail.smtp.auth")
    String smtpAuth;
    
    @Value("mail.smtp.starttls.enable")
    String tlsEnable;
    
    @Value("mail.smtp.host")
    String smtpHost;
    
    @Value("mail.smtp.port")
    String smtpPort;
    
    @Value("mail.smtp.ssl.trust")
    String sslTrust; 
    
    @Value("mail.smtp.password")
    String password;
    
    @Value("mail.smtp.username")
    String username;

    Properties properties;
    
    public EmailService()
    {
        properties.put("mail.smpt.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", tlsEnable);
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);        
        properties.put("mail.smtp.ssl.trust", sslTrust);        
    }
    
    private Session openSession()
    {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });        
    }
    
    public boolean sendMail(String email, String title, String emailMessage)
    {
        Session session = openSession();
        Message message = new MimeMessage(session);
        try
        {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(title);
         
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailMessage, "text/html");
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            
            message.setContent(multipart);
            
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            //Email wasn't sent.
            return false;
        }
        return true;        
    }
}
