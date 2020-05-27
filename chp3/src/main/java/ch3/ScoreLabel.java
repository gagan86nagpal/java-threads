package ch3;

import ch2.CharacterEvent;
import ch2.CharacterListener;
import ch2.CharacterSource;

import javax.swing.*;

/**
 * @author gagandeep.nagpal
 **/
public class ScoreLabel extends JLabel implements CharacterListener {
    private CharacterSource typist;
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

    /**
     * setter of generator
     */
    public synchronized void resetGenerator(CharacterSource generator_) {
        if (null != generator) {
            generator.removeCharacterListener(this);
        }
        generator = generator_;
        if (null != generator) {
            generator.addCharacterListener(this);
        }
    }

    /**
     * setter of typist
     */

    public synchronized void resetTypist(CharacterSource typist_) {
        if (null != typist) {
            typist.removeCharacterListener(this);
        }
        typist = typist_;
        if (null != typist) {
            typist.addCharacterListener(this);
        }
    }

    /**
     * called at initialisation of app
     */
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
        // is user hasn't typed previous character
        if (ce.characterSource == generator) {
            if (char2Type != -1) {
                score--;
                setScore();
            }
            char2Type = ce.character;

        } else {
            // if user has typed, check it against the current generator.
            // if matched, then unset the point.
            // is char2Type is -1, that means user has typed at least an extra character to that of generator
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
