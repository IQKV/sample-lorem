package org.ujar.loremipsum.wordsstatistic.web.converter;

import java.util.Locale;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.ujar.loremipsum.wordsstatistic.enums.LengthType;
import org.springframework.core.convert.converter.Converter;

@Component
public class StringParamToLengthTypeConverter implements Converter<String, LengthType> {

  @Override
  public LengthType convert(@NonNull String source) {
    try {
      return source.isEmpty() ? null : LengthType.valueOf(source.trim().toUpperCase(Locale.ROOT));
    } catch(Exception e) {
      return LengthType.SHORT;
    }
  }
}
