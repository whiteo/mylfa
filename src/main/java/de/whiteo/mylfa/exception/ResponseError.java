package de.whiteo.mylfa.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String code;
}