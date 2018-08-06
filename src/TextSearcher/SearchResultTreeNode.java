package TextSearcher;

import java.nio.file.Files;
import java.nio.file.Path;

public class SearchResultTreeNode {
    private final Path path;

    public SearchResultTreeNode() {
        // is root
        this.path = null;
    }

    public SearchResultTreeNode(String name, boolean isDirectory, SearchResultTreeNode parent) {
        this.path = null;
    }

    public SearchResultTreeNode(Path path, String name, SearchResultTreeNode parent) {
        this.path = path;
        boolean isDirectory = Files.isDirectory(path);
    }

    public SearchResultTreeNode(Path path, SearchResultTreeNode parent) {
        this.path = path;
        String name = path.getFileName().toString();
        boolean isDirectory = Files.isDirectory(path);
    }

    public SearchResultTreeNode addResult(Path path) {
        System.out.println(path);
        return new SearchResultTreeNode(path, this);
    }

    public SearchResultTreeNode insertResult(int index, String name, boolean isDirectory) {
        return new SearchResultTreeNode(name, isDirectory, this);
    }

    public SearchResultTreeNode insertResult(int index, Path path, String name) {
        return new SearchResultTreeNode(path, name, this);
    }

    public Path getPath() {
        return path;
    }
}
