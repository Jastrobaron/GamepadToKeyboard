package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.*;
import xyz.rtsvk.gamepad.actions.PlaySoundAction;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) throws AWTException, ControllerUnpluggedException {
        Robot robot = new Robot();

        ControllerManager controllers = new ControllerManager();
        controllers.initSDLGamepad();

        ControllerIndex currController = controllers.getControllerIndex(0);
        BindingManager manager = new BindingManager(controllers);

        manager.bindButtonToKey(ControllerButton.A, KeyEvent.VK_SPACE);
        manager.bindButtonToKey(ControllerButton.B, KeyEvent.VK_R);
        manager.bindButtonToKey(ControllerButton.X, KeyEvent.VK_SHIFT);
        manager.bindButtonToKey(ControllerButton.Y, KeyEvent.VK_E);
        manager.registerButtonListener(ControllerButton.RIGHTBUMPER, e -> robot.mouseWheel(1));
        manager.registerButtonListener(ControllerButton.LEFTBUMPER, e -> robot.mouseWheel(-1));
        manager.registerButtonListener(ControllerButton.START, new PlaySoundAction("metalpipe.wav"), 200);

        // mouse
        manager.bindAxisToAction(ControllerAxis.RIGHTX, e -> {
            Point location = MouseInfo.getPointerInfo().getLocation();
            int dx = (int) (e.getAxisState(ControllerAxis.RIGHTX) * 10);
            robot.mouseMove(location.x + dx, location.y);
        }, null, 0.1f, 10);

        manager.bindAxisToAction(ControllerAxis.RIGHTY, e -> {
            Point location = MouseInfo.getPointerInfo().getLocation();
            int dy = (int) (e.getAxisState(ControllerAxis.RIGHTY) * 10);
            robot.mouseMove(location.x, location.y - dy);
        }, null, 0.1f, 10);

        manager.bindAxisToMouseButton(ControllerAxis.TRIGGERRIGHT, InputEvent.BUTTON1_DOWN_MASK, v -> v > 0.5f, 0, 10);
        manager.bindAxisToMouseButton(ControllerAxis.TRIGGERLEFT, InputEvent.BUTTON3_DOWN_MASK, v -> v > 0.5f, 0, 10);

        // W A S D
        manager.bindAxisToKey(ControllerAxis.LEFTY, KeyEvent.VK_W, v -> v > 0.5, 0.1f, 100);
        manager.bindAxisToKey(ControllerAxis.LEFTY, KeyEvent.VK_S, v -> v < -0.5, 0.1f, 100);
        manager.bindAxisToKey(ControllerAxis.LEFTX, KeyEvent.VK_D, v -> v > 0.5, 0.1f, 100);
        manager.bindAxisToKey(ControllerAxis.LEFTX, KeyEvent.VK_A, v -> v < -0.5, 0.1f, 100);

        manager.start();

        while(manager.isRunning()) {
            if(currController.isButtonPressed(ControllerButton.BACK) || controllers.getNumControllers() == 0) {
                break;
            }
        }

        manager.stop();
        manager.clearAllBindings();
        controllers.quitSDLGamepad();
    }
}