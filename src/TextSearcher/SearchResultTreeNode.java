package TextSearcher;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SearchResultTreeNode implements MutableTreeNode {
    private final Path path;
    private final boolean isDirectory;
    private String name;

    private final List<MutableTreeNode> childrenList;
    private MutableTreeNode parent;

    public SearchResultTreeNode() {
        // is root
        this.path = null;
        this.isDirectory = true;
        this.name = "results";
        this.childrenList = new ArrayList<>();
        this.parent = null;
    }

    public SearchResultTreeNode(Path path, String name, SearchResultTreeNode parent) {
        this.path = path;
        this.isDirectory = Files.isDirectory(path);
        this.name = name;
        this.childrenList = isDirectory ? new ArrayList<>() : Collections.emptyList();
        this.parent = parent;
        parent.childrenList.add(this);
    }

    public SearchResultTreeNode(Path path, SearchResultTreeNode parent) {
        this.path = path;
        this.isDirectory = Files.isDirectory(path);
        this.name = path.getFileName().toString();
        this.childrenList = isDirectory ? new ArrayList<>() : Collections.emptyList();
        this.parent = parent;
        parent.childrenList.add(this);
    }

    public SearchResultTreeNode(int index, Path path, String name, SearchResultTreeNode parent) {
        this.path = path;
        this.isDirectory = Files.isDirectory(path);
        this.name = name;
        this.childrenList = isDirectory ? new ArrayList<>() : Collections.emptyList();
        this.parent = parent;
        parent.childrenList.add(index, this);
    }

    public SearchResultTreeNode(int index, Path path, SearchResultTreeNode parent) {
        this.path = path;
        this.isDirectory = Files.isDirectory(path);
        this.name = path.getFileName().toString();
        this.childrenList = isDirectory ? new ArrayList<>() : Collections.emptyList();
        this.parent = parent;
        parent.childrenList.add(index, this);
    }

    public SearchResultTreeNode addResult(Path path) {
        return new SearchResultTreeNode(path, this);
    }

    public SearchResultTreeNode addResult(Path path, String name) {
        return new SearchResultTreeNode(path, name,this);
    }

    public SearchResultTreeNode addResult(int index, Path path) {
        return new SearchResultTreeNode(index, path, this);
    }

    public SearchResultTreeNode addResult(int index, Path path, String name) {
        return new SearchResultTreeNode(index, path, name, this);
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
        if (isDirectory) {
            MutableTreeNode oldParent = (MutableTreeNode)child.getParent();
            if (oldParent != null) {
                oldParent.remove(child);
            }
            child.setParent(this);
            childrenList.add(0, child);
        }
    }

    @Override
    public void remove(int index) {
        MutableTreeNode node = childrenList.get(index);
        childrenList.remove(index);
        node.setParent(null);
    }

    @Override
    public void remove(MutableTreeNode node) {
        childrenList.remove(node);
        node.setParent(null);
    }

    @Override
    public void setUserObject(Object object) {
        name = object.toString();
    }

    public Object getUserObject() {
        return name;
        //DefaultMutableTreeNode
    }

    @Override
    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
        }
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
        parent = newParent;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return childrenList.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return childrenList.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return childrenList.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return isDirectory;
    }

    @Override
    public boolean isLeaf() {
        return !isDirectory;
    }

    @Override
    public Enumeration children() {
        return new Enumeration() {
            private Iterator it = childrenList.iterator();

            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public Object nextElement() {
                return it.next();
            }
        };
    }

    public Iterator<MutableTreeNode> childrenIt() {
        return childrenList.iterator();
    }
}
