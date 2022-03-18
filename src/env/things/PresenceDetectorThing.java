package things;

import cartago.INTERNAL_OPERATION;
import cartago.ObsProperty;
import cartago.tools.GUIArtifact;

import javax.swing.*;

public class PresenceDetectorThing extends GUIArtifact {

    private JSpinner spinner;

    public void setup() {
        defineObsProperty("detected", 0);

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
        ObsProperty oldDetected = getObsProperty("detected");
        if (detectedPeople < oldDetected.intValue())
            signal("exit");
        else
            signal("entrance");
        oldDetected.updateValue(detectedPeople);
    }

}
