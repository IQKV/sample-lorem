/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixlorem.processing.web.converter;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Locale;

import com.iqkv.incubator.sample.mixlorem.processing.enums.LengthType;
import com.iqkv.incubator.sample.mixlorem.processing.exception.IllegalEnumParameterException;
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
