package com.rapidftr.model;

public class ChildBuilder {

    private String creationDate = "2010/10/10";
    private String name = "test-name";
    private String id = "1";

    public ChildBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Child build() {
        Child child = new Child(creationDate);

        child.setField("name", name);
        child.setId(id);

        return new Child(creationDate);
    }
}
