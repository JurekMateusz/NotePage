package pl.mjurek.notepage.service.user.delete;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.executor.LongTermTask;
import pl.mjurek.notepage.service.note.NoteService;
import pl.mjurek.notepage.service.user.UserService;

public class DeleteAccountService implements LongTermTask {
  private final User user;

  public DeleteAccountService(User user) {
    this.user = user;
  }

  @Override
  public void run() {
    UserService userService = new UserService();
    long userId = user.getId();
    userService.updateVerification(userId, "NO");

    NoteService noteService = new NoteService();
    try {
      noteService.deleteAllUserNotes(userId);
      userService.delete(userId);
    } catch (DeleteObjectException e) {
      e.printStackTrace();
    }
  }
}
