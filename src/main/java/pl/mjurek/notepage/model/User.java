package pl.mjurek.notepage.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private long id;
    @EqualsAndHashCode.Include
    private String name;
    private String email;
    @EqualsAndHashCode.Include
    private String password;
    private String verification;
}
