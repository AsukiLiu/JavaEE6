package org.asuki.common.javase;

import java.util.HashMap;

import lombok.Getter;

public class ImmutableClass {

    /* Required fields */

    @Getter
    private int id;

    @Getter
    private String name;

    /* Optional fields */

    @Getter
    private String company;

    private HashMap<String, String> properties;

    private ImmutableClass(ImmutableClassBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.company = builder.company;
        this.properties = builder.properties;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getProperties() {
        return (HashMap<String, String>) properties.clone();
    }

    public static class ImmutableClassBuilder {

        /* Required fields */

        private int id;

        private String name;

        /* Optional fields */

        private String company;

        private HashMap<String, String> properties;

        public ImmutableClassBuilder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public ImmutableClassBuilder setCompany(String company) {
            this.company = company;
            return this;
        }

        @SuppressWarnings("unchecked")
        public ImmutableClassBuilder setProperties(
                HashMap<String, String> properties) {
            this.properties = (HashMap<String, String>) properties.clone();
            return this;
        }

        public ImmutableClass build() {
            return new ImmutableClass(this);
        }
    }
}
