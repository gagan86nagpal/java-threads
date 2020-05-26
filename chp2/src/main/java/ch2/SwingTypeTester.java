package ch2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.*;

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

    public SwingTypeTester() {
        initComponents();
    }

    public static void main(String[] args) {
        new SwingTypeTester().setVisible(true);
    }

    private void initComponents() {
        handler = new CharacterEventHandler();
        randomCharacterDisplayCanvas = new AnimatedCharacterDisplayCanvas();
        typedCharacterFeedbackCanvas = new CharacterDisplayCanvas();
        addCharacterListener(typedCharacterFeedbackCanvas);
        JButton quitButton = new JButton();
        startButton = new JButton();
        stopButton = new JButton();
        add(randomCharacterDisplayCanvas, BorderLayout.NORTH);
        add(typedCharacterFeedbackCanvas, BorderLayout.CENTER);
        JPanel p = new JPanel();
        startButton.setText("Start");
        quitButton.setText("Quit");
        stopButton.setText("Stop");
        p.add(startButton);
        p.add(quitButton);
        p.add(stopButton);
        add(p, BorderLayout.SOUTH);
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
            producer.interrupt();
            randomCharacterDisplayCanvas.setDone(true);
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

