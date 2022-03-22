package slt;

public enum State {
    ON, OFF;

    @Override
    public String toString() {
        switch (this){
            case ON:
                return "ON";
            case OFF:
                return "OFF";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
