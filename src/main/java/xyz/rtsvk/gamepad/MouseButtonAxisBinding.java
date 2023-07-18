package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerAxis;

public class MouseButtonAxisBinding extends KeyAxisBinding {
    public MouseButtonAxisBinding(ControllerAxis axis, int key, TriggerCallback callback, float deadzone, int defaultCooldown) {
        super(axis, key, callback, deadzone, defaultCooldown);
    }

    public MouseButtonAxisBinding(ControllerAxis axis, int key, TriggerCallback callback, float deadzone) {
        super(axis, key, callback, deadzone, 100);
    }
}
