package pdt;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

public class PresenceDetectorThingModel implements PresenceDetectorThingAPI {

    private int detected;
    private final EventBus eventBus;

    public PresenceDetectorThingModel(final EventBus eventBus) {
        this.detected = 0;
        this.eventBus = eventBus;
    }

    @Override
    public Future<Integer> getDetected() {
        final Promise<Integer> promise = Promise.promise();
        synchronized (this) {
            promise.complete(this.detected);
        }
        return promise.future();
    }

    @Override
    public Future<Void> setDetected(final int detected) {
        final Promise<Void> promise = Promise.promise();
        synchronized (this) {
            if (detected < this.detected)
                this.publishExit();
            else
                this.publishEntrance();
            this.detected = detected;
            promise.complete();
        }
        return promise.future();
    }

    private void publishEntrance() {
        this.eventBus.publish("events/entrance", null);
    }

    private void publishExit() {
        this.eventBus.publish("events/exit", null);
    }

}
