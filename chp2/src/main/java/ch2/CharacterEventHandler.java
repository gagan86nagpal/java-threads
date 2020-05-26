package ch2;

import java.util.Vector;

/**
 * @author gagandeep.nagpal
 **/
public class CharacterEventHandler {
    private Vector<CharacterListener> listeners = new Vector<>();

    public void addCharacterListener(CharacterListener characterListener) {
        listeners.add(characterListener);
    }

    public void removeCharacterListener(CharacterListener characterListener) {
        listeners.remove(characterListener);
    }

    public void fireNewCharacter(CharacterSource characterSource, int character) {
        final CharacterEvent characterEvent = new CharacterEvent(characterSource, character);
        listeners.forEach(characterListener -> characterListener.newCharacter(characterEvent));

    }
}
