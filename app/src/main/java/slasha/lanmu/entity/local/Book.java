
package slasha.lanmu.entity.local;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Book implements Serializable {
    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String author;
    @Expose
    private String publishDate;
    @Expose
    private String publisher;
    @Expose
    private String version;
    @Expose
    private String languish;
    @Expose
    private String coverUrl;
    @Expose
    private String introduction;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "{" + "\"id\":" +
                id +
                ",\"name\":\"" +
                name + '\"' +
                ",\"author\":\"" +
                author + '\"' +
                ",\"publishDate\":\"" +
                publishDate + '\"' +
                ",\"publisher\":\"" +
                publisher + '\"' +
                ",\"version\":\"" +
                version + '\"' +
                ",\"languish\":\"" +
                languish + '\"' +
                ",\"coverUrl\":\"" +
                coverUrl + '\"' +
                ",\"introduction\":\"" +
                introduction + '\"' +
                '}';
    }
}
