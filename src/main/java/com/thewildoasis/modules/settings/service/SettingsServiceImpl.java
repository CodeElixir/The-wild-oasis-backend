package com.thewildoasis.modules.settings.service;

import com.thewildoasis.entities.Settings;
import com.thewildoasis.modules.settings.repository.ISettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsServiceImpl implements ISettingsService {

    private final ISettingsRepository settingsRepository;

    @Override
    public Settings getSettings() {
        if (settingsRepository.count() > 0) {
            return settingsRepository.findAll().get(0);
        }
        return new Settings();
    }

    @Override
    public Settings updateSettings(Settings settings) {
        return settingsRepository.save(settings);
    }
}
