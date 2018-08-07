package TextSearcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class TextSearcher extends JFrame {
    static final FileSystem fileSystem = FileSystems.getDefault();
    static final String homePath = System.getProperty("user.home");
    static final public Path homeDirectory = Paths.get(homePath);

    private final SearchPanel searchPanel = new SearchPanel("", "*.log", homePath);

    private final SearchResultTree searchResultTree = new SearchResultTree(this);

    private final SearchResultTabs searchResultTabs = new SearchResultTabs(this);

    //Thread temp = null;

    private final Set<Searching> searchings = new HashSet<>();

    public void startSearch(Path directory, String fileFilter, String what) {
        SearchResultTreeNode node = searchResultTree.addResultTree(directory, fileFilter, what);

        Searching searching = new Searching(directory, fileFilter, what, node, this);

        if (searchings.add(searching)) {
            Thread temp = new Thread(searching);

            temp.start();
        }
    }

    private final ActionListener searchButtonActionListener = actionEvent -> {
        Path directory = Paths.get(searchPanel.getWhereValue());
        String filter = searchPanel.getFilterValue();
        String what = searchPanel.getWhatValue();
        startSearch(directory, filter, what);
    };

    private final ActionListener nextButtonActionListener = actionEvent -> {
    };

    private final ActionListener previousButtonActionListener = actionEvent -> {
    };

    private final ActionListener selectAllButtonActionListener = actionEvent -> {
    };

    private final ActionListener closeTabButtonActionListener = actionEvent -> {
        searchResultTabs.getCurrentTab().close();
    };

    public TextSearcher() {
        super("TextSearcher");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640, 480);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel buttons = new JPanel(); buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(new Button("Search", searchButtonActionListener));
        buttons.add(new Button("Next", nextButtonActionListener));
        buttons.add(new Button("Previous", previousButtonActionListener));
        buttons.add(new Button("Select All", selectAllButtonActionListener));
        buttons.add(new Button("Close Tab", closeTabButtonActionListener));

        JPanel result = new JPanel(new GridLayout(1, 2));
        result.add(searchResultTree);
        result.add(searchResultTabs);

        panel.add(searchPanel);
        panel.add(buttons);
        panel.add(result);

        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    public SearchResultTabs getSearchResultTabs() {
        return searchResultTabs;
    }

    public SearchResultTree getSearchResultTree() {
        return searchResultTree;
    }

    public static void main(String[] args) {
        TextSearcher textSearcher = new TextSearcher();

        //try {
        //    textSearcher.startSearch(homeDirectory, "*.log", "xml");
        //    textSearcher.temp.join();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }
}
