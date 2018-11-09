package com.edu.edumediaplayer.fileselection;

import java.io.IOException;
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

    public String[] getFiles(String path) throws IOException {
        if (path.charAt(0)=='/') {
            return root.getFiles(path.split("/",2)[1]);
        } else {
            return root.getFiles(path);
        }
    }

    public String[] getRootFiles() {
        return root.getOwnFiles();
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

        public String[] getOwnFiles() {
            return children.keySet().toArray(new String[children.size()]);
        }

        public String[] getFiles(String path) throws IOException {
            String[] parts = path.split("/", 2);
            if (path.equals("")) {
                return getOwnFiles();
            } else {
                if (children.containsKey(parts[0])) {
                    if (parts.length>1) {
                        return children.get(parts[0]).getFiles(parts[1]);
                    } else {
                        return children.get(parts[0]).getFiles("");
                    }
                } else {
                    throw new IOException("Path not stored in this File System Tree");
                }
            }
        }
    }

}
