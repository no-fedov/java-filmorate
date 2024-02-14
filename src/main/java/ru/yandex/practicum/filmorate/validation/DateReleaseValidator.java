package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateReleaseValidator implements ConstraintValidator<DateRelease, LocalDate> {
    LocalDate FIRST_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(DateRelease constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.isAfter(FIRST_DATE_RELEASE);
    }
}
