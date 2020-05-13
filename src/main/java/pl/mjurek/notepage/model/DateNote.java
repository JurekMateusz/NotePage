package pl.mjurek.notepage.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
@Getter
@Builder
public class DateNote {
    private long id;
    private Timestamp dateStickNote;
    private Timestamp dateDeadlineNote;
    private Timestamp dateUserMadeTask;
}
