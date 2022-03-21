package slst;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public class SmartLightSensorThingModel implements SmartLightSensorThing {

    private int brightnessLevel;

    @Override
    public Future<Integer> getBrightnessLevel() {
        final Promise<Integer> promise = Promise.promise();
        synchronized (this) {
            promise.complete(this.brightnessLevel);
        }
        return promise.future();
    }
}
