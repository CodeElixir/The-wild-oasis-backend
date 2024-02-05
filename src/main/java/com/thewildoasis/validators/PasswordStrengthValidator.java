package com.thewildoasis.validators;

import com.thewildoasis.annotations.PasswordStrength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {
    List<String> weakPasswords;

    @Override
    public void initialize(PasswordStrength passwordStrength) {
        weakPasswords = Arrays.asList("12345678", "password", "qwerty123");
    }

    @Override
    public boolean isValid(String passwordField,
                           ConstraintValidatorContext cxt) {
        return !weakPasswords.contains(passwordField);
    }
}
