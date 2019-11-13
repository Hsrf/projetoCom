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
    private JCheckBox checkBox;
    private JFrame janela;
    private cliente client;
    public captureAudio capAudio;
    private Thread threadCA;

    public gui(cliente client){
        this.client = client;
        capAudio = new captureAudio();
        janela = new JFrame();
        janela.setContentPane(panel);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(540, 540);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!textField.getText().equals("")) {
                        textArea.append("YOU: " + textField.getText() + "\n");
                        try {
                            client.sendPack(textField.getText(), client.otherIP, client.otherPort);
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
                    textArea.append("YOU: " + textField.getText() + "\n");
                    try {
                        client.sendPack(textField.getText(), client.otherIP, client.otherPort);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    textField.setText("");
                }
            }
        });

        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(checkBox.isSelected()){
                    //permita continuar a thread
                    capAudio.setStop(false);
                    //iniciar thread
                    threadCA = new Thread(capAudio);
                    threadCA.start();
                }else{
                    //pare a thread
                    capAudio.setStop(true);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
