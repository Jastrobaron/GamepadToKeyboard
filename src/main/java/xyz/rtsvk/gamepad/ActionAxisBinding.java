package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerIndex;

public class ActionAxisBinding extends AxisBinding {

    private ControllerAxis axis;
    private Action action;
    private TriggerCallback callback;
    private float deadzone;

    public ActionAxisBinding(ControllerAxis axis, Action action, AxisBinding.TriggerCallback callback, float deadzone, int defaultCooldown) {
        super(axis, callback, deadzone, defaultCooldown);
        this.axis = axis;
        this.action = action;
        this.callback = callback;
        this.deadzone = deadzone;
    }

    public ActionAxisBinding(ControllerAxis axis, Action action, TriggerCallback callback, float deadzone) {
        this(axis, action, callback, deadzone, 100);
    }

    public ControllerAxis getAxis() {
        return this.axis;
    }

    public void executeAction(ControllerIndex gamepad) {
        try {
            this.action.handle(gamepad);
            this.used();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
