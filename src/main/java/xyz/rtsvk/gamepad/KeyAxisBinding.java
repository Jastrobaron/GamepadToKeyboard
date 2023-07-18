package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerAxis;

public class KeyAxisBinding extends AxisBinding {

    private int key;
    private boolean pressed;

    public KeyAxisBinding(ControllerAxis axis, int key, TriggerCallback callback, float deadzone, int defaultCooldown) {
        super(axis, callback, deadzone, defaultCooldown);
        this.key = key;
    }

    public KeyAxisBinding(ControllerAxis axis, int key, TriggerCallback callback, float deadzone) {
        this(axis, key, callback, deadzone, 100);
    }


    public int getKey() {
        return this.key;
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
