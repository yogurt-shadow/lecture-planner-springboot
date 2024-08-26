package com.github.mitschi.validation;

import com.github.mitschi.models.Employee;
import com.github.mitschi.models.validation.EmployeeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeValidatorTest {
    private EmployeeValidator validator;

    @BeforeEach
    public void setup(){
        validator = new EmployeeValidator();
    }

    @Test
    public void isNameValid_null_isInvalid(){
        boolean res = validator.isNameValid(null);
        Assertions.assertFalse(res);
    }

    @Test
    public void isNameValid_emptyString_isInvalid(){
        boolean res = validator.isNameValid("");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNameValid_someName_isValid(){
        boolean res = validator.isNameValid("Peter");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNameValid_someLowercaseName_isValid(){
        boolean res = validator.isNameValid("peter");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNameValid_concatenatedNames_isValid(){
        boolean res = validator.isNameValid("Peter Müller");
        Assertions.assertTrue(res);
    }

    @Test
    public void isEmployeeNumberValid_negativeNum_isInvalid(){
        // is -1 better than -5 ?
        boolean res = validator.isEmployeeNumberValid(-5);
        Assertions.assertFalse(res);
    }

    @Test
    public void isEmployeeNumberValid_Zero_isInvalid(){
        boolean res = validator.isEmployeeNumberValid(0);
        Assertions.assertFalse(res);
    }

    @Test
    public void isEmployeeNumberValid_posNum_isValid(){
        boolean res = validator.isEmployeeNumberValid(1);
        Assertions.assertTrue(res);
    }

    @Test
    public void isEmployeeValid_null_isInvalid(){
        boolean res = validator.isEmployeeValid(null);
        Assertions.assertFalse(res);
    }

    @Test
    public void isEmployeeValid_SomeEmployee_isValid(){
        Employee someEmployee = new Employee("peter", 1);
        boolean res = validator.isEmployeeValid(someEmployee);
        Assertions.assertTrue(res);
    }
}
