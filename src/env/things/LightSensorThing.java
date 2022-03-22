package things;

import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.tools.GUIArtifact;
import io.vertx.core.json.JsonObject;
import web.WebHelper;

import javax.swing.*;

public class LightSensorThing extends GUIArtifact {

    private static final int PORT = 10002;
    private static final String HOST = "localhost";
    private static final String PROPERTY_BRIGHTNESS = "/properties/brightnessLevel";
    private static final String ACTION_SET_BRIGHTNESS = "/actions/setBrightnessLevel";
    private final String uri = "http://" + HOST + ":" + PORT;

    private static final String BRIGHTNESS_LEVEL_PROPERTY = "brightnessLevel";
    private static final String BRIGHTNESS_LABEL_TEXT = "Current brightness in the room: ";
    private JLabel label;

    public void setup() {

        final int remoteBrightness = WebHelper.getAsInteger(this.uri + PROPERTY_BRIGHTNESS).orElse(-1);
        defineObsProperty(BRIGHTNESS_LEVEL_PROPERTY, remoteBrightness);

        JPanel panel = new JPanel();
        this.label = new JLabel(BRIGHTNESS_LABEL_TEXT + remoteBrightness);
        panel.add(this.label);

        JFrame frame = new JFrame("Smart Light Sensor");
        frame.setSize(300, 75);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @OPERATION
    void setBrightnessLevel(int totalBrightness) {
        final JsonObject brightnessJson = new JsonObject();
        brightnessJson.put(BRIGHTNESS_LEVEL_PROPERTY, totalBrightness);
        WebHelper.postSendingJsonBody(this.uri + ACTION_SET_BRIGHTNESS, brightnessJson);

        final int remoteBrightness = WebHelper.getAsInteger(this.uri + PROPERTY_BRIGHTNESS).orElse(-1);
        updateObsProperty(BRIGHTNESS_LEVEL_PROPERTY, remoteBrightness);
        this.label.setText(BRIGHTNESS_LABEL_TEXT + remoteBrightness);
    }


}
