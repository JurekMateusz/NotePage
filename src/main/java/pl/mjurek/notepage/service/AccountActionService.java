package pl.mjurek.notepage.service;

import org.apache.commons.codec.digest.DigestUtils;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.email.EmailSessionBean;

public class AccountActionService {//todo
//todo single respon..
    public static String encodePassword(String pass) {
        return DigestUtils.sha1Hex(pass).substring(0, 40);
    }

    public void makeActivateKeyAndSendEmail(User user, StringBuffer patch) throws AddObjectException {
        sleep();
        String activateKey = getKey();
        String url = makeURL(patch);

        KeyActionService service = new KeyActionService();
        service.addKey(user.getId(), activateKey);

        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String email = user.getEmail();
        String subject = "Registration link";
        String content = url + "/verification?key=" + activateKey;
        emailSessionBean.sendEmail(email, subject, content);
    }

    public static String getKey() {
        String key = String.valueOf((int) System.currentTimeMillis() % 3333);
        key = DigestUtils.md5Hex(key);
        return key.substring(0, 20);
    }

    public void sendEmailWithResetLink(String email, StringBuffer patch, String key) {
        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String readyPatch = makeURL(patch);
        String subject = "Reset password";
        String content = "If you want reset password click link bellow:\n " +
                readyPatch + "/reset_password?key=" + key;

        emailSessionBean.sendEmail(email, subject, content);
    }

    private String makeURL(StringBuffer stringBuffer) {
        int lastIndex = stringBuffer.lastIndexOf("/");
        String result = String.valueOf(stringBuffer);
        result = result.substring(0, lastIndex);
        return result;
    }

    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
