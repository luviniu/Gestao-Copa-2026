package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class TelaArbitros extends JFrame {
    private JButton novoArbitroButton;
    private JButton buscarButton;
    private JButton filtrarButton;
    private JTable ArbitroColuna;
    private JPanel painelPrincipal;

    public TelaArbitros() {
        setTitle("Gestão de Árbitros - Copa 2026");
        setContentPane(painelPrincipal);
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"Nome", "Nacionalidade", "Experiência (Anos)", "Categoria"};
        DefaultTableModel tabelaModel = new DefaultTableModel(colunas, 0);

        ArbitroColuna.setModel(tabelaModel);
        ArbitroColuna.setAutoCreateRowSorter(false);
        ArbitroColuna.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(ArbitroColuna);

        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        novoArbitroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaArbitros tela = new TelaArbitros();
            tela.setVisible(true);
        });
    }

}