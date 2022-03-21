package slst;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class SmartLightSensorThingService extends AbstractVerticle {

    private static final String PROPERTY_BRIGHTNESS = "/slst/properties/brightness";
    private final int port;
    private SmartLightSensorThingModel model;

    public SmartLightSensorThingService(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        this.model = new SmartLightSensorThingModel();
        this.startServer(this.createRoutes());
    }

    private Router createRoutes() {
        Router router = Router.router(this.getVertx());
        router.get(PROPERTY_BRIGHTNESS).handler(this::getBrightness);
        return router;
    }

    private void startServer(final Router router) {
        this.getVertx().createHttpServer().requestHandler(router)
                .listen(this.port);
    }

    private void getBrightness(final RoutingContext routingContext) {
        this.model.getBrightnessLevel().onSuccess(brightnessLevel -> routingContext.response().end(String.valueOf(brightnessLevel)));
    }

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SmartLightSensorThingService(Integer.parseInt(args[0])));
    }
}