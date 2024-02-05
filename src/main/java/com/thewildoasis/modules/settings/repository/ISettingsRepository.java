package com.thewildoasis.modules.settings.repository;

import com.thewildoasis.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISettingsRepository extends JpaRepository<Settings, Integer> {

}
