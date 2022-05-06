package org.ujar.loremipsum.wordsstatistic.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LengthType {
  SHORT("short"),
  MEDIUM("medium"),
  LONG("long"),
  VERYLONG("verylong");
  private final String type;
}
