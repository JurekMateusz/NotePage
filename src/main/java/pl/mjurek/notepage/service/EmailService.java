package pl.mjurek.notepage.service;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.email.EmailSessionBean;
import pl.mjurek.notepage.service.fun.Hash;

public class EmailService {

    public void makeActivateKeyAndSendEmail(User user, StringBuffer patch) throws AddObjectException {
        sleep();
        String activateKey = Hash.getKey();
        String url = provideURL(patch);

        KeyActionService service = new KeyActionService();
        service.addKey(user.getId(), activateKey);

        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String email = user.getEmail();
        String subject = "Registration link";
        String content = url + "/verification?key=" + activateKey;
        emailSessionBean.sendEmail(email, subject, content);
    }

    public void sendEmailWithResetLink(String email, StringBuffer patch, String key) {
        EmailSessionBean emailSessionBean = EmailSessionBean.getInstance();
        String readyPatch = provideURL(patch);
        String subject = "Reset password";
        String content = "If you want reset password click link bellow:\n " +
                readyPatch + "/reset_password?key=" + key;

        emailSessionBean.sendEmail(email, subject, content);
    }

    private String provideURL(StringBuffer stringBuffer) {
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
