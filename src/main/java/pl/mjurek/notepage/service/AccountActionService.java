package pl.mjurek.notepage.service;

import org.apache.commons.codec.digest.DigestUtils;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.KeyAction;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.email.EmailSessionBean;

import java.util.Random;

public class AccountActionService {//todo

    public static String encodePassword(String pass) {
        return DigestUtils.sha1Hex(pass).substring(0, 40);
    }

    public void makeActivateKeyAndSendEmail(User user, String patch) throws AddObjectException {
        sleep();
        String activateKey = getKey();

        KeyActionService service = new KeyActionService();
        service.addKey(user.getId(), activateKey);

        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String email = user.getEmail();
        String subject = "Registration link";
        String content = patch + "/verification?key=" + activateKey;
        emailSessionBean.sendEmail(email, subject, content);
    }

    public void verification(String key) throws UpdateObjectException, DeleteObjectException {
        KeyActionService service = new KeyActionService();
        KeyAction keyAction = service.read(key);

        long userId = keyAction.getUserId();
        UserService userService = new UserService();
        userService.unblock(userId);
        service.delete(userId);
    }

    public String getKey() {
        String key = String.valueOf((int) System.currentTimeMillis() % 3333);
        key = DigestUtils.md5Hex(key);
        return key.substring(0, 20);
    }

    public void sendEmail(String email, String password) {
        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String subject = "Password Reset";
        String content = "YOUR NEW PASSWORD:  " + password;
        emailSessionBean.sendEmail(email, subject, content);
    }

    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getNewPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random(System.currentTimeMillis() % 17);

        for (int i = 0; i < 4; i++) {
            password.append((char) (random.nextInt(90) + 65));
            password.append((char) (random.nextInt(63) + 46));
        }
        return password.toString();
    }
}
