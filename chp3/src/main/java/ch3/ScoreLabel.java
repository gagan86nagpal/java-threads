import ch2.CharacterEvent;
import ch2.CharacterListener;
import ch2.CharacterSource;

import javax.swing.*;

/**
 * @author gagandeep.nagpal
 **/
public class ScoreLabel extends JLabel implements CharacterListener {
    private final CharacterSource typist;
    private int score = 0;
    private int char2Type = -1;
    private CharacterSource generator;

    public ScoreLabel(CharacterSource generator, CharacterSource typist) {
        this.generator = generator;
        this.typist = typist;
        if (null != generator) {
            generator.addCharacterListener(this);
        }
        if (null != typist) {
            typist.addCharacterListener(this);
        }
    }

    public ScoreLabel() {
        this(null, null);
    }

    public synchronized void resetGenerator(CharacterSource generator_) {
        if (null != generator) {
            generator.removeCharacterListener(this);
        }
        generator = generator_;
        if (null != generator) {
            generator.addCharacterListener(this);
        }
    }

    public synchronized void resetTypist(CharacterSource typist_) {
        if (null != typist) {
            typist.removeCharacterListener(this);
        }
        generator = typist_;
        if (null != typist) {
            typist.addCharacterListener(this);
        }
    }

    public synchronized void resetScore() {
        score = 0;
        char2Type = -1;
        setScore();
    }

    private synchronized void setScore() {
        SwingUtilities.invokeLater(() -> {
            setText(Integer.toString(score));
        });
    }

    @Override
    public synchronized void newCharacter(CharacterEvent ce) {
        // previous character check (not typed)
        if (ce.characterSource == generator) {
            if (char2Type != -1) {
                score--;
                setScore();
            }
            char2Type = ce.character;

        } else {
            // If character is extraneous: 1-point penalty // If character does not match: 1-point penalty
            if (char2Type != ce.character) {
                score--;
            } else {
                score++;
                char2Type = -1;
            }
            setScore();
        }
    }
}
