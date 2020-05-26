package ch2;

/**
 * @author gagandeep.nagpal
 **/
public class CharacterEvent {
    public CharacterSource characterSource;
    public int character;

    public CharacterEvent(CharacterSource characterSource, int character) {
        this.characterSource = characterSource;
        this.character = character;
    }
}
