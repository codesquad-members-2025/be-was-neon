package model;

public class Article {
    private final String title;
    private final String content;
    private final User author;
    private final Long createdAt;

    public Article(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = System.currentTimeMillis();
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

    public Long getCreatedAt() {
        return createdAt;
    }
}
