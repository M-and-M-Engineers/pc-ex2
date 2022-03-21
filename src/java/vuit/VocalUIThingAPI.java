package vuit;

import io.vertx.core.Future;

public interface VocalUIThingAPI {

    Future<String> getUserPolicy();

    Future<Void> setUserPolicy(String userPolicy);
}
