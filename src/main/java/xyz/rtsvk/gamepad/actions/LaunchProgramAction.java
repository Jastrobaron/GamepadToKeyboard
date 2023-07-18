package xyz.rtsvk.gamepad.actions;

import com.studiohartman.jamepad.ControllerIndex;
import xyz.rtsvk.gamepad.Action;

public class LaunchProgramAction implements Action {

    private String path;

    public LaunchProgramAction(String path) {
        this.path = path;
    }

    @Override
    public void handle(ControllerIndex ctl) throws Exception {
        //Process proc = Runtime.getRuntime().exec(this.path);
        System.out.println("program launched");
    }
}
