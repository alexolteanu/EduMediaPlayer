package com.edu.edumediaplayer.fileselection;

import java.util.HashMap;
import java.util.Map;

public class FileSystemTree {

    Node root;

    public FileSystemTree() {
        root = new Node("");
    }

    public void addFile(String filePath) {
        if (filePath.charAt(0)=='/') {
            root.addFile(filePath.split("/",2)[1]);
        } else {
            root.addFile(filePath);
        }
    }

    public class Node {
        private String name;
        private Map<String,Node> children = new HashMap<>();
        private Node parent;

        public Node(String name) {
            this.name = name;
        }

        public void addFile(String filePath) {
            String[] parts = filePath.split("/", 2);
            if (!children.containsKey(parts[0])) {
                Node newNode = new Node(parts[0]);
                newNode.parent = this;
                children.put(parts[0], newNode);
            }
            if (parts.length>1)
                children.get(parts[0]).addFile(parts[1]);
        }
    }

}

