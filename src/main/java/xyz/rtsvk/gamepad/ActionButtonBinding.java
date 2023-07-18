package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;

public class ActionButtonBinding extends Binding {

    private ControllerButton btn;
    private Action a;

    public ActionButtonBinding(ControllerButton btn, Action a, int cooldown) {
        super(cooldown);
        this.btn = btn;
        this.a = a;
    }

    public ActionButtonBinding(ControllerButton btn, Action a) {
        this(btn, a, 100);
    }

    public void executeAction(final ControllerIndex gamepad) {
        new Thread(() -> {
            try {
                this.a.handle(gamepad);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        used();
    }

    public ControllerButton getControllerButton() {
        return btn;
    }
}
