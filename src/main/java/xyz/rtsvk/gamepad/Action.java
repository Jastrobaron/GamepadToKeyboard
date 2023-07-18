package xyz.rtsvk.gamepad;

import com.studiohartman.jamepad.ControllerIndex;

public interface Action {
    void handle(ControllerIndex ctl) throws Exception;
}
