package pdt;

import io.vertx.core.Future;

public interface PresenceDetectorThingAPI {

    Future<Integer> getDetected();

    Future<Void> setDetected(int detected);
}
