package things;

import cartago.INTERNAL_OPERATION;
import cartago.ObsProperty;
import cartago.tools.GUIArtifact;
import io.vertx.core.json.JsonObject;
import web.WebHelper;

import javax.swing.*;

public class VocalUIThing extends GUIArtifact {

    private static final String HTTP_BASE_URI = "http://localhost:10004/";
    private static final String PROPERTY_USER_POLICY = HTTP_BASE_URI + "properties/userPolicy";
    private static final String ACTION_SET_USER_POLICY = HTTP_BASE_URI + "actions/setUserPolicy";
    private static final String MODE_TEXT = "Current Mode: ";

    public void setup() {
        this.getUserPolicy();

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
        WebHelper.postSendingJsonBody(ACTION_SET_USER_POLICY, new JsonObject().put("userPolicy", status));
        this.getUserPolicy();
    }

    private void getUserPolicy() {
        final ObsProperty userPolicy = getObsProperty("userPolicy");

        final String value = WebHelper.getAsString(PROPERTY_USER_POLICY).orElse("auto");
        if (userPolicy == null)
            this.defineObsProperty("userPolicy", value);
        else
            userPolicy.updateValue(value);
    }

}
