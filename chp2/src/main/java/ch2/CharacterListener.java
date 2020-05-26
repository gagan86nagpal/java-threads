package ch2;

/**
 * @author gagandeep.nagpal
 **/

/**
 * Listeners only has a single method. We can add this listener to a source.
 * Whenever a character is generated from source. It calls the {@link #newCharacter } method of all added listeners
 */
public interface CharacterListener {
    void newCharacter(CharacterEvent ce);
}
