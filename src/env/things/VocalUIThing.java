package things;

import cartago.INTERNAL_OPERATION;
import cartago.tools.GUIArtifact;

import javax.swing.*;

public class VocalUIThing extends GUIArtifact {

    private static final String MODE_TEXT = "Current Mode: ";

    public void setup() {
        defineObsProperty("userPolicy", "auto");

        JPanel panel = new JPanel();

        JLabel label = new JLabel(MODE_TEXT + "Automatic");
        panel.add(label);

        JButton switchOnButton = new JButton("Switch On");
        switchOnButton.addActionListener(e -> {
            label.setText(MODE_TEXT + "Always ON");
            execInternalOp("setUserPolicy", "alwaysOn");
        });
        panel.add(switchOnButton);

        JButton switchOffButton = new JButton("Switch Off");
        switchOffButton.addActionListener(e -> {
            label.setText(MODE_TEXT + "Always OFF");
            execInternalOp("setUserPolicy", "alwaysOff");
        });
        panel.add(switchOffButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            label.setText(MODE_TEXT + "Automatic");
            execInternalOp("setUserPolicy", "auto");
        });
        panel.add(resetButton);

        JFrame frame = new JFrame("User Commands Simulator");
        frame.setSize(450, 75);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @INTERNAL_OPERATION
    void setUserPolicy(final String status) {
        updateObsProperty("userPolicy", status);
    }

}
