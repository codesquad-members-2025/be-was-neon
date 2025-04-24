package model;

public class Article {
    private static int idCounter = 0;
    private int id;
    private String title;
    private String content;
    private User author;

    public Article(String title, String content, User author) {
        idCounter++;
        this.id = idCounter;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }
}
