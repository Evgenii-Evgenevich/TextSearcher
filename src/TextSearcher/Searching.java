package TextSearcher;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.function.Predicate;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

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

    private boolean filter(String name) {
        return name.matches("." + fileFilter);
    }

    private void search(Path path) {
        try {
            if (Files.lines(path).anyMatch(line -> line.contains(this.what))) {
                searchNode.addResult(path);
                textSearcher.getSearchResultTree().reload();
            }
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
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (UncheckedIOException e) {
            //e.printStackTrace();
        }
    }

    private boolean zipFileSeacrh(Path path, SearchResultTreeNode treeNode) {
        try (ZipFile zipFile = new ZipFile(path.toFile())) {
            zipFile.stream()
                    .filter(zipEntry -> !zipEntry.isDirectory())
                    .filter(zipEntry -> filter(zipEntry.getName()))
                    .forEach(zipEntry -> {
                        try (InputStream inputStream = zipFile.getInputStream(zipEntry)){
                            CharsetDecoder decoder = StandardCharsets.ISO_8859_1.newDecoder();
                            if ((new BufferedReader(new InputStreamReader(inputStream, decoder)))
                                    .lines()
                                    .anyMatch(line -> line.contains(this.what))) {

                                System.out.println(path.toString() + '/' + zipEntry.getName());
                            }
                        } catch (IOException e) {
                            //e.printStackTrace();
                        } catch (UncheckedIOException e) {
                            //e.printStackTrace();
                        }
                    })
            ;
            return true;
        } catch (ZipException e) {
            return false;
        }
        catch (IOException e) {
            return false;
        } catch (UncheckedIOException e) {
            return false;
        }
    }

    private void fileSearch(Path path, SearchResultTreeNode treeNode) {
        try {
            if (Files.lines(path, StandardCharsets.ISO_8859_1).anyMatch(line -> line.contains(this.what))) {
                SearchResultTreeNode newNode = treeNode.addResult(path);
                textSearcher.getSearchResultTree().reload();
                textSearcher.getSearchResultTree().scrollPathToVisible(newNode);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
            System.out.println("IOException " + path);
        } catch (UncheckedIOException e) {
            System.out.println("UncheckedIOException " + e.getMessage());
            System.out.println("UncheckedIOException " + path);
        }
    }

    private void filesList(Path path, SearchResultTreeNode treeNode) {
        if (Files.isDirectory(path)) {
            directorySearch(path, treeNode);
        } else if (filter(path)) {
            fileSearch(path, treeNode);
        //} else if (zipFileSeacrh(path, treeNode)) {
        }
    }

    private void directorySearch(Path start, SearchResultTreeNode treeNode) {
        try {
            SearchResultTreeNode newNode = treeNode.addResult(start);
            Files
                    .list(start)
                    .forEach(path -> filesList(path, newNode))
            ;
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
            Files
                    .list(directory)
                    .forEach(path -> filesList(path, searchNode))
            ;
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
