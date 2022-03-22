package vuit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class VocalUIThingService extends AbstractVerticle {

    private static final String PROPERTY_USER_POLICY = "/properties/userPolicy";
    private static final String ACTION_SET_USER_POLICY = "/actions/setUserPolicy";
    private final int port;
    private VocalUIThingModel model;

    public VocalUIThingService(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        this.model = new VocalUIThingModel();
        this.startServer(this.createRoutes());
    }

    private Router createRoutes() {
        Router router = Router.router(this.getVertx());
        router.get(PROPERTY_USER_POLICY).handler(this::getUserPolicy);
        router.post(ACTION_SET_USER_POLICY).handler(BodyHandler.create()).handler(this::setUserPolicy);
        return router;
    }

    private void startServer(final Router router) {
        this.getVertx().createHttpServer()
                .requestHandler(router)
                .listen(this.port);
    }

    private void getUserPolicy(final RoutingContext routingContext) {
       this.model.getUserPolicy().onSuccess(policy -> routingContext.response().end(policy));
    }

    private void setUserPolicy(final RoutingContext routingContext) {
        this.model.setUserPolicy(routingContext.getBodyAsJson().getString("userPolicy"))
                .onSuccess(unused -> routingContext.response().end());
    }

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VocalUIThingService(Integer.parseInt(args[0])));
    }
}
