package was.member;

public class Member {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private String password;

    public Member(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
