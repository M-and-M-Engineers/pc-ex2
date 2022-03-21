package slt;

public enum State {
    ON, OFF;

    @Override
    public String toString() {
        switch (this){
            case ON:
                return "on";
            case OFF:
                return "off";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
