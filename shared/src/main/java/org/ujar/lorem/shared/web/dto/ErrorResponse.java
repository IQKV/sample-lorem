package org.ujar.lorem.shared.web.dto;

import java.util.List;

public record ErrorResponse(List<Error> errors) {
}
