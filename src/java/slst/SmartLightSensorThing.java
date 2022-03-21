package slst;

import io.vertx.core.Future;

public interface SmartLightSensorThing {

    Future<Integer> getBrightnessLevel();
}
