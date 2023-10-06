package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exception.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    RuleNameService ruleNameService;

    @Test
    public void getAllruleName() {
        RuleName ruleName1 = new RuleName();
            ruleName1.setId(1);
            ruleName1.setName("rule1");
            ruleName1.setDescription("description1");
            ruleName1.setJson("json1");
            ruleName1.setTemplate("template1");
            ruleName1.setSqlStr("sqlStr1");
            ruleName1.setSqlPart("sqlPart1");
        RuleName ruleName2 = new RuleName();
            ruleName2.setId(2);
            ruleName2.setName("rule2");
            ruleName2.setDescription("description2");
            ruleName2.setJson("json2");
            ruleName2.setTemplate("template2");
            ruleName2.setSqlStr("sqlStr2");
            ruleName2.setSqlPart("sqlPart2");
        List<RuleName> ruleNameList = new ArrayList<>();
            ruleNameList.add(ruleName1);
            ruleNameList.add(ruleName2);

        when(ruleNameRepository.findAll()).thenReturn(ruleNameList);

        List<RuleName> result = ruleNameService.getAllruleName();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(ruleNameList);
        verify(ruleNameRepository).findAll();
    }

    @Test
    public void saveRuleName() {
        RuleName ruleName = new RuleName();
            ruleName.setId(1);
            ruleName.setName("rule1");
            ruleName.setDescription("description1");
            ruleName.setJson("description1");
            ruleName.setTemplate("template1");
            ruleName.setSqlStr("sqlStr1");
            ruleName.setSqlPart("sqlPart1");
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);

        RuleName result = ruleNameService.saveRuleName(ruleName);

        assertEquals(result, ruleName);
        assertThat(1).isEqualTo(result.getId());
        assertThat("rule1").isEqualTo(result.getName());
        assertThat("description1").isEqualTo(result.getDescription());
        assertThat("template1").isEqualTo(result.getTemplate());
        assertThat("sqlStr1").isEqualTo(result.getSqlStr());
        assertThat("sqlPart1").isEqualTo(result.getSqlPart());
    }

    @Test
    public void getRuleNameById() {
        RuleName ruleName = new RuleName();
            ruleName.setId(1);
            ruleName.setName("rule1");
            ruleName.setDescription("description1");
            ruleName.setJson("description1");
            ruleName.setTemplate("template1");
            ruleName.setSqlStr("sqlStr1");
            ruleName.setSqlPart("sqlPart1");

        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleName));

        RuleName result = ruleNameService.getRuleNameById(1);

        assertEquals(result, ruleName);
        assertThat(1).isEqualTo(result.getId());
        assertThat("rule1").isEqualTo(result.getName());
        assertThat("description1").isEqualTo(result.getDescription());
        assertThat("template1").isEqualTo(result.getTemplate());
        assertThat("sqlStr1").isEqualTo(result.getSqlStr());
        assertThat("sqlPart1").isEqualTo(result.getSqlPart());
    }

    @Test
    public void getRuleNameByIdDoTrowRuleNameNotFoundException() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(RuleNameNotFoundException.class,()->ruleNameService.getRuleNameById(1));
        verify(ruleNameRepository).findById(isA(Integer.class));
    }

    @Test
    public void updateRuleName() {
        RuleName ruleNameBeforeUpdate = new RuleName();
            ruleNameBeforeUpdate.setId(1);
            ruleNameBeforeUpdate.setName("rule1");
            ruleNameBeforeUpdate.setDescription("description1");
            ruleNameBeforeUpdate.setJson("json1");
            ruleNameBeforeUpdate.setTemplate("template1");
            ruleNameBeforeUpdate.setSqlStr("sqlStr1");
            ruleNameBeforeUpdate.setSqlPart("sqlPart1");
        RuleName ruleNameAfterUpdate = new RuleName();
            ruleNameAfterUpdate.setId(1);
            ruleNameAfterUpdate.setName("rule2");
            ruleNameAfterUpdate.setDescription("description2");
            ruleNameAfterUpdate.setJson("json2");
            ruleNameAfterUpdate.setTemplate("template2");
            ruleNameAfterUpdate.setSqlStr("sqlStr2");
            ruleNameAfterUpdate.setSqlPart("sqlPart2");

        when(ruleNameRepository.getById(anyInt())).thenReturn(ruleNameBeforeUpdate);
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleNameAfterUpdate);

        RuleName result = ruleNameService.updateRuleName(ruleNameBeforeUpdate);

        assertEquals(result, ruleNameAfterUpdate);
        assertThat(1).isEqualTo(result.getId());
        assertThat("rule2").isEqualTo(result.getName());
        assertThat("description2").isEqualTo(result.getDescription());
        assertThat("template2").isEqualTo(result.getTemplate());
        assertThat("sqlStr2").isEqualTo(result.getSqlStr());
        assertThat("sqlPart2").isEqualTo(result.getSqlPart());
    }

    @Test
    public void deleteRuleName() {
        RuleName ruleName = new RuleName();
            ruleName.setId(1);
            ruleName.setName("rule1");
            ruleName.setDescription("description1");
            ruleName.setJson("description1");
            ruleName.setTemplate("template1");
            ruleName.setSqlStr("sqlStr1");
            ruleName.setSqlPart("sqlPart1");

        ruleNameService.deleteRuleName(ruleName.getId());

        verify(ruleNameRepository).deleteById(1);
    }
}