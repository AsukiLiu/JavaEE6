package org.asuki.model.hibernate;

import org.asuki.model.jackson.Tool;

public class ToolType extends BaseUserType<Tool> {

    @Override
    protected Class<Tool> getClazz() {
        return Tool.class;
    }

}
