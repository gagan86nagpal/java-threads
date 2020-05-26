package ch2;

/**
 * @author gagandeep.nagpal
 **/

/**
 * source for a character. Could be a thread producing random characters or could be listetning events and producing it.
 *
 */
public interface CharacterSource {
    void addCharacterListener(CharacterListener characterListener);

    void removeCharacterListener(CharacterListener characterListener);

    /**
     * Either throws a runtime exception or always sends an event to listerens
     */
    void nextCharacter();
}
