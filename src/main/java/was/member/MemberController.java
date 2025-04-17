package was.member;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.PageNotFoundException;
import was.httpserver.servlet.annotation.Mapping;

import java.io.IOException;
import java.io.InputStream;
import static util.MyLog.log;

public class MemberController {
    private final MemberRepository memberRepository;
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Mapping("/article")
    public void article(HttpResponse response) throws IOException {
    }

    @Mapping("/comment")
    public void comment (HttpResponse response) throws IOException {
    }

    @Mapping("/login")
    public void login(HttpResponse response) throws IOException {
    }

    @Mapping("/main")
    public void main(HttpResponse response) throws IOException {
    }

    @Mapping("/registration")
    public void createMember(HttpResponse response) throws IOException {
    }

    @Mapping("/create")
    public void create(HttpRequest request, HttpResponse response) {
        log("MemberController request = " + request);

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        Member member = new Member(userId, password, name, email);
        memberRepository.save(member);

        response.setStatus(302);
        response.addHeader("Location", "/main");
    }

    private static void connectPage(HttpResponse response, InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new PageNotFoundException("Page not found: " + inputStream);
        }
        byte[] content = inputStream.readAllBytes();
        response.setStatus(200);
        response.setContentType("text/html; charset=UTF-8");
        response.writeBody(content);
    }
}
