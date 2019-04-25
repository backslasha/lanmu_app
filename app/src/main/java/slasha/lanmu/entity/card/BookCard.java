package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime;


public class BookCard implements Serializable {
    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String author;
    @Expose
    private String publisher;
    @Expose
    private LocalDateTime publishDate;
    @Expose
    private String version;
    @Expose
    private String languish;
    @Expose
    private String coverUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Expose
    private String introduction;

    public boolean check() {
        return name != null && name.length() > 0 &&
                author != null && author.length() > 0 &&
                publisher != null && publisher.length() > 0 &&
                publishDate != null &&
                version != null && version.length() > 0 &&
                languish != null && languish.length() > 0 &&
                coverUrl != null && coverUrl.length() > 0;
    }
}
