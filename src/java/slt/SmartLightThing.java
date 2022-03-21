package slt;

import io.vertx.core.Future;

public interface SmartLightThing {

    Future<String> getState();

    Future<Integer> getBrightnessLevel();

    Future<Void> switchOn();

    Future<Void> switchOff();

    Future<Void> increaseBrightness();

    Future<Void> decreaseBrightness();
}
