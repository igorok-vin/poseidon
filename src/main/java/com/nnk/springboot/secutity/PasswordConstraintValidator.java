package com.nnk.springboot.secutity;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordValidation,String> {

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
           new LengthRule(8,125),
           new CharacterRule(EnglishCharacterData.UpperCase,1),
           new CharacterRule(EnglishCharacterData.LowerCase,1),
           new CharacterRule(EnglishCharacterData.Special,1),
           new CharacterRule(EnglishCharacterData.Digit),
           new WhitespaceRule()));

        RuleResult ruleResult = validator.validate(new PasswordData(password));
        if (ruleResult.isValid()){
            return true;
        }
        List<String> messages = validator.getMessages(ruleResult);

        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        if (context != null) {
            context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return false;
    }
}
