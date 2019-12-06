import javax.swing.*;

public class serverUI {


    private JPanel panel;
    private JTextArea textArea;
    private JFrame janela;
    private servidor server;

    public serverUI(servidor server){
        this.server = server;

        janela = new JFrame();
        janela.setContentPane(panel);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(300, 100);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea.setEditable(false);
    }

    public void updateStatus(){
        if(server.isOnline1) textArea.setText(server.id1 + " está online\n");
        else textArea.setText(server.id1 + " está offline\n");
        if(server.isOnline2) textArea.append(server.id2 + " está online\n");
        else textArea.append(server.id2 + " está offline\n");
    }
}
