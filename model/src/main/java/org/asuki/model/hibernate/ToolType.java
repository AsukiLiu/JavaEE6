package org.asuki.model.hibernate;

import org.asuki.model.jackson.Tool;

public class ToolType extends BaseUserType<Tool> {

    @Override
    public Class<Tool> returnedClass() {
        return Tool.class;
    }

}
