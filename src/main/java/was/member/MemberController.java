package was.member;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;
import java.util.List;
import static util.MyLog.log;

public class MemberController {
    private final MemberRepository memberRepository;
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Mapping("/")
    public void home(HttpResponse response) {
        String str = "<html><body>" +
                "<h1>Member Manager</h1>" +
                "<ul>" +
                "<li><a href='/members'>Member List</a></li>" +
                "<li><a href='/create-member'>Add New Member</a></li>" +
                "</ul>" +
                "</body></html>";
        response.writeBody(str);
    }

    @Mapping("/index")
    public void index(HttpResponse response) {
        response.writeBody("<h1>Index world!</h1>");
    }

    @Mapping("/members")
    public void members(HttpResponse response) {
        List<Member> members = memberRepository.findAll();
        StringBuilder str = new StringBuilder();
        str.append("<html><body>");
        str.append("<h1>Member List</h1>");
        str.append("<ul>");
        for (Member member : members) {
            str.append("<li>")
                    .append("ID: ").append(member.getUserId())
                    .append(", Name: ").append(member.getName())
                    .append(", email: ").append(member.getEmail())
            .append("</li>");
        }
        str.append("</ul>");
        str.append("<a href='/'>Back to Home</a>");
        str.append("</body></html>");
        response.writeBody(str.toString());
    }

    @Mapping("/create-member")
    public void createMember(HttpResponse response) {
        String body = "<html><body>" +
                "<h1>회원가입!!</h1>" +
                "<form action='/create' method='post'>" +
                "ID: <input type='text' name='userId'><br>" +
                "PW: <input type='text' name='password'><br>" +
                "Name: <input type='text' name='name'><br>" +
                "Email: <input type='text' name='email'><br>" +
                "<input type='submit' value='Add'>" +
                "</form>" +
                "<a href='/'>Back to Home</a>" +
                "</body></html>";
        response.writeBody(body);
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

        response.writeBody("<h1>save ok</h1>");
        response.writeBody("<a href='/'>Back to Home</a>");
    }
}
