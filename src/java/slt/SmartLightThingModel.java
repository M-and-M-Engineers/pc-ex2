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
        final Promise<Void> promise = Promise.promise();
        synchronized (this) {
            this.state = State.ON;
            promise.complete();
        }
        return promise.future();
    }

    @Override
    public Future<Void> switchOff() {
        final Promise<Void> promise = Promise.promise();
        synchronized (this) {
            this.state = State.OFF;
            promise.complete();
        }
        return promise.future();
    }

    @Override
    public Future<Void> increaseBrightness() {
        final Promise<Void> promise = Promise.promise();
        synchronized (this) {
            this.brightnessLevel++;
            promise.complete();
        }
        return promise.future();
    }

    @Override
    public Future<Void> decreaseBrightness() {
        final Promise<Void> promise = Promise.promise();
        synchronized (this) {
            this.brightnessLevel--;
            promise.complete();
        }
        return promise.future();
    }
}
