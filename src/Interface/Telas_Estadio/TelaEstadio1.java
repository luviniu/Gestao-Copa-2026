package Interface.Telas_Estadio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaEstadio1 {
    private JButton novoEstádioButton;
    private JButton buscarButton;
    private JButton filtrarButton;
    private JTable tabela_estadio;
    private JPanel painel_estadios;

    public TelaEstadio1() {
        novoEstádioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame tela = new JFrame("Adicionar novo estádio");
                tela.setContentPane(new TelaNewEstadio().getPainel_novo_estadio());
                tela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tela.setSize(600,400);
                tela.setLocationRelativeTo(null);
                tela.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame tela = new JFrame("Estádios");
        tela.setContentPane(new TelaEstadio1().painel_estadios);
        tela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tela.setSize(1000,800);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }
}

