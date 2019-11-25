import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;

public class idGui {
    private JFrame janela;
    private JButton button;
    private JTextField textField;
    private JPanel panel;
    private JTextPane textPane;
    private cliente client;

    public static byte[] concatenarBytes(byte[] bytes1, byte[] bytes2){
        byte[] new_byte = new byte[bytes1.length + bytes2.length];
        int k = 0;

        for(int i = 0 ; i < bytes1.length ; i++) new_byte[k++] = bytes1[i];
        for(int i = 0 ; i < bytes2.length ; i++) new_byte[k++] = bytes2[i];

        return new_byte;
    }

    public idGui(cliente client){
        this.client = client;
        janela = new JFrame();
        janela.setContentPane(panel);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(400, 150);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!textField.getText().equals("")) {
                    client.idThis = textField.getText();

                    //Criando a mensagem de inicializacao
                    int thisPorta = client.portThisAudio.getLocalPort();
                    byte[] msg = ByteBuffer.allocate(4).putInt(thisPorta).array();
                    msg = concatenarBytes(msg, client.idThis.getBytes());

                    //Inicializando o cliente no servidor
                    try {
                        client.sendMsg(msg, client.serverIP, client.serverPort);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    //Fecha a janela
                    janela.dispose();
                }
            }
        });
    }
}
