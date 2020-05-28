package chp4;

import ch2.CharacterDisplayCanvas;
import ch2.CharacterEvent;
import ch2.CharacterListener;

import java.awt.*;

/**
 * @author gagandeep.nagpal
 **/
public class AnimatedCharacterDisplayCanvas extends CharacterDisplayCanvas
        implements CharacterListener, Runnable {
    private boolean done = false;
    private int curX = 0;
    private Thread timer;

    public AnimatedCharacterDisplayCanvas() {
    }

    public synchronized void newCharacter(CharacterEvent ce) {
        curX = 0;
        tmpChar[0] = (char) ce.character;
        repaint();
    }

    protected synchronized void paintComponent(Graphics gc) {
        Dimension d = getSize();
        gc.clearRect(0, 0, d.width, d.height);
        if (tmpChar[0] == 0)
            return;
        fm.charWidth(tmpChar[0]);
        gc.drawChars(tmpChar, 0, 1,
                curX++, fontHeight);
    }

    public synchronized void run() {
        while (true) {
            try {
                if (done) {
                    wait();
                } else {
                    repaint();
                    wait(100);
                }
            } catch (InterruptedException ie) {
                return;
            }
        }
    }

    public synchronized void setDone(boolean b) {
        done = b;
        if (timer == null) {
            timer = new Thread(this);
            timer.start();
        }
        if (!done)
            notify();
    }


}

