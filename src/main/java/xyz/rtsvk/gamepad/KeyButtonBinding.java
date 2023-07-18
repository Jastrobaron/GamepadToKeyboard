package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerButton;

public class KeyButtonBinding extends Binding {

    private ControllerButton btn;
    private int key;

    private boolean pressed;

    public KeyButtonBinding(ControllerButton btn, int key, int cooldown) {
        super(cooldown);
        this.btn = btn;
        this.key = key;
        this.pressed = false;
    }

    public KeyButtonBinding(ControllerButton btn, int key)  {
        this(btn, key, 100);
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public ControllerButton getControllerButton() {
        return btn;
    }

    public int getKey() {
        return key;
    }
}
