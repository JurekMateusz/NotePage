package pl.mjurek.notepage.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Note {
    private long id;
    private String description;
    private DateNote date;
    private User user;
    private ImportantState importantState;
}
