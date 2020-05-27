package ch3;


import ch2.AnimatedCharacterDisplayCanvas;
import ch2.CharacterDisplayCanvas;
import ch2.CharacterEventHandler;
import ch2.CharacterListener;
import ch2.CharacterSource;
import ch2.RandomCharacterGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author gagandeep.nagpal
 **/
public class SwingTypeTester extends JFrame implements CharacterSource {
    /**
     * this is a thread and a character source that will be created when start button is clicked.
     * It adds {@link #randomCharacterDisplayCanvas } as a listener and it displays that character to NORTH side
     */
    protected RandomCharacterGenerator producer;
    private AnimatedCharacterDisplayCanvas randomCharacterDisplayCanvas;
    /**
     * This is a listener, it displays a character
     */
    private CharacterDisplayCanvas typedCharacterFeedbackCanvas;
    private JButton startButton;
    private JButton stopButton;
    private CharacterEventHandler handler;

    private ScoreLabelLockVersion scoreLabel;
    public SwingTypeTester() {
        initComponents();
    }

    public static void main(String[] args) {
        new SwingTypeTester().setVisible(true);
    }

    private void initComponents() {
        handler = new CharacterEventHandler();
        randomCharacterDisplayCanvas = new AnimatedCharacterDisplayCanvas();
        typedCharacterFeedbackCanvas = new CharacterDisplayCanvas(this);
        JButton quitButton = new JButton();
        startButton = new JButton();
        stopButton = new JButton();
        add(randomCharacterDisplayCanvas, BorderLayout.NORTH);
        add(typedCharacterFeedbackCanvas, BorderLayout.CENTER);
        JPanel p = new JPanel();
        startButton.setText("Start");
        quitButton.setText("Quit");
        stopButton.setText("Stop");
        JPanel scorePanel = new JPanel();
        scoreLabel = new ScoreLabelLockVersion();
        scorePanel.add(scoreLabel);
        p.add(startButton);
        p.add(quitButton);
        p.add(stopButton);
        add(p, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.WEST);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                quit();
            }
        });
        typedCharacterFeedbackCanvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                char c = ke.getKeyChar();
                if (c != KeyEvent.CHAR_UNDEFINED) {
                    fireNewCharacter(c);
                }
            }
        });
        startButton.addActionListener(evt -> {
            // creates a thread that generates a character
            producer = new RandomCharacterGenerator();
            // here it means that when producer produces, displayCanvas will show it.
            randomCharacterDisplayCanvas.setCharacterSource(producer);

            scoreLabel.resetGenerator(producer);
            scoreLabel.resetTypist(this);
            // runs the thread
            Thread t = new Thread(producer);
            t.start( );
            // just setting buttons
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            new Thread(randomCharacterDisplayCanvas).start();
            typedCharacterFeedbackCanvas.setEnabled(true);
            typedCharacterFeedbackCanvas.requestFocus();
        });

        stopButton.addActionListener(e -> {
            producer.setDone(true);
            System.out.println("i just interrupted producer thread, isProducerThreadInterrupted:" + producer.isInterrupted());
            randomCharacterDisplayCanvas.setDone(true);
            scoreLabel.resetScore();
        });

        quitButton.addActionListener(evt -> quit());
        pack();
    }

    private void quit() {
        System.exit(0);
    }

    @Override
    public void addCharacterListener(CharacterListener cl) {
        handler.addCharacterListener(cl);
    }

    @Override
    public void removeCharacterListener(CharacterListener cl) {
        handler.removeCharacterListener(cl);
    }

    public void fireNewCharacter(int c) {
        handler.fireNewCharacter(this, c);
    }

    @Override
    public void nextCharacter() {
        throw new IllegalStateException("We don't produce on demand");
    }
}

