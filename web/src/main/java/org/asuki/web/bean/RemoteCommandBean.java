package org.asuki.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Named
@SessionScoped
// @RequestScoped
public class RemoteCommandBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    private List<Item> items;

    public void loadItems(ActionEvent event) {
        if (items != null) {
            return;
        }

        items = loadItems();
    }

    @SneakyThrows
    private List<Item> loadItems() {
        List<Item> items = new ArrayList<>();

        TimeUnit.SECONDS.sleep(3);
        for (int i = 0; i < 10; i++) {
            items.add(new Item("Name" + i));
        }

        return items;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public class Item implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
    }

}
