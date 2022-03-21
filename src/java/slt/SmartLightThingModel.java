package slt;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public class SmartLightThingModel implements SmartLightThing {

    private State state;
    private int brightnessLevel;

    public SmartLightThingModel() {
        this.state = State.OFF;
    }

    @Override
    public Future<Integer> getBrightnessLevel() {
        final Promise<Integer> promise = Promise.promise();
        synchronized (this) {
            promise.complete(this.brightnessLevel);
        }
        return promise.future();
    }

    @Override
    public Future<String> getState() {
        final Promise<String> promise = Promise.promise();
        synchronized (this) {
            promise.complete(this.state.toString());
        }
        return promise.future();
    }

    @Override
    public Future<Void> switchOn() {
        this.state = State.ON;
        return null;
    }

    @Override
    public Future<Void> switchOff() {
        this.state = State.OFF;
        return null;
    }

    @Override
    public Future<Void> increaseBrightness() {
        this.brightnessLevel++;
        return null;
    }

    @Override
    public Future<Void> decreaseBrightness() {
        this.brightnessLevel--;
        return null;
    }
}
