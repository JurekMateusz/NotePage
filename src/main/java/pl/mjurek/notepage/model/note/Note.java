package pl.mjurek.notepage.model.note;

import lombok.*;
import pl.mjurek.notepage.model.datenote.DateNote;
import pl.mjurek.notepage.model.user.User;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Note {
  private long id;
  private String description;
  private DateNote date;
  private User user;
  private ImportantState importantState;
  private StatusNote statusNote;
}
