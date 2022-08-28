package com.edu.fud.projectfootbalpitch.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberValidator implements ConstraintValidator<NumberConstraint,String> {
    @Override
    public void initialize(NumberConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String number, ConstraintValidatorContext constraintValidatorContext) {
        return number != null && number.matches("[0-9]+");
    }
}
