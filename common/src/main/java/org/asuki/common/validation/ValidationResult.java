package org.asuki.common.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    public List<String> violations = new ArrayList<>();

    public void addViolation(String violation) {
        violations.add(violation);
    }

    public boolean isValid() {
        return violations.isEmpty();
    }

    @Override
    public String toString() {
        return violations.toString();
    }
}
