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
    private JLabel label;

    public void setup() {
        JPanel panel = new JPanel();

        this.label = new JLabel();
        panel.add(label);

        this.getUserPolicy();

        JButton switchOnButton = new JButton("Switch On");
        switchOnButton.addActionListener(e -> execInternalOp("setUserPolicy", "Always ON"));
        panel.add(switchOnButton);

        JButton switchOffButton = new JButton("Switch Off");
        switchOffButton.addActionListener(e -> execInternalOp("setUserPolicy", "Always OFF"));
        panel.add(switchOffButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> execInternalOp("setUserPolicy", "Automatic"));
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

        final String value = WebHelper.getAsString(PROPERTY_USER_POLICY).orElse("Automatic");
        if (userPolicy == null)
            this.defineObsProperty("userPolicy", value);
        else
            userPolicy.updateValue(value);

       this.label.setText(MODE_TEXT + value);
    }

}
