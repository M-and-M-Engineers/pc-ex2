package things;

import cartago.INTERNAL_OPERATION;
import cartago.ObsProperty;
import cartago.tools.GUIArtifact;
import io.vertx.core.json.JsonObject;
import web.WebHelper;

import javax.swing.*;

public class PresenceDetectorThing extends GUIArtifact {

    private static final String HTTP_BASE_URI = "http://localhost:10001/";
    private static final String PROPERTY_DETECTED = HTTP_BASE_URI + "properties/detected";
    private static final String ACTION_SET_DETECTED = HTTP_BASE_URI + "actions/setDetected";
    private static final String WS_BASE_URI = "ws://localhost:10001/";
    private static final String ENTRANCE = WS_BASE_URI + "events/entrance";
    private static final String EXIT = WS_BASE_URI + "events/exit";
    private JSpinner spinner;

    public void setup() {
        this.getDetected();
        this.subscribeToEntrance();
        this.subscribeToExit();

        JPanel panel = new JPanel();

        panel.add(new JLabel("Number of people in the room"));

        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 5, 1);
        this.spinner = new JSpinner(model);
        this.spinner.addChangeListener(e -> execInternalOp("setDetected", this.spinner.getValue()));
        panel.add(this.spinner);

        JFrame frame = new JFrame("Presence Detector");
        frame.setSize(300, 75);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @INTERNAL_OPERATION
    void setDetected(int detectedPeople) {
        WebHelper.postSendingJsonBody(ACTION_SET_DETECTED, new JsonObject().put("detected", detectedPeople));
        this.getDetected();
    }

    @INTERNAL_OPERATION
    void entrance() {
        signal("entrance");
    }

    @INTERNAL_OPERATION
    void exit() {
        signal("exit");
    }

    private void getDetected() {
        final ObsProperty detected = getObsProperty("detected");

        final int value = WebHelper.getAsInteger(PROPERTY_DETECTED).orElse(0);
        if (detected == null)
            this.defineObsProperty("detected", value);
        else
            detected.updateValue(value);
    }

    private void subscribeToEntrance() {
        WebHelper.subscribeOn(ENTRANCE, unused -> execInternalOp("entrance"));
    }

    private void subscribeToExit() {
        WebHelper.subscribeOn(EXIT, unused -> execInternalOp("exit"));
    }

}
