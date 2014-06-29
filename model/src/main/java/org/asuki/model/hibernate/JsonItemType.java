package org.asuki.model.hibernate;

import org.asuki.model.jackson.JsonItem;

public class JsonItemType extends BaseUserType<JsonItem> {

    @Override
    protected Class<JsonItem> getClazz() {
        return JsonItem.class;
    }

}
