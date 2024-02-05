package com.thewildoasis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ValidationErrorsDto {
    private Map<String, String> validationErrors;
}
