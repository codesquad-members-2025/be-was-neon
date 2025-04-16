package was;

import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.StaticResourceServlet;
import was.httpserver.servlet.annotation.AnnotationServlet;
import was.member.MemberController;
import was.member.MemberRepository;

import java.io.IOException;
import java.util.List;

public class WebServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        MemberRepository memberRepository = new MemberRepository();
        MemberController memberController = new MemberController(memberRepository);
        List<Object> controllers = List.of(memberController);
        HttpServlet servlet = new AnnotationServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.add("/favicon", new DiscardServlet());
        servletManager.add("/static", new StaticResourceServlet(getStaticPath()));

        servletManager.setDefaultServlet(servlet);
        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }

    private static String getStaticPath() {
        return System.getProperty("user.dir") + "/src/main/resources/static";
    }
}
