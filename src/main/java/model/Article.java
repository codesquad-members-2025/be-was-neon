package model;

public class Article {
    private static int idCounter = 0;
    private int id;
    private String content;

    public Article(String content) {
        idCounter++;
        this.id = idCounter;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
