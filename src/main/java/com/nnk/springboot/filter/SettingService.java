package com.nnk.springboot.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> findByCategory() {
        List<Setting> settingList =  settingRepository.findByCategory(SettingCategory.PERMISSION);
        return settingList;
    }
}
