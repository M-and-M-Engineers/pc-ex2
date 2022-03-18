package things;

import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.tools.GUIArtifact;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LightThing extends GUIArtifact {

    private enum LightStatus {
        ON, OFF
    }

    private static final String STATUS_PROPERTY = "status";
    private static final String LIGHT_BRIGHTNESS_PROPERTY = "lightBrightnessLevel";
    private static final String INITIAL_STATUS = LightStatus.OFF.toString();
    private static final int RESET_LIGHT_BRIGHTNESS = 0;
    private static final String STATUS_TEXT = "Status: ";
    private static final String LIGHT_BRIGHTNESS_TEXT = "Current light brightness: ";
    private JLabel lightBrightnessLabel;
    private JLabel statusLabel;

    public void setup() {
        defineObsProperty(LIGHT_BRIGHTNESS_PROPERTY, RESET_LIGHT_BRIGHTNESS);
        defineObsProperty(STATUS_PROPERTY, INITIAL_STATUS);

        final JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.statusLabel = new JLabel(STATUS_TEXT + INITIAL_STATUS);
        this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.statusLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
        panel.add(this.statusLabel);

        this.lightBrightnessLabel = new JLabel(LIGHT_BRIGHTNESS_TEXT + RESET_LIGHT_BRIGHTNESS);
        this.lightBrightnessLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.lightBrightnessLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.lightBrightnessLabel.setVisible(false);
        panel.add(this.lightBrightnessLabel);

        final JFrame frame = new JFrame("Light Thing");
        frame.setSize(250, 100);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @OPERATION
    void switchOn() {
        final ObsProperty status = getObsProperty(STATUS_PROPERTY);
        status.updateValue(LightStatus.ON.toString());
        this.statusLabel.setText(STATUS_TEXT + status.stringValue());
        this.lightBrightnessLabel.setVisible(true);
    }

    @OPERATION
    void switchOff() {
        final ObsProperty status = getObsProperty(STATUS_PROPERTY);
        status.updateValue(LightStatus.OFF.toString());
        this.statusLabel.setText(STATUS_TEXT + status.stringValue());
        updateObsProperty(LIGHT_BRIGHTNESS_PROPERTY, RESET_LIGHT_BRIGHTNESS);
        this.lightBrightnessLabel.setVisible(false);
    }

    @OPERATION
    void increaseBrightness() {
        final ObsProperty brightness = getObsProperty(LIGHT_BRIGHTNESS_PROPERTY);
        brightness.updateValue(brightness.intValue() + 1);
        this.lightBrightnessLabel.setText(LIGHT_BRIGHTNESS_TEXT + brightness.intValue());
    }

    @OPERATION
    void decreaseBrightness() {
        final ObsProperty brightness = getObsProperty(LIGHT_BRIGHTNESS_PROPERTY);
        brightness.updateValue(brightness.intValue() - 1);
        this.lightBrightnessLabel.setText(LIGHT_BRIGHTNESS_TEXT + brightness.intValue());
    }
}
