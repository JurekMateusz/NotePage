package pl.mjurek.notepage.service.email.bean;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.user.UserService;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
@LocalBean
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailSessionBean {
  private static EmailSessionBean emailSessionBean;

  @NonNull protected String mailEmailFrom;
  @NonNull protected String username;
  @NonNull protected String password;

  protected String mailSmtpAuth = "true";
  protected String mailSmtpHost = "smtp.gmail.com";
  protected int mailSmtpPort = 587;
  protected String mailSmtpStarttlsEnable = "true";

  public static EmailSessionBean getInstance() {
    if (emailSessionBean == null) {
      UserService service = new UserService();
      User user = service.getUserByUserName("ADMIN");
      String email = user.getEmail();
      String password = user.getPassword();
      emailSessionBean = new EmailSessionBean(email, email, password);
    }
    return emailSessionBean;
  }

  @Asynchronous
  public void sendEmail(String recipientEmail, String subject, String content) {
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Properties props = new Properties();
    props.put("mail.smtp.auth", mailSmtpAuth);
    props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
    props.put("mail.smtp.host", mailSmtpHost);
    props.put("mail.smtp.port", mailSmtpPort);
    props.put("mail.smtp.ssl.trust", mailSmtpHost);
    Session session =
        Session.getInstance(
            props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
              }
            });
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(mailEmailFrom));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
      message.setSubject(subject);
      message.setText(content);
      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
