package ch2;

import java.util.Arrays;
import java.util.Random;

/**
 * @author gagandeep.nagpal
 **/

/**
 * It keeps sending events to all the attached listeners.
 */
public class RandomCharacterGenerator extends Thread implements CharacterSource {

    static char[] chars;
    static String charArray = "abcdefghijklmnopqrstuvwxyz0123456789";

    static {
        chars = charArray.toCharArray();
    }

    Random random;
    CharacterEventHandler handler;

    public RandomCharacterGenerator() {
        random = new Random();
        handler = new CharacterEventHandler();
    }

    public int getPauseTime() {
        return (int) (Math.max(1000, 5000 * random.nextDouble()));
    }

    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListener(cl);
    }

    public void removeCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    @Override
    public void nextCharacter() {
        handler.fireNewCharacter(this, chars[random.nextInt(chars.length)]);
    }

    @SuppressWarnings("BusyWait")
    public void run() {
        while(!isInterrupted()) {
            nextCharacter();
            try {
                Thread.sleep(getPauseTime());
            } catch (InterruptedException ie) {
                return;
            }
        }
    }
}
