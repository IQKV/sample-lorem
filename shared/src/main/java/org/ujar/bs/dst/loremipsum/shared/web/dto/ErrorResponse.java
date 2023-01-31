package org.ujar.bs.dst.loremipsum.shared.web.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;

@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record ErrorResponse(List<Error> errors) {
}
