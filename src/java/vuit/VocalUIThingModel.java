package vuit;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public class VocalUIThingModel implements VocalUIThingAPI {

    private String userPolicy;

    public VocalUIThingModel() {
        this.userPolicy = "auto";
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
            promise.complete();
        }
        return promise.future();
    }

}
