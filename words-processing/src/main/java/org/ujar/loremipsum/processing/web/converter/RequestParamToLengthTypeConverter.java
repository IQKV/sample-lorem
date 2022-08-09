package org.ujar.loremipsum.processing.web.converter;

import java.util.Arrays;
import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.ujar.loremipsum.processing.enums.LengthType;
import org.ujar.loremipsum.processing.exception.IllegalEnumParameterException;

@Component
public class RequestParamToLengthTypeConverter implements Converter<String, LengthType> {

  @Override
  public LengthType convert(@NonNull String source) {
    try {
      return source.isEmpty() ? null : LengthType.valueOf(source.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      final var values = Arrays.stream(LengthType.values()).map(LengthType::getType).toList();
      throw new IllegalEnumParameterException("l", values);
    }
  }
}
