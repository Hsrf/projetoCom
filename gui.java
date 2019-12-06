import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class gui {

    public JTextArea textArea;
    private JPanel panel;
    private JTextField textField;
    private JButton button1;
    private JCheckBox cbAudio;
    private JCheckBox cbStatus;
    private JFrame janela;
    private cliente client;
    TargetDataLine audio_in;
    SourceDataLine audio_out;

    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channel, signed, bigEndian);
    }

    public void init_send_audio() {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Not supported");
            System.exit(0);
        }
        try {
            audio_in = (TargetDataLine) AudioSystem.getLine(info);
            audio_in.open(format);
            audio_in.start();

            recorder audioRecorder = new recorder(audio_in, this.client);
            audioRecorder.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        audio_in.start();
    }

    public void init_receive_audio() {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Not supported");
            System.exit(0);
        }
        try {
            audio_out = (SourceDataLine) AudioSystem.getLine(info);
            audio_out.open(format);
            audio_out.start();

            player audioPlayer = new player(audio_out, this.client);
            audioPlayer.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public gui(cliente client){
        this.client = client;

        janela = new JFrame();
        janela.setContentPane(panel);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(540, 540);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setTitle(client.idThis.toUpperCase() + "  -->  " + client.idOther.toUpperCase());
        textArea.setEditable(false);

        cbStatus.setSelected(true);

        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!textField.getText().equals("")) {
                        textArea.append(client.idThis + ": " + textField.getText() + "\n");
                        try {
                            client.sendMsg(textField.getText().getBytes(), client.otherIP, client.portOtherMsg);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        textField.setText("");
                    }
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!textField.getText().equals("")) {
                    textArea.append(client.idThis + ": " + textField.getText() + "\n");
                    try {
                        client.sendMsg(textField.getText().getBytes(), client.otherIP, client.portOtherMsg);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    textField.setText("");
                }
            }
        });

        cbAudio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cbAudio.isSelected()) {
                    client.calling = true;
                    init_send_audio();
                    init_receive_audio();
                }else{
                    client.calling = false;
                }
            }
        });

        cbStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!cbStatus.isSelected()){
                    button1.setEnabled(false);
                    cbAudio.setEnabled(false);
                    cbAudio.setSelected(false);
                    textField.setEnabled(false);
                    textField.setText("");
                }else{
                    button1.setEnabled(true);
                    cbAudio.setEnabled(true);
                    textField.setEnabled(true);
                }
                byte[] bytes = "".getBytes();
                try {
                    client.sendMsg(bytes, client.serverIP, client.serverPort);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
