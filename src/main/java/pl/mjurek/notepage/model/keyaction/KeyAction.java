package pl.mjurek.notepage.model.keyaction;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class KeyAction {
  private long userId;
  private String key;
}
