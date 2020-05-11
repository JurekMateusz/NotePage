package pl.mjurek.notepage.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private long id;
    @EqualsAndHashCode.Include
    private String name;
    private String email;
    @EqualsAndHashCode.Include
    private String password;
}
