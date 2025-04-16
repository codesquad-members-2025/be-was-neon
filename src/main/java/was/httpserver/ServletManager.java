package was.httpserver;

import was.httpserver.servlet.InternalErrorServlet;
import was.httpserver.servlet.NotFoundServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;
    private HttpServlet notFoundErrorServlet =  new NotFoundServlet();
    private HttpServlet internalErrorServlet =  new InternalErrorServlet();

    public void add(String path, HttpServlet servlet) {
        servletMap.put(path, servlet);
    }

    public void setDefaultServlet(HttpServlet defaultServlet) {
        this.defaultServlet = defaultServlet;
    }
    public void setNotFoundErrorServlet(HttpServlet notFoundErrorServlet) {
        this.notFoundErrorServlet = notFoundErrorServlet;
    }
    public void setInternalErrorServlet(HttpServlet internalErrorServlet) {
        this.internalErrorServlet = internalErrorServlet;
    }

    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        try {
            String requestPath = request.getPath();
            HttpServlet servlet = servletMap.get(requestPath);

            if (servlet == null) {
                for (Map.Entry<String, HttpServlet> entry : servletMap.entrySet()) {
                    String prefix = entry.getKey();
                    if (requestPath.startsWith(prefix)) {
                        servlet = entry.getValue();
                        break;
                    }
                }
            }

            if (servlet == null) {
                servlet = defaultServlet;
            }

            if (servlet == null) {
                throw new PageNotFoundException("request url= " + requestPath);
            }

            servlet.service(request, response);

        } catch (PageNotFoundException e) {
            e.printStackTrace();
            notFoundErrorServlet.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            internalErrorServlet.service(request, response);
        }
    }
}
