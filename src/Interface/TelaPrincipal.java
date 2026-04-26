package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TelaPrincipal extends JFrame {
    private JPanel TelaPrincipalPanel;
    private JTextPane telaPrincipalTextPane;
    private JButton BotaoPrincipal;

    public TelaPrincipal() {
        setContentPane(TelaPrincipalPanel);
        setSize(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BotaoPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TelaLogin TelaLogin = new TelaLogin();
                TelaLogin.setVisible(true);
                TelaPrincipal.this.dispose();

            }
        });
    }
}
