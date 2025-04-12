package webserver.resolver;

import webserver.http.response.HttpResponse;

import java.io.IOException;

public interface Resolver {

     HttpResponse resolve() throws IOException;

}
