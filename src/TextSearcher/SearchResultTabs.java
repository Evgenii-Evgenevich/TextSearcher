package TextSearcher;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchResultTabs extends JTabbedPane {
    private final Set<SearchResultFile> resultFiles;
    private final TextSearcher textSearcher;

    public SearchResultTabs(TextSearcher textSearcher) {
        resultFiles = new HashSet<>();
        this.textSearcher = textSearcher;
    }

    public void open(Path path) {
        if (!resultFiles.contains(path)) {
            SearchResultFile tab = new SearchResultFile(path, this);
            resultFiles.add(tab);
            this.addTab(path.getFileName().toString(), tab);
        }
    }

    public void close(Path path) {
        if (resultFiles.remove(path)) {
            this.removeTabAt(this.indexOfTab(path.getFileName().toString()));
        }
    }

    public void close(SearchResultFile tab) {
        if (resultFiles.remove(tab)) {
            this.remove(tab);
        }
    }

    public SearchResultFile getCurrentTab() {
        return (SearchResultFile) this.getSelectedComponent();
    }
}
