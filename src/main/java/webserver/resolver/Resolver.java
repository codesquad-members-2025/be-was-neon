package webserver.resolver;

import java.io.IOException;

public interface Resolver {

    ResolveResponse<?> resolve() throws IOException;

}
