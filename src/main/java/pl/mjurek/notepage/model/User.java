package pl.mjurek.notepage.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@EqualsAndHashCode
public class User {
    private long id = -1;
    private String name;
    private String email;
    private String password;
}
