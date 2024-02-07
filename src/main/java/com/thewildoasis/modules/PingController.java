package com.thewildoasis.modules;

import com.thewildoasis.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
public class PingController {

    @RequestMapping
    public ResponseEntity<ResponseDto> ping() {
        return ResponseEntity.ok(ResponseDto.builder().statusId(1).statusMessage("OK").build());
    }
}
