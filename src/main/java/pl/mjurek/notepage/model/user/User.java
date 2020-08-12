package pl.mjurek.notepage.model.user;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
  private long id;
  @EqualsAndHashCode.Include private String name;
  private String email;
  @EqualsAndHashCode.Include private String password;
  private String verification;
}
