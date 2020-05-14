package pl.mjurek.notepage.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@Builder(toBuilder=true)
public class DateNote {
    private long id;
    private Timestamp dateStickNote;
    private Timestamp dateDeadlineNote;
    private Timestamp dateUserMadeTask;
}
