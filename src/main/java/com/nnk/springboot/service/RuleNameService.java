package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exception.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService {

    private final Logger logger = LoggerFactory.getLogger(RuleNameService.class);

    @Autowired
    RuleNameRepository ruleNameRepository;

    /**
     * Get a list of all ruleName
     * @return list of RuleName containing all ruleNames
     */
    public List<RuleName> getAllruleName() {
        logger.info("Service: display ruleName list");
        return ruleNameRepository.findAll();
    }

    /**
     * Save a new RuleName
     * @param ruleName
     * @return RuleName saved
     */
    public RuleName saveRuleName (RuleName ruleName) {
        logger.info("Service: save new ruleName to DB");
        return ruleNameRepository.save(ruleName);
    }

    /**
     * Get ruleName by id.
     * @param id RuleName that containing id of the RuleName
     * @return Instance of RuleName
     */
    public RuleName getRuleNameById(Integer id) {
        Optional<RuleName> rating = ruleNameRepository.findById(id);
        if(rating.isPresent()){
            logger.info("Service: RuleName with Id "+id+" was found");
            return rating.get();
        }else {
            throw new RuleNameNotFoundException("RuleName not found");
        }
    }

    /**
     * Update existing ruleName
     * @param ruleName instance
     * @return the Rating updated
     */
    public RuleName updateRuleName(RuleName ruleName) {
        RuleName ruleNameForUpdate = ruleNameRepository.getById(ruleName.getId());
        ruleNameForUpdate.setName(ruleName.getName());
        ruleNameForUpdate.setDescription(ruleName.getDescription());
        ruleNameForUpdate.setJson(ruleName.getJson());
        ruleNameForUpdate.setTemplate(ruleName.getTemplate());
        ruleNameForUpdate.setSqlStr(ruleName.getSqlStr());
        ruleNameForUpdate.setSqlPart(ruleName.getSqlPart());
        logger.info("Service: Rating updated with ID: " + ruleName.getId());
        return ruleNameRepository.save(ruleNameForUpdate);
    }

    /**
     * Delete a RuleName by id
     * @param id Integer that containing id of the RuleName
     */
    public void deleteRuleName(Integer id) {
        logger.info("Service: RuleName deleted with ID: " + id);
        ruleNameRepository.deleteById(id);
    }
}
