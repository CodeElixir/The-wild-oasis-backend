package com.thewildoasis.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private Integer statusId;
    private String statusMessage;
}
