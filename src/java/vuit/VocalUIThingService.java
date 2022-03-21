package vuit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VocalUIThingService extends AbstractVerticle {

    private static final String PROPERTY_USER_POLICY = "/properties/userPolicy";
    private static final String ACTION_SET_USER_POLICY = "/actions/setUserPolicy";
    private static final String NEW_USER_POLICY = "/events/newUserPolicy";
    private final List<ServerWebSocket> newUserPolicySubscribers;
    private final int port;
    private VocalUIThingModel model;

    public VocalUIThingService(int port) {
        this.port = port;
        this.newUserPolicySubscribers = new ArrayList<>();
    }

    @Override
    public void start() {
        this.model = new VocalUIThingModel(this.getVertx().eventBus());
        this.createSockets();
        this.startServer(this.createRoutes());
    }

    private Router createRoutes() {
        Router router = Router.router(this.getVertx());
        router.get(PROPERTY_USER_POLICY).handler(this::getUserPolicy);
        router.post(ACTION_SET_USER_POLICY).handler(this::setUserPolicy);
        return router;
    }

    private void createSockets() {
        final EventBus eventBus = this.getVertx().eventBus();
        eventBus.consumer("events/newUserPolicy", jsonObject -> {
            Iterator<ServerWebSocket> iterator = this.newUserPolicySubscribers.iterator();
            while (iterator.hasNext()){
                ServerWebSocket socket = iterator.next();
                if (socket.isClosed())
                    iterator.remove();
                else {
                    try {
                        socket.write(((JsonObject) jsonObject.body()).toBuffer());
                    } catch (Exception exception) {
                        iterator.remove();
                    }
                }
            }
        });
    }

    private void startServer(final Router router) {
        this.getVertx().createHttpServer().webSocketHandler(handler -> {
            if (handler.path().equals(NEW_USER_POLICY))
                this.newUserPolicySubscribers.add(handler);
            else
                handler.reject();
        })
        .requestHandler(router)
        .listen(this.port);
    }

    private void getUserPolicy(final RoutingContext routingContext) {
       this.model.getUserPolicy().onSuccess(policy -> routingContext.response().end(policy));
    }

    private void setUserPolicy(final RoutingContext routingContext) {
        this.model.setUserPolicy(routingContext.getBodyAsString())
                .onSuccess(unused -> routingContext.response().end());
    }

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VocalUIThingService(Integer.parseInt(args[0])));
    }
}
