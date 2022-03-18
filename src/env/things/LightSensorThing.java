package things;

import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.tools.GUIArtifact;

import javax.swing.*;

public class LightSensorThing extends GUIArtifact {

    private static final String BRIGHTNESS_LEVEL_PROPERTY = "brightnessLevel";
    private static final String BRIGHTNESS_LABEL_TEXT = "Current brightness in the room: ";
    private static final int INITIAL_BRIGHTNESS = 0;
    private JLabel label;

    public void setup() {
        defineObsProperty(BRIGHTNESS_LEVEL_PROPERTY, INITIAL_BRIGHTNESS);

        JPanel panel = new JPanel();
        this.label = new JLabel(BRIGHTNESS_LABEL_TEXT + INITIAL_BRIGHTNESS);
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
        ObsProperty currentBrightness = getObsProperty(BRIGHTNESS_LEVEL_PROPERTY);
        currentBrightness.updateValue(totalBrightness);
        this.label.setText(BRIGHTNESS_LABEL_TEXT + currentBrightness.intValue());
    }
}
