import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;

public class MetronomoGUI extends JFrame {
    private JTextField bpmTextField;
    private JButton startStopButton;
    private boolean isRunning = false;
    private Clip metronomeClip;

    public MetronomoGUI() {
        setTitle("Metronomo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        bpmTextField = new JTextField(10);
        startStopButton = new JButton("Iniciar");
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    startMetronome();
                } else {
                    stopMetronome();
                }
            }
        });

        add(new JLabel("BPM (batidas por minuto):"));
        add(bpmTextField);
        add(startStopButton);
    }

    private void startMetronome() {
        try {
            int bpm = Integer.parseInt(bpmTextField.getText());
            int interval = 60000 / bpm; // Intervalo entre batidas em milissegundos

            File soundFile = new File("metronome_tick.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            metronomeClip = AudioSystem.getClip();
            metronomeClip.open(audioInputStream);

            isRunning = true;
            startStopButton.setText("Parar");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        metronomeClip.setFramePosition(0);
                        metronomeClip.start();
                        try {
                            Thread.sleep(interval);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void stopMetronome() {
        isRunning = false;
        startStopButton.setText("Iniciar");
        metronomeClip.stop();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MetronomoGUI metronomeGUI = new MetronomoGUI();
                metronomeGUI.setVisible(true);
            }
        });
    }
}
