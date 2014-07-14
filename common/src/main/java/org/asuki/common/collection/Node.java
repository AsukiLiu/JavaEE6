package org.asuki.common.collection;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Node {

    private String name;

    private List<Node> children = new ArrayList<>();

    @Setter
    @Getter
    private Node parent;

    public Node(String name) {
        this.name = name;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    public String toString() {
        return name;
    }
}