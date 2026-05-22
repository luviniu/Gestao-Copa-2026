package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame{
    private JPanel TelaLoginPanel;
    private JButton ButtonLogin;
    private JLabel TextLogin;
    private JTextField textField2;
    private JPasswordField passwordField1;

    public TelaLogin() {
        setTitle("Login");
        setContentPane(TelaLoginPanel);
        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TelaPrincipal TelaPrincipal = new TelaPrincipal();
                TelaPrincipal.setVisible(true);
                TelaLogin.this.dispose();

            }

        });

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin TelaLogin = new TelaLogin();
            TelaLogin.setVisible(true);

        });

    }

}
