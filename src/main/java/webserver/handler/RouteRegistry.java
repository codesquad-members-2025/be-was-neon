package webserver.handler;

public class RouteRegistry {
    public static void initRoutes() {
        RouteTable.GET.registerHandler("/", new GetRequestHandler());
        RouteTable.GET.registerHandler("/registration", new GetRequestHandler());
        RouteTable.GET.registerHandler("*", new GetRequestHandler());
        RouteTable.POST.registerHandler("/create", new RegistrationHandler());
        RouteTable.POST.registerHandler("/login", new LoginHandler());
        RouteTable.POST.registerHandler("/logout", new LogoutHandler());
    }
}
