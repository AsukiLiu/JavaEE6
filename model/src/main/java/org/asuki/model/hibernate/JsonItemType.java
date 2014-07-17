package org.asuki.model.hibernate;

import org.asuki.model.jackson.JsonItem;

public class JsonItemType extends BaseUserType<JsonItem> {

    @Override
    public Class<JsonItem> returnedClass() {
        return JsonItem.class;
    }

}
