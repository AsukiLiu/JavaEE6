package org.asuki.test.service;

import org.asuki.test.BaseArquillian;

public abstract class BaseServiceArquillian extends BaseArquillian {

    static {
        webInfResources.put("META-INF/xml/entity.xml",
                "classes/META-INF/xml/entity.xml");
        webInfResources.put("META-INF/xml/jpql.xml",
                "classes/META-INF/xml/jpql.xml");
        webInfResources.put("META-INF/xml/sql.xml",
                "classes/META-INF/xml/sql.xml");
        webInfResources.put("META-INF/testPersistence.xml",
                "classes/META-INF/persistence.xml");
    }

}
