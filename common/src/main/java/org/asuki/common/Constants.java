package org.asuki.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.common.base.Ascii;
import com.google.common.base.Charsets;

public interface Constants {

    interface Services {

        String ADDRESS_SERVICE = "java:global/demo-app/demo-service-impl/AddressService";
        String EMPLOYEE_SERVICE = "java:global/demo-app/demo-service-impl/EmployeeService";
        String TRANSLATOR_SERVICE = "java:global/demo-app/demo-service-impl/TranslatorService";
    }

    interface Systems {

        String LINE_SEPARATOR = System.getProperty("line.separator");
        String FILE_SEPARATOR = System.getProperty("file.separator");
        String PATH_SEPARATOR = System.getProperty("path.separator");

        String CARRIAGE_RETURN = new String(new byte[] { Ascii.CR });
    }

    interface Webs {
        String DEFAULT_CHARSET = Charsets.UTF_8.toString();
    }

    interface Ejbs {
        String CONTEXT_DATA_KEY = "xKey";
    }

    @AllArgsConstructor
    enum Language {

        JAPAN("Japan"), CHINA("China");

        @Getter
        private final String value;

    }
}
