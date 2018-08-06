package TextSearcher;

import java.nio.file.Path;

public class SearchResultFile {
    private final Path path;

    public SearchResultFile(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResultFile that = (SearchResultFile) o;

        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
