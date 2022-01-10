package team.hello.usedbook.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class CustomMailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String form;

    @Getter @Setter
    @NoArgsConstructor
    public static class MailDTO {
        private String address;
        private String title;
        private String message;
    }


    public void simpleMailSend(MailDTO mailDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(form);  //naver, daum, nate일 경우 넣어주어야함
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        mailSender.send(message);
    }

    public void mimeMailSend(MailDTO mailDTO){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(form));  //naver, daum, nate일 경우 넣어주어야함
            message.setSubject(mailDTO.getTitle());
            message.setText(mailDTO.getMessage(), "UTF-8", "html");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDTO.getAddress()));
            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
