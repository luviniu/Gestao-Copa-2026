package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TelaPrincipal extends JFrame {
    private JPanel TelaPrincipalPanel;
    private JButton BotaoPrincipal;
    private JTabbedPane PrincipalPane;
    private JTextField NomeField;
    private JTable GestaoTable;
    private JRadioButton NomeButtom;
    private JComboBox CargocomboBox;
    private JTabbedPane RelatoriosPane;
    private JTable DesempenhoTable;
    private JTable PartidasTable;
    private JPanel InicioPanel;
    private JPanel GestaoPainel;
    private JPanel PartidasPanel;
    private JPanel DesempenhoPanel;
    private JRadioButton CPFButtom;
    private JRadioButton CARGOButtom;
    private JRadioButton PAISButtom;
    private JLabel NomeLabel;
    private JLabel CPFLabel;
    private JLabel CargoLabel;
    private JLabel PaisLabel;
    private JTextField CPFField;
    private JTextField PaisField;

    public TelaPrincipal() {
        setContentPane(TelaPrincipalPanel);
        setSize(800,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] partidaTable={
                "Status", "Time 1", "Time 2", "Estádio", "Gols Time 1", "Gols Time 2", "Público", "Faltas", "Vencedor"

        };

        DefaultTableModel partidaModel = new DefaultTableModel(partidaTable, 0);
        PartidasTable.setModel(partidaModel);

        PartidasTable.setAutoCreateRowSorter(false);
        PartidasTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(PartidasTable);
        PartidasPanel.setLayout(new java.awt.BorderLayout());
        PartidasPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        

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
