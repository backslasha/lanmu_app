package slasha.lanmu.bean;

public class Book {
    private String name;
    private String author;
    private String publishDate;
    private String version;
    private String languish;

    public Book(String name, String author, String publishDate, String version, String languish) {
        this.name = name;
        this.author = author;
        this.publishDate = publishDate;
        this.version = version;
        this.languish = languish;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", version='" + version + '\'' +
                ", languish='" + languish + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguish() {
        return languish;
    }

    public void setLanguish(String languish) {
        this.languish = languish;
    }
}
