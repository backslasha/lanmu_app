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
}
