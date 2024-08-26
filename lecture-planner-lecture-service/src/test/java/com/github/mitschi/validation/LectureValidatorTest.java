package com.github.mitschi.validation;

import com.github.mitschi.models.Lecture;
import com.github.mitschi.models.validation.LectureValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LectureValidatorTest {
    private LectureValidator validator;

    @BeforeEach
    public void setup(){
        validator = new LectureValidator();
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
        boolean res = validator.isNameValid("ASE");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNameValid_someLowercaseName_isValid(){
        boolean res = validator.isNameValid("ase");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNameValid_concatenatedNames_isValid(){
        boolean res = validator.isNameValid("Advanced Software Engineering");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNumberValid_null_isInvalid(){
        boolean res = validator.isNumberValid(null);
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_emptyString_isInvalid(){
        boolean res = validator.isNumberValid("");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_someString_isInvalid(){
        boolean res = validator.isNumberValid("test");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_singleNumberWithDot_isInvalid(){
        boolean res = validator.isNumberValid("123.");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_twoDigitsWithDot_isInvalid(){
        boolean res = validator.isNumberValid("1.2");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_onlyOneNumberWithCorrectLength_isInvalid(){
        boolean res = validator.isNumberValid("123.1");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_twoStringsWithCorrectLengths1_isInvalid(){
        boolean res = validator.isNumberValid("123.abc");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_twoStringsWithCorrectLengths2_isInvalid(){
        boolean res = validator.isNumberValid("abc.123");
        Assertions.assertFalse(res);
    }

    @Test
    public void isNumberValid_twoNumbersWithCorrectLengths1_isValid(){
        boolean res = validator.isNumberValid("123.123");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNumberValid_twoNumbersWithCorrectLengths2_isValid(){
        boolean res = validator.isNumberValid("123.000");
        Assertions.assertTrue(res);
    }

    @Test
    public void isNumberValid_twoNumbersTooLong_isInvalid(){
        boolean res = validator.isNumberValid("123.1234");
        Assertions.assertFalse(res);
    }

    @Test
    public void isLecturerIdValid_null_isInvalid(){
        boolean res = validator.isLecturerIdValid(null);
        Assertions.assertFalse(res);
    }

    @Test
    public void isLecturerIdValid_negativeNum_isInvalid(){
        // is -1 better than -10 ?
        boolean res = validator.isLecturerIdValid(-10L);
        Assertions.assertFalse(res);
    }

    @Test
    public void isLecturerIdValid_zero_isInvalid(){
        boolean res = validator.isLecturerIdValid(0L);
        Assertions.assertFalse(res);
    }

    @Test
    public void isLecturerIdValid_positiveNum_isValid(){
        // is 1 better than 27 ?
        boolean res = validator.isLecturerIdValid(27L);
        Assertions.assertTrue(res);
    }

    @Test
    public void isLectureValid_null_isInvalid(){
        boolean res = validator.isLectureValid(null);
        Assertions.assertFalse(res);
    }

    @Test
    public void isLectureValid_someLecture_isValid(){
        Lecture someLecture = new Lecture("ase", "123.456", 1L);
        boolean res = validator.isLectureValid(someLecture);
        Assertions.assertTrue(res);
    }
}
