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
        janela.setTitle(client.idThis.toUpperCase() + "  -->  " + client.idOther.toUpperCase());
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
                if(cbAudio.isSelected()){
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

        cbStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!cbStatus.isSelected()){
                    button1.setEnabled(false);
                    cbAudio.setEnabled(false);
                    cbAudio.setSelected(false);
                    capAudio.setStop(true);
                    textField.setEnabled(false);
                    textField.setText("");
                }else{
                    button1.setEnabled(true);
                    cbAudio.setEnabled(true);
                    textField.setEnabled(true);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
