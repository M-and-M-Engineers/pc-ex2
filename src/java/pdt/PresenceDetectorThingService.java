package pdt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PresenceDetectorThingService extends AbstractVerticle {

    private static final String PROPERTY_DETECTED = "/properties/detected";
    private static final String ACTION_SET_DETECTED = "/actions/setDetected";
    private static final String EVENTS = "/events/";
    private static final String ENTRANCE = EVENTS + "entrance";
    private static final String EXIT = EVENTS + "exit";
    private final List<ServerWebSocket> entranceSubscribers;
    private final List<ServerWebSocket> exitSubscribers;
    private final int port;
    private PresenceDetectorThingModel model;

    public PresenceDetectorThingService(int port) {
        this.port = port;
        this.entranceSubscribers = new ArrayList<>();
        this.exitSubscribers = new ArrayList<>();
    }

    @Override
    public void start() {
        this.model = new PresenceDetectorThingModel(this.getVertx().eventBus());
        this.createSockets();
        this.startServer(this.createRoutes());
    }

    private Router createRoutes() {
        Router router = Router.router(this.getVertx());
        router.get(PROPERTY_DETECTED).handler(this::getDetected);
        router.post(ACTION_SET_DETECTED).handler(BodyHandler.create()).handler(this::setDetected);
        return router;
    }

    private void createSockets() {
        final EventBus eventBus = this.getVertx().eventBus();
        eventBus.consumer("events/entrance", jsonObject -> {
            Iterator<ServerWebSocket> iterator = this.entranceSubscribers.iterator();
            while (iterator.hasNext()){
                ServerWebSocket socket = iterator.next();
                if (socket.isClosed())
                    iterator.remove();
                else {
                    try {
                        socket.writeTextMessage("");
                    } catch (Exception exception) {
                        iterator.remove();
                    }
                }
            }
        });
        eventBus.consumer("events/exit", jsonObject -> {
            Iterator<ServerWebSocket> iterator = this.exitSubscribers.iterator();
            while (iterator.hasNext()){
                ServerWebSocket socket = iterator.next();
                if (socket.isClosed())
                    iterator.remove();
                else {
                    try {
                        socket.writeTextMessage("");
                    } catch (Exception exception) {
                        iterator.remove();
                    }
                }
            }
        });
    }

    private void startServer(final Router router) {
        this.getVertx().createHttpServer(new HttpServerOptions().setWebSocketSubProtocols(List.of("websub"))).webSocketHandler(handler -> {
            if (handler.path().equals(ENTRANCE))
                this.entranceSubscribers.add(handler);
            else if (handler.path().equals(EXIT))
                this.exitSubscribers.add(handler);
            else
                handler.reject();
        })
        .requestHandler(router)
        .listen(this.port);
    }

    private void getDetected(final RoutingContext routingContext) {
       this.model.getDetected().onSuccess(detected -> routingContext.response().end(String.valueOf(detected)));
    }

    private void setDetected(final RoutingContext routingContext) {
        this.model.setDetected(routingContext.getBodyAsJson().getLong("detected").intValue())
                .onSuccess(unused -> routingContext.response().end());
    }

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new PresenceDetectorThingService(Integer.parseInt(args[0])));
    }
}
