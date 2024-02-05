package com.thewildoasis.modules.settings.controller;

import com.thewildoasis.entities.Settings;
import com.thewildoasis.modules.settings.service.ISettingsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Data
@RequestMapping("/api/v1/settings")
@Validated
public class SettingsController {
    private final ISettingsService settingsService;

    @GetMapping("/")
    public ResponseEntity<Settings> getSettings() {
        return ResponseEntity.ok(getSettingsService().getSettings());
    }

    @PatchMapping("/update")
    public ResponseEntity<Settings> updateSettings(@Valid @RequestBody Settings settings) {
        return ResponseEntity.ok(getSettingsService().updateSettings(settings));
    }
}
