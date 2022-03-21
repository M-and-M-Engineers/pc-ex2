package slt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class SmartLightThingService extends AbstractVerticle {

    private static final int SERVICE_UNAVAILABLE = 503;
    private static final String PROPERTY_STATUS = "/slt/properties/state";
    private static final String PROPERTY_BRIGHTNESS = "/slt/properties/brightness";
    private static final String ACTIONS_ON = "/slt/actions/switchOn";
    private static final String ACTIONS_OFF = "/slt/actions/switchOff";
    private static final String ACTIONS_INCREASE_BRIGHTNESS = "/slt/actions/increaseBrightness";
    private static final String ACTIONS_DECREASE_BRIGHTNESS = "/slt/actions/decreaseBrightness";
    private final int port;
    private SmartLightThingModel model;

    public SmartLightThingService(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        this.model = new SmartLightThingModel();
        this.startServer(this.createRoutes());
    }

    private Router createRoutes() {
        Router router = Router.router(this.getVertx());
        router.get(PROPERTY_STATUS).handler(this::getState);
        router.get(PROPERTY_BRIGHTNESS).handler(this::getBrightness);
        router.post(ACTIONS_ON).handler(this::switchOn);
        router.post(ACTIONS_OFF).handler(this::switchOff);
        router.post(ACTIONS_INCREASE_BRIGHTNESS).handler(this::increaseBrightness);
        router.post(ACTIONS_DECREASE_BRIGHTNESS).handler(this::decreaseBrightness);
        return router;
    }

    private void startServer(final Router router) {
        this.getVertx().createHttpServer().requestHandler(router)
                .listen(this.port);
    }

    private void getState(final RoutingContext routingContext) {
        this.model.getState().onSuccess(state -> routingContext.response().end(state));
    }

    private void getBrightness(final RoutingContext routingContext) {
        this.model.getBrightnessLevel().onSuccess(brightnessLevel -> routingContext.response().end(String.valueOf(brightnessLevel)));
    }

    private void switchOn(RoutingContext routingContext) {
        routingContext.request().bodyHandler(b -> {
            this.model.switchOn();
        });
    }

    private void switchOff(RoutingContext routingContext) {
        routingContext.request().bodyHandler(b -> {
            this.model.switchOff();
        });
    }

    private void increaseBrightness(RoutingContext routingContext) {
        routingContext.request().bodyHandler(b -> {
            this.model.increaseBrightness();
        });
    }

    private void decreaseBrightness(RoutingContext routingContext) {
        routingContext.request().bodyHandler(b -> {
            this.model.decreaseBrightness();
        });
    }

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SmartLightThingService(Integer.parseInt(args[0])));
    }
}
