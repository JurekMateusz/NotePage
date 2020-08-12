package pl.mjurek.notepage.service.email;

import pl.mjurek.notepage.service.email.bean.EmailSessionBean;
import pl.mjurek.notepage.service.executor.LongTermTask;

import java.util.Map;
import java.util.Objects;

public class EmailService implements LongTermTask {
  protected String email;
  protected String subject;
  protected String content;

  private EmailService() {}

  public EmailService(Map<String, String> map) {
    this.email = map.get("email");
    this.subject = map.get("subject");
    this.content = map.get("content");
    if (isAnyNull()) {
      throw new RuntimeException("Email, subject or content is null");
    }
  }

  private boolean isAnyNull() {
    return Objects.isNull(email) || Objects.isNull(subject) || Objects.isNull(content);
  }

  @Override
  public void run() {
    sendEmail();
  }

  protected void sendEmail() {
    EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
    emailSessionBean.sendEmail(email, subject, content);
  }
}
