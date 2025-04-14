package com.nnk.springboot.filter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends CrudRepository <Setting, String> {

    public List<Setting> findByCategory(SettingCategory category);


}
