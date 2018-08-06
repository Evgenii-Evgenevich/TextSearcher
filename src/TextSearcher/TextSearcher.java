package TextSearcher;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextSearcher {
    static final FileSystem fileSystem = FileSystems.getDefault();
    static final String homePath = System.getProperty("user.home");
    static final public Path homeDirectory = Paths.get(homePath);

    private final SearchResultTree searchResultTree = new SearchResultTree();

    private final SearchResultTabs searchResultTabs = new SearchResultTabs();

    Thread temp = null;

    public void startSearch(Path directory, String fileFilter, String what) {
        SearchResultTreeNode node = searchResultTree.addResultTree(directory, fileFilter, what);

        Searching searching = new Searching(directory, fileFilter, what, node);

        temp = new Thread(searching);

        temp.start();
    }

    public TextSearcher() {
        startSearch(homeDirectory, "*.log", "xml");
    }

    public static void main(String[] args) {
        TextSearcher textSearcher = new TextSearcher();

        try {
            textSearcher.temp.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
