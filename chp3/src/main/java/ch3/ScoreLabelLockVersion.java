package ch3;

import ch2.CharacterEvent;
import ch2.CharacterListener;
import ch2.CharacterSource;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gagandeep.nagpal
 **/
public class ScoreLabelLockVersion extends JLabel implements CharacterListener {
    // custom lock
    private final Lock scoreLock = new ReentrantLock();
    private CharacterSource typist;
    private int score = 0;
    private int char2Type = -1;
    private CharacterSource generator;

    public ScoreLabelLockVersion(CharacterSource generator, CharacterSource typist) {
        this.generator = generator;
        this.typist = typist;
        if (null != generator) {
            generator.addCharacterListener(this);
        }
        if (null != typist) {
            typist.addCharacterListener(this);
        }
    }

    public ScoreLabelLockVersion() {
        this(null, null);
    }

    /**
     * setter of generator
     */
    public void resetGenerator(CharacterSource generator_) {
        try {
            scoreLock.lock();
            if (null != generator) {
                generator.removeCharacterListener(this);
            }
            generator = generator_;
            if (null != generator) {
                generator.addCharacterListener(this);
            }
        } finally {
            scoreLock.unlock();
        }
    }

    /**
     * setter of typist
     */

    public void resetTypist(CharacterSource typist_) {
        try {
            scoreLock.lock();
            if (null != typist) {
                typist.removeCharacterListener(this);
            }
            typist = typist_;
            if (null != typist) {
                typist.addCharacterListener(this);
            }
        } finally {
            scoreLock.unlock();
        }
    }

    /**
     * called at initialisation of app
     */
    public void resetScore() {
        try {
            scoreLock.lock();
            score = 0;
            char2Type = -1;
            setScore();
        } finally {
            scoreLock.unlock();
        }
    }

    private void setScore() {
        try {
            scoreLock.lock();
            SwingUtilities.invokeLater(() -> setText(Integer.toString(score)));
        } finally {
            scoreLock.unlock();
        }
    }


    private void newGeneratorCharacter(CharacterEvent ce) {
        if (char2Type != -1) {
            score--;
            setScore();
        }
        char2Type = ce.character;
    }


    private void newTypistCharacter(CharacterEvent ce) {
        if (char2Type != ce.character) {
            score--;
        } else {
            score++;
            char2Type = -1;
        }
        setScore();
    }
    @Override
    public void newCharacter(CharacterEvent ce) {
        try {
            scoreLock.lock();
            // is user hasn't typed previous character
            if (ce.characterSource == generator) {
                newGeneratorCharacter(ce);

            } else {
                // if user has typed, check it against the current generator.
                // if matched, then unset the point.
                // is char2Type is -1, that means user has typed at least an extra character to that of generator
                newTypistCharacter(ce);
            }
        } finally {
            scoreLock.unlock();
        }
    }
}
