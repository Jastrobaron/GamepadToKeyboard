package xyz.rtsvk.gamepad.actions;

import com.studiohartman.jamepad.ControllerIndex;
import xyz.rtsvk.gamepad.Action;

import javax.sound.sampled.*;
import java.io.File;

public class PlaySoundAction implements Action {

    private File soundFile;

    public PlaySoundAction(String file) {
        this.soundFile = new File(file);
    }

    @Override
    public void handle(ControllerIndex ctl) throws Exception {
        // play the sound file
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.soundFile);
        AudioFormat fmt = audioIn.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(fmt);
        line.start();

        byte[] bufferBytes = new byte[2048];
        int readBytes = -1;
        while ((readBytes = audioIn.read(bufferBytes)) != -1) {
            line.write(bufferBytes, 0, readBytes);
        }

        line.drain();
        line.close();
        audioIn.close();
    }
}
