package pl.mjurek.notepage.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.mjurek.notepage.model.states.ImportantState;
import pl.mjurek.notepage.model.states.StatusNote;

@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
public class Note {
    private long id;
    private String description;
    private DateNote date;
    private User user;
    private ImportantState importantState;
    private StatusNote statusNote;
}
