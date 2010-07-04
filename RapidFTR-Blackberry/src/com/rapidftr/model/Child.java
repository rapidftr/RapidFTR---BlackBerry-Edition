package com.rapidftr.model;

public class Child {

    private final String name;

    public Child(String name) {
        this.name = name;
    }

    public String toString() {
        return "Child: " + name;
    }

    public String getName() {
        return name;
    }
}

