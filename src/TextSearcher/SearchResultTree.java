package TextSearcher;

import java.nio.file.Path;

public class SearchResultTree {
    private final SearchResultTreeNode root = new SearchResultTreeNode();

    public SearchResultTreeNode addResultTree(Path directory, String filter, String what) {
        String name = "Search \"" + what + "\" in \"" + directory.toString() + "\" with filter(\"" + filter + "\")";
        return root.insertResult(0, directory, name);
    }
}
