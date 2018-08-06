package TextSearcher;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class Searching implements Runnable {
    private final Path directory;
    private final String fileFilter;
    private final String what;
    private final SearchResultTreeNode searchNode;
    private final TextSearcher textSearcher;

    public Searching(Path directory, String fileFilter, String what, SearchResultTreeNode node, TextSearcher textSearcher) {
        this.directory = directory;
        this.fileFilter = fileFilter;
        this.what = what;
        this.searchNode = node;
        this.textSearcher = textSearcher;
    }

    private boolean filter(Path path) {
        final PathMatcher pathMatcher = TextSearcher.fileSystem.getPathMatcher("glob:*" + fileFilter);
        return pathMatcher.matches(path);
    }

    private void search(Path path) {
        try {
            if (Files.lines(path).anyMatch(line -> line.contains(this.what))) {
                searchNode.addResult(path);
                System.out.println(path);
                textSearcher.getSearchResultTree().reload();
            }
        } catch (FileSystemException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (UncheckedIOException e) {
            //e.printStackTrace();
        }
    }

    private void filesWalk() {
        try {
            Files
                    .find(directory, Integer.MAX_VALUE, (path, basicFileAttributes) -> !basicFileAttributes.isDirectory() && this.filter(path))
                    //.walk(directory, Integer.MAX_VALUE).filter(this::filter).filter(path -> !Files.isDirectory(path))
                    .forEach(this::search)
            ;
        } catch (FileSystemException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (UncheckedIOException e) {
            //e.printStackTrace();
        }
    }

    private void fileSearch(Path path, SearchResultTreeNode treeNode) {
        try {
            if (Files.lines(path).anyMatch(line -> line.contains(this.what))) {
                treeNode.addResult(path);
                System.out.println(path);
                textSearcher.getSearchResultTree().reload();
            }
        } catch (FileSystemException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (UncheckedIOException e) {
            //e.printStackTrace();
        }
    }

    private void directorySearch(Path start, SearchResultTreeNode treeNode) {
        try {
            SearchResultTreeNode newNode = treeNode.addResult(start);
            Files.list(start).forEach(path -> {
                if (Files.isDirectory(path)) {
                    directorySearch(path, newNode);
                } else if (filter(path)) {
                    fileSearch(path, newNode);
                }
            });
        } catch (FileSystemException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (UncheckedIOException e) {
            //e.printStackTrace();
        }
    }

    public void run1() {
        filesWalk();
    }

    public void run() {
        try {
            Files.list(directory).forEach(path -> {
                if (Files.isDirectory(path)) {
                    directorySearch(path, searchNode);
                } else if (filter(path)) {
                    fileSearch(path, searchNode);
                }
            });
        } catch (FileSystemException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (UncheckedIOException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Searching searching = (Searching) o;

        if (!directory.equals(searching.directory)) return false;
        if (fileFilter != null ? !fileFilter.equals(searching.fileFilter) : searching.fileFilter != null) return false;
        return what.equals(searching.what);
    }

    @Override
    public int hashCode() {
        int result = directory.hashCode();
        result = 31 * result + (fileFilter != null ? fileFilter.hashCode() : 0);
        result = 31 * result + what.hashCode();
        return result;
    }
}
