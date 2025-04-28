package model;

public class Article {
    private int id;
    private String title;
    private String content;
    private User author;
    private String imageUrl;

    public Article(String title, String content, User author, String imageUrl) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    public Article(int id, String title, String content, User author,String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "/img/default.jpeg"; // 기본 이미지 경로
        }
        return imageUrl;
    }

    public Article withId(int id) {
       return new Article(id, title, content, author, imageUrl);
    }
}
