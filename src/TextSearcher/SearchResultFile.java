package TextSearcher;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SearchResultFile extends JScrollPane implements Runnable {
    private final Path path;
    private final SearchResultTabs tabbedPane;

    private final List<Integer> lineLengths = new ArrayList<>();

    private int currentPos = -1;

    //private final JTable text = new JTable();
    private final JTextArea text = new JTextArea();

    private final Highlighter highlighter = text.getHighlighter();
    private final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

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

        this.setViewportView(text);
        text.setEditable(false);

        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Files
                    .lines(path, StandardCharsets.ISO_8859_1)
                    .forEachOrdered(s -> {
                        text.append(s + '\n');
                        lineLengths.add(s.length() + 1);
                    })
            ;
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
            System.out.println("IOException " + path);
        } catch (UncheckedIOException e) {
            System.out.println("UncheckedIOException " + e.getMessage());
            System.out.println("UncheckedIOException " + path);
        }
    }

    public String next(int start, String what) {
        int searched = -1;
        return null;
    }

    public void selectNext(int start, String what) {
        Runnable selecting = () -> {
            int searched = text.getText().indexOf(what, start);
            int end = searched + what.length();

            try {
                highlighter.removeAllHighlights();
                highlighter.addHighlight(searched, end, painter);
            } catch (BadLocationException e) {
                return;
            }
        };

        new Thread(selecting).start();
    }

    public void selectPrevious(int start, String what) {
        Runnable selecting = () -> {
            int searched = text.getText().lastIndexOf(what, start);
            int end = searched + what.length();

            try {
                highlighter.removeAllHighlights();
                highlighter.addHighlight(searched, end, painter);
            } catch (BadLocationException e) {
                return;
            }
        };

        new Thread(selecting).start();
    }

    public void selectAll(String what) {
        Runnable selecting = () -> {
            int start = 0;
            int length = text.getDocument().getLength();

            //for (Integer length : lineLengths) {
                try {
                    String line = text.getText(start, length);

                    int searched = line.indexOf(what, 0);
                    int end = searched + what.length();

                    while (searched != -1) {
                        try {
                            highlighter.addHighlight(start + searched, start + end, painter);
                        } catch (BadLocationException e) {
                            return;
                        }

                        searched = line.indexOf(what, end);
                        end = searched + what.length();
                    }
                } catch (BadLocationException e) {
                    return;
                }

                //start += length;
            //}
        };

        new Thread(selecting).start();
        currentPos = 0;
    }

    public void selectNext(String what) {
        Runnable selecting = () -> {
            int searched = text.getText().indexOf(what, currentPos);
            if (searched == currentPos) {
                searched = text.getText().indexOf(what, currentPos + 1);
            }
            int end = searched + what.length();
            try {
                highlighter.removeAllHighlights();
                highlighter.addHighlight(searched, end, painter);
            } catch (BadLocationException e) {
                return;
            }
            currentPos = searched;
        };

        new Thread(selecting).start();
    }

    public void selectPrevious(String what) {
        Runnable selecting = () -> {
            int searched = text.getText().lastIndexOf(what, currentPos);
            if (searched == currentPos) {
                searched = text.getText().lastIndexOf(what, currentPos - 1);
            }
            int end = searched + what.length();
            try {
                highlighter.removeAllHighlights();
                highlighter.addHighlight(searched, end, painter);
            } catch (BadLocationException e) {
                return;
            }
            currentPos = searched;
        };

        new Thread(selecting).start();
    }

    public Path getPath() {
        return path;
    }

    public void close() {
        tabbedPane.close(this);
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
        return path.hashCode();
    }
}
