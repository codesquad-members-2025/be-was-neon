package model;

public class Article {
    private int id;
    private String title;
    private String content;
    private User author;

    public Article(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Article(int id, String title, String content, User author) {
        this.id = id;
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

    public Article withId(int id) {
       return new Article(id, title, content, author);
    }
}
