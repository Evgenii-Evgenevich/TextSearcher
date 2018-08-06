package TextSearcher;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SearchResultFile extends JScrollPane {
    private final Path path;
    private final SearchResultTabs tabbedPane;

    //private final JTable text = new JTable();
    private final JTextArea text = new JTextArea();

    private class CloseListener extends MouseInputAdapter {
        private final SearchResultFile component;

        public CloseListener(SearchResultFile component) {
            this.component = component;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            component.close();
        }
    }

    public final MouseInputAdapter mouseCloseListener = new CloseListener(this);

    public SearchResultFile(Path path, SearchResultTabs tabbedPane) {
        super();
        this.path = path;
        this.tabbedPane = tabbedPane;
        System.out.println(path);

        this.setViewportView(text);
        text.setEditable(false);
        read();
    }

    private void read() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            bufferedReader.lines().forEachOrdered(s -> text.append(s + '\n'));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return path;
    }

    public void close() {
        tabbedPane.close(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (getClass() == o.getClass()) {
            SearchResultFile that = (SearchResultFile) o;
            return path.equals(that.path);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
