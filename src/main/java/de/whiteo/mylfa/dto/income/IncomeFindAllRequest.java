package de.whiteo.mylfa.dto.income;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.whiteo.mylfa.util.DateUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
public class IncomeFindAllRequest {

    @JsonFormat(pattern = DateUtil.PATTERN_ISO_8601)
    private LocalDateTime startDate;
    @JsonFormat(pattern = DateUtil.PATTERN_ISO_8601)
    private LocalDateTime endDate;
    private UUID currencyTypeId;
    private UUID categoryId;
}