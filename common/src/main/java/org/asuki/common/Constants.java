package org.asuki.common;

public interface Constants {

    interface Services {

        String ADDRESS_SERVICE = "java:global/demo-app/demo-service-impl/AddressService";
    }

    interface Systems {

        String LINE_SEPARATOR = System.getProperty("line.separator");
        String FILE_SEPARATOR = System.getProperty("file.separator");
        String PATH_SEPARATOR = System.getProperty("path.separator");
    }

}
