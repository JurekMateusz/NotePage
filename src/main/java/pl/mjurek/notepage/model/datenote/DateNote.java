package pl.mjurek.notepage.model.datenote;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class DateNote {
  private long id;
  private Timestamp dateStickNote;
  private Timestamp dateDeadlineNote;
  private Timestamp dateUserMadeTask;
}
