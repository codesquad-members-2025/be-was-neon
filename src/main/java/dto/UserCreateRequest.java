package dto;

public record UserCreateRequest(String userId, String password, String name, String email) {

}