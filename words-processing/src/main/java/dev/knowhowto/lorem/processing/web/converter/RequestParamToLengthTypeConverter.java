package dev.knowhowto.lorem.processing.web.converter;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Locale;

import dev.knowhowto.lorem.processing.enums.LengthType;
import dev.knowhowto.lorem.processing.exception.IllegalEnumParameterException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestParamToLengthTypeConverter implements Converter<String, LengthType> {

  @Override
  public LengthType convert(@NotNull String source) {
    try {
      return source.isEmpty() ? null : LengthType.valueOf(source.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      final var values = Arrays.stream(LengthType.values()).map(LengthType::getType).toList();
      throw new IllegalEnumParameterException("l", values);
    }
  }
}
