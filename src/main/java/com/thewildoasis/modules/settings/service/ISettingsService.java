package com.thewildoasis.modules.settings.service;


import com.thewildoasis.entities.Settings;

public interface ISettingsService {
    Settings getSettings();

    Settings updateSettings(Settings settings);
}
