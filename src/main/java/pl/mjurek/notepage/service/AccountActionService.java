package pl.mjurek.notepage.service;

import org.apache.commons.codec.digest.DigestUtils;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.KeyAction;
import pl.mjurek.notepage.service.email.EmailSessionBean;
import pl.mjurek.notepage.model.User;

public class AccountActionService {
    public void makeActivateKeyAndSendEmail(User user, String patch) {
        sleep();
        long userId = user.getId();
        String id = String.valueOf(userId);
        String buf = DigestUtils.md5Hex(id);
        String activateKey = buf.substring(0, 20);

        KeyActionService service = new KeyActionService();
        service.addKey(userId, activateKey);

        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String email = user.getEmail();
        String subject = "Registration link";
        String content = patch + "/verification?key=" + activateKey;
        emailSessionBean.sendEmail(email, subject, content);
    }

    public boolean verification(String key) {
        KeyActionService service = new KeyActionService();
        KeyAction keyAction = service.read(key);
        if(keyAction == null){
            return false;
        }else {
            long userId = keyAction.getUserId();
            UserService userService = new UserService();

            try {
                userService.unblock(userId);
            } catch (UpdateObjectException e) {
                return false;
            }

            service.delete(userId);
        }
        return true;
    }


    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
