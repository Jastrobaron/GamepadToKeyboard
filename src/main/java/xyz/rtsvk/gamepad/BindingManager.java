package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BindingManager {

    private List<Binding> binds;
    private Thread worker;
    private ControllerManager mgr;

    private int controllerIndex;
    private int minimumCooldown;
    private boolean running;

    private static long lastMeasure;

    public BindingManager(ControllerManager mgr) {
        this.binds = new ArrayList<>();
        this.mgr = mgr;
        this.running = true;
        this.controllerIndex = 0;
        this.minimumCooldown = 50;
    }

    public void start() {
        this.worker = new Thread(() -> {
            try {
                final ControllerIndex gamepad = mgr.getControllerIndex(this.controllerIndex);
                final Robot robot = new Robot();

                while (mgr.getNumControllers() > 0 && this.running) {
                    mgr.update();

                    for (int i = 0; i < this.binds.size() && this.running; i++) {
                        Binding e = this.binds.get(i);

                        if (e instanceof KeyButtonBinding) {
                            KeyButtonBinding bind = (KeyButtonBinding) e;

                            if (e.isAvailable()) {
                                if (gamepad.isButtonPressed(bind.getControllerButton())) {
                                    robot.keyPress(bind.getKey());
                                    bind.setPressed(true);
                                }

                                else if (bind.isPressed()) {
                                    robot.keyRelease(bind.getKey());
                                    bind.setPressed(false);
                                }

                                int cooldown = e.getDefaultCooldown();
                                e.lockFor(cooldown > 0 ? cooldown : this.minimumCooldown);   // cooldown
                            }
                        }

                        else if (e instanceof ActionButtonBinding) {
                            ActionButtonBinding bind = (ActionButtonBinding) e;

                            if (gamepad.isButtonPressed(bind.getControllerButton()) && bind.isAvailable()) {
                                bind.executeAction(gamepad);
                                int cooldown = e.getDefaultCooldown();
                                e.lockFor(cooldown > 0 ? cooldown : this.minimumCooldown);   // cooldown
                            }
                        }

                        else if (e instanceof ActionAxisBinding) {
                            ActionAxisBinding bind = (ActionAxisBinding) e;
                            float value = gamepad.getAxisState(bind.getAxis());

                            if (bind.isTriggered(value) && bind.isAvailable()) {
                                bind.executeAction(gamepad);
                                int cooldown = e.getDefaultCooldown();
                                e.lockFor(cooldown > 0 ? cooldown : this.minimumCooldown);   // cooldown
                            }
                        }

                        else if (e instanceof MouseButtonAxisBinding) {
                            MouseButtonAxisBinding bind = (MouseButtonAxisBinding) e;
                            float value = gamepad.getAxisState(bind.getAxis());

                            if (e.isAvailable()) {
                                if (bind.isTriggered(value)) {
                                    if (!bind.isPressed()) {
                                        robot.mousePress(bind.getKey());
                                        bind.setPressed(true);
                                    }
                                }
                                else if (bind.isPressed()) {
                                    robot.mouseRelease(bind.getKey());
                                    bind.setPressed(false);
                                }

                                int cooldown = e.getDefaultCooldown();
                                e.lockFor(cooldown > 0 ? cooldown : this.minimumCooldown);   // cooldown
                            }
                        }

                        else if (e instanceof KeyAxisBinding) {
                            KeyAxisBinding bind  = (KeyAxisBinding) e;
                            float value = gamepad.getAxisState(bind.getAxis());

                            if (e.isAvailable()) {
                                if (bind.isTriggered(value)) {
                                    robot.keyPress(bind.getKey());
                                    bind.setPressed(true);
                                }
                                else if (bind.isPressed()) {
                                    robot.keyRelease(bind.getKey());
                                    bind.setPressed(false);
                                }

                                int cooldown = e.getDefaultCooldown();
                                e.lockFor(cooldown > 0 ? cooldown : this.minimumCooldown);   // cooldown
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                this.stop();
                throw new RuntimeException(ex);
            }
        });
        this.worker.start();
    }


    public void stop() {
        this.running = false;
    }

    public void setControllerIndex(int index) {
        this.stop();
        this.controllerIndex = index;
        this.start();
    }

    public void setMinimumCooldown(int ms) {
        this.minimumCooldown = ms;
    }

    public void clearAllBindings() {
        this.binds.clear();
    }

    public void registerButtonListener(ControllerButton btn, Action a) {
        this.binds.add(new ActionButtonBinding(btn, a));
    }

    public void registerButtonListener(ControllerButton btn, Action a, int cooldown) {
        this.binds.add(new ActionButtonBinding(btn, a, cooldown));
    }

    public void bindButtonToKey(ControllerButton btn, int key) {
        this.binds.add(new KeyButtonBinding(btn, key));
    }

    public void unbindButton(ControllerButton btn) {
        for (int i = 0; i < this.binds.size(); i++) {
            if (!(this.binds.get(i) instanceof KeyButtonBinding bind)) continue;

            if (bind.getControllerButton() == btn) {
                this.binds.remove(bind);
                break;
            }
        }
    }


    public void bindAxisToAction(ControllerAxis axis, Action action, ActionAxisBinding.TriggerCallback callback, float deadzone, int cooldown) {
        this.binds.add(new ActionAxisBinding(axis, action, callback, deadzone, cooldown));
    }

    public void bindAxisToAction(ControllerAxis axis, Action action, ActionAxisBinding.TriggerCallback callback, float deadzone) {
        this.binds.add(new ActionAxisBinding(axis, action, callback, deadzone));
    }

    public void bindAxisToKey(ControllerAxis axis, int key, KeyAxisBinding.TriggerCallback callback, float deadzone, int cooldown) {
        this.binds.add(new KeyAxisBinding(axis, key, callback, deadzone, cooldown));
    }

    public void bindAxisToKey(ControllerAxis axis, int key, KeyAxisBinding.TriggerCallback callback, float deadzone) {
        this.binds.add(new KeyAxisBinding(axis, key, callback, deadzone));
    }

    public void bindAxisToMouseButton(ControllerAxis axis, int key, KeyAxisBinding.TriggerCallback callback, float deadzone, int cooldown) {
        this.binds.add(new MouseButtonAxisBinding(axis, key, callback, deadzone, cooldown));
    }

    public void bindAxisToMouseButton(ControllerAxis axis, int key, KeyAxisBinding.TriggerCallback callback, float deadzone) {
        this.binds.add(new MouseButtonAxisBinding(axis, key, callback, deadzone));
    }

    public static float deltaTime() {
        long now = System.currentTimeMillis();
        float dt = now - lastMeasure;
        lastMeasure = now;
        return dt;
    }

    public boolean isRunning() {
        return this.running;
    }
}
