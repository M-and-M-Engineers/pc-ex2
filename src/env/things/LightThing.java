package things;

import cartago.OPERATION;
import cartago.tools.GUIArtifact;
import web.WebHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LightThing extends GUIArtifact {

    private static final int PORT = 10003;
    private static final String HOST = "localhost";
    private static final String PROPERTY_STATE = "/properties/state";
    private static final String PROPERTY_BRIGHTNESS = "/properties/lightBrightnessLevel";
    private static final String ACTIONS_ON = "/actions/switchOn";
    private static final String ACTIONS_OFF = "/actions/switchOff";
    private static final String ACTIONS_INCREASE_BRIGHTNESS = "/actions/increaseBrightness";
    private static final String ACTIONS_DECREASE_BRIGHTNESS = "/actions/decreaseBrightness";
    private final String uri = "http://" + HOST + ":" + PORT;;

    private static final String STATE_PROPERTY = "state";
    private static final String LIGHT_BRIGHTNESS_PROPERTY = "lightBrightnessLevel";
    private static final String STATE_TEXT = "State: ";
    private static final String LIGHT_BRIGHTNESS_TEXT = "Current light brightness: ";
    private JLabel lightBrightnessLabel;
    private JLabel stateLabel;

    public void setup() {

        final int remoteBrightnessLevel = WebHelper.getAsInteger(this.uri + PROPERTY_BRIGHTNESS).orElse(-1);
        defineObsProperty(LIGHT_BRIGHTNESS_PROPERTY, remoteBrightnessLevel);

        final String remoteState = WebHelper.getAsString(this.uri + PROPERTY_STATE).orElse("");
        defineObsProperty(STATE_PROPERTY, remoteState);

        final JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.stateLabel = new JLabel(STATE_TEXT + remoteState);
        this.stateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.stateLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
        panel.add(this.stateLabel);

        this.lightBrightnessLabel = new JLabel(LIGHT_BRIGHTNESS_TEXT + remoteBrightnessLevel);
        this.lightBrightnessLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.lightBrightnessLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        if ("OFF".equals(remoteState))
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
        WebHelper.emptyPost(this.uri + ACTIONS_ON);
        final String remoteState = WebHelper.getAsString(this.uri + PROPERTY_STATE).orElse("");

        updateObsProperty(STATE_PROPERTY, remoteState);
        this.stateLabel.setText(STATE_TEXT + remoteState);
        this.lightBrightnessLabel.setVisible(true);
    }

    @OPERATION
    void switchOff() {
        WebHelper.emptyPost(this.uri + ACTIONS_OFF);
        final String remoteState = WebHelper.getAsString(this.uri + PROPERTY_STATE).orElse("");

        updateObsProperty(STATE_PROPERTY, remoteState);
        this.stateLabel.setText(STATE_TEXT + remoteState);

        final int remoteBrightnessLevel = WebHelper.getAsInteger(this.uri + PROPERTY_BRIGHTNESS).orElse(-1);
        updateObsProperty(LIGHT_BRIGHTNESS_PROPERTY, remoteBrightnessLevel);
        this.lightBrightnessLabel.setVisible(false);
    }

    @OPERATION
    void increaseBrightness() {
        WebHelper.emptyPost(this.uri + ACTIONS_INCREASE_BRIGHTNESS);

        final int remoteBrightnessLevel = WebHelper.getAsInteger(this.uri + PROPERTY_BRIGHTNESS).orElse(-1);
        updateObsProperty(LIGHT_BRIGHTNESS_PROPERTY, remoteBrightnessLevel);
        this.lightBrightnessLabel.setText(LIGHT_BRIGHTNESS_TEXT + remoteBrightnessLevel);
    }

    @OPERATION
    void decreaseBrightness() {
        WebHelper.emptyPost(this.uri + ACTIONS_DECREASE_BRIGHTNESS);

        final int remoteBrightnessLevel = WebHelper.getAsInteger(this.uri + PROPERTY_BRIGHTNESS).orElse(-1);
        updateObsProperty(LIGHT_BRIGHTNESS_PROPERTY, remoteBrightnessLevel);
        this.lightBrightnessLabel.setText(LIGHT_BRIGHTNESS_TEXT + remoteBrightnessLevel);
    }
}
