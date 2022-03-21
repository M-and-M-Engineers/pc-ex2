package vuit;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

public class VocalUIThingModel implements VocalUIThingAPI {

    private String userPolicy;
    private final EventBus eventBus;

    public VocalUIThingModel(final EventBus eventBus) {
        this.userPolicy = "auto";
        this.eventBus = eventBus;
    }

    @Override
    public Future<String> getUserPolicy() {
        final Promise<String> promise = Promise.promise();
        synchronized (this) {
            promise.complete(this.userPolicy);
        }
        return promise.future();
    }

    @Override
    public Future<Void> setUserPolicy(final String userPolicy) {
        final Promise<Void> promise = Promise.promise();
        synchronized (this) {
            this.userPolicy = userPolicy;
            this.publishNewUserPolicy();
            promise.complete();
        }
        return promise.future();
    }

    private void publishNewUserPolicy() {
        this.eventBus.publish("events/newUserPolicy", this.userPolicy);
    }

}
