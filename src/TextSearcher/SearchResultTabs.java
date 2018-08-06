package TextSearcher;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchResultTabs {
    private Set<SearchResultFile> resultFiles;

    public SearchResultTabs() {
        resultFiles = new LinkedHashSet<>();
    }

    public void open(Path path) {
        if (!resultFiles.contains(path)) {
            resultFiles.add(new SearchResultFile(path));
        }
    }

    public void close(Path path) {
        resultFiles.remove(path);
    }
}
