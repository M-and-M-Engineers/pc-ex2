package vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

public class VertxInstance {
    public static final Vertx VERTX = Vertx.vertx();
    public static final WebClient WEB_CLIENT = WebClient.create(VERTX);
}
