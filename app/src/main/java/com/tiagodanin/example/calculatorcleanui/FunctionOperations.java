package com.tiagodanin.example.calculatorcleanui;

public enum FunctionOperations {
    Sum("+"),
    Subtraction("-"),
    Multiplication("x"),
    Division("/"),
    Result("");

    private final String charRepresentation;
    FunctionOperations(String charRepresentation) {
        this.charRepresentation = charRepresentation;
    }

    public String getCharRepresentation() {
        return charRepresentation;
    }
}
