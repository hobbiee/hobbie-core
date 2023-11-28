package com.br.hobbie.shared.core.validators;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    private final EntityManager manager;
    private Class<?> domainClass;
    private String fieldName;

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        domainClass = constraintAnnotation.domainClass();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        var query = manager.createQuery("select 1 from " + domainClass.getName() + " where " + fieldName + " = :value");
        query.setParameter("value", value);
        var result = query.getResultList();
        return result.isEmpty();
    }
}
