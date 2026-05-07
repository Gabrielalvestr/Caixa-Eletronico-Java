import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private ICaixaEletronico caixa;

    public GUI(Class<?> caixaClass) {
        this.caixa = new CaixaEletronico(); 

        frame = new JFrame("Caixa eletronico");
        frame.setSize(300, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(9, 1, 5, 5)); 

        JLabel lblCliente = new JLabel(" Modulo do Cliente:");
        JButton btnSaque = new JButton("Efetuar Saque");
        JLabel lblAdmin = new JLabel(" Modulo do Administrador:");
        JButton btnRelatorio = new JButton("Relatorio de Cedulas");
        JButton btnValorTotal = new JButton("Valor total disponivel");
        JButton btnReposicao = new JButton("Reposicao de Cedulas");
        JButton btnCota = new JButton("Cota Minima");
        JLabel lblAmbos = new JLabel(" Modulo de Ambos:");
        JButton btnSair = new JButton("Sair");

        frame.add(lblCliente); frame.add(btnSaque);
        frame.add(lblAdmin); frame.add(btnRelatorio);
        frame.add(btnValorTotal); frame.add(btnReposicao);
        frame.add(btnCota); frame.add(lblAmbos); frame.add(btnSair);

        btnValorTotal.addActionListener(e -> JOptionPane.showMessageDialog(frame, caixa.pegaValorTotalDisponivel()));
        btnRelatorio.addActionListener(e -> JOptionPane.showMessageDialog(frame, caixa.pegaRelatorioCedulas()));
        
        btnCota.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Digite a cota mínima:");
            if (input != null && !input.isEmpty()) JOptionPane.showMessageDialog(frame, caixa.armazenaCotaMinima(Integer.parseInt(input)));
        });

        btnReposicao.addActionListener(e -> {
            String nota = JOptionPane.showInputDialog(frame, "Valor da nota (2, 5, 10, 20, 50, 100):");
            String qtd = JOptionPane.showInputDialog(frame, "Quantidade:");
            if (nota != null && qtd != null) JOptionPane.showMessageDialog(frame, caixa.reposicaoCedulas(Integer.parseInt(nota), Integer.parseInt(qtd)));
        });

        btnSaque.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Valor do saque:");
            if (input != null && !input.isEmpty()) JOptionPane.showMessageDialog(frame, caixa.sacar(Integer.parseInt(input)));
        });

        btnSair.addActionListener(e -> {
            if (caixa instanceof CaixaEletronico) JOptionPane.showMessageDialog(frame, ((CaixaEletronico) caixa).getExtrato().toString());
            System.exit(0);
        });
    }

    public void show() {
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}