package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerAxis;

public class AxisBinding extends Binding {

    private ControllerAxis axis;
    private TriggerCallback callback;

    private float deadzone;

    public AxisBinding(ControllerAxis axis, TriggerCallback callback, float deadzone, int defaultCooldown) {
        super(defaultCooldown);
        this.axis = axis;
        this.callback = callback;
        this.deadzone = deadzone;
    }

    public AxisBinding(ControllerAxis axis, TriggerCallback callback, float deadzone) {
        this(axis, callback, deadzone, 100);
    }

    public ControllerAxis getAxis() {
        return this.axis;
    }

    public boolean isTriggered(float value) {
        if (this.callback == null) return true;
        return this.callback.doTrigger(value) && Math.abs(value) > this.deadzone;
    }

    interface TriggerCallback {
        boolean doTrigger(float value);
    }
}
