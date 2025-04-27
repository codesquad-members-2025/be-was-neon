package model;

public class Article {
    private final String id;
    private final String title;
    private final String content;
    private final User author;
    private final String createdAt;

    public Article(String id, String title, String content, User author, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
