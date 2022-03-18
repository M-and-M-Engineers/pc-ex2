package simulators;

import cartago.INTERNAL_OPERATION;
import cartago.tools.GUIArtifact;

import javax.swing.*;

public class ExternalLightSimulator extends GUIArtifact {

    private static final String EXTERNAL_BRIGHTNESS_LEVEL_PROPERTY = "externalBrightnessLevel";
    private static final String EXTERNAL_BRIGHTNESS_LABEL_TEXT = "External Brightness Level: ";
    private static final int EXTERNAL_INITIAL_LIGHT = 0;
    private JSlider slider;
    private JLabel label;

    @Override
    public void setup() {
        defineObsProperty(EXTERNAL_BRIGHTNESS_LEVEL_PROPERTY, EXTERNAL_INITIAL_LIGHT);

        JPanel panel = new JPanel();
        this.slider = new JSlider(0, 70, EXTERNAL_INITIAL_LIGHT);
//        this.slider.setLabelTable(this.slider.createStandardLabels(1));
        panel.add(this.slider);

        this.slider.addChangeListener(e -> execInternalOp("setExternalBrightnessLevel"));
        this.label = new JLabel(EXTERNAL_BRIGHTNESS_LABEL_TEXT + this.slider.getValue());
        panel.add(this.label);

        JFrame frame = new JFrame("External Light Simulator");
        frame.setSize(350, 100);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @INTERNAL_OPERATION
    void setExternalBrightnessLevel() {
        final int externalBrightnessLevel = this.slider.getValue();
        updateObsProperty(EXTERNAL_BRIGHTNESS_LEVEL_PROPERTY, externalBrightnessLevel);
        this.label.setText(EXTERNAL_BRIGHTNESS_LABEL_TEXT + externalBrightnessLevel);
    }
}
