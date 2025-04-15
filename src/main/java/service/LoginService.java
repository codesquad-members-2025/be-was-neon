package service;

import db.Database;
import dto.LoginRequest;
import exception.ClientException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static domain.error.HttpClientError.UNAUTHORIZED;

public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public static User login(LoginRequest request){
        log.info("Login request: " + request.userId());
        User findUser = Database.findUserById(request.userId());
        if(!findUser.getPassword().equals(request.password())||findUser==null){
            log.info("Login failed");
            throw new ClientException(UNAUTHORIZED);
        }
        return findUser;
    }

}
