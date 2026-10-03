package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	public TelaPrincipal() {
		configurarJanela();
		criarMenuBar();
		criarPainelPrincipal();
	}

	private void configurarJanela() {
		setTitle("CALABRIA Seguros - Sistema Integrado de Gestão");
		setSize(800, 520);
		setMinimumSize(new Dimension(650, 450));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void criarMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Menu Cadastros
		JMenu menuCadastros = new JMenu("Cadastros");
		menuCadastros.setMnemonic('C');

		JMenuItem itemPessoa = new JMenuItem("Segurado Pessoa Física...");
		itemPessoa.addActionListener(this::abrirTelaSeguradoPessoa);

		JMenuItem itemEmpresa = new JMenuItem("Segurado Empresa...");
		itemEmpresa.addActionListener(this::abrirTelaSeguradoEmpresa);

		JMenuItem itemVeiculo = new JMenuItem("Veículos...");
		itemVeiculo.addActionListener(this::abrirTelaVeiculo);

		JMenuItem itemApolice = new JMenuItem("Apólices...");
		itemApolice.addActionListener(this::abrirTelaApolice);

		JMenuItem itemSinistro = new JMenuItem("Sinistros...");
		itemSinistro.addActionListener(this::abrirTelaSinistro);

		JMenuItem itemSair = new JMenuItem("Sair");
		itemSair.addActionListener(e -> System.exit(0));

		menuCadastros.add(itemPessoa);
		menuCadastros.add(itemEmpresa);
		menuCadastros.addSeparator();
		menuCadastros.add(itemVeiculo);
		menuCadastros.add(itemApolice);
		menuCadastros.add(itemSinistro);
		menuCadastros.addSeparator();
		menuCadastros.add(itemSair);

		// Menu Ajuda
		JMenu menuAjuda = new JMenu("Ajuda");
		JMenuItem itemSobre = new JMenuItem("Sobre o Sistema");
		itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(this,
				"CALABRIA Seguros v1.0\nSistema de Gestão de Seguros\nProgramação Orientada a Objetos",
				"Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE));
		menuAjuda.add(itemSobre);

		menuBar.add(menuCadastros);
		menuBar.add(menuAjuda);

		setJMenuBar(menuBar);
	}

	private void criarPainelPrincipal() {
		JPanel painelRoot = new JPanel(new BorderLayout());
		painelRoot.setBackground(new Color(245, 247, 250));

		// Cabeçalho
		JPanel painelHeader = new JPanel(new BorderLayout());
		painelHeader.setBackground(new Color(30, 41, 59));
		painelHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

		JLabel lblTitulo = new JLabel("CALABRIA SEGUROS");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblTitulo.setForeground(Color.WHITE);

		JLabel lblSubtitulo = new JLabel("Painel de Controle e Módulos Operacionais");
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblSubtitulo.setForeground(new Color(203, 213, 225));

		painelHeader.add(lblTitulo, BorderLayout.NORTH);
		painelHeader.add(lblSubtitulo, BorderLayout.SOUTH);
		painelRoot.add(painelHeader, BorderLayout.NORTH);

		// Grid de Módulos (Botões em destaque)
		JPanel painelCards = new JPanel(new GridLayout(2, 3, 16, 16));
		painelCards.setBackground(new Color(245, 247, 250));
		painelCards.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

		painelCards.add(criarBotaoModulo("Segurado Pessoa Física", "Gestão de segurados pessoa física (CPF)", this::abrirTelaSeguradoPessoa));
		painelCards.add(criarBotaoModulo("Segurado Empresa", "Gestão de segurados pessoa jurídica (CNPJ)", this::abrirTelaSeguradoEmpresa));
		painelCards.add(criarBotaoModulo("Veículos", "Cadastro e categorias de veículos", this::abrirTelaVeiculo));
		painelCards.add(criarBotaoModulo("Apólices", "Emissão e controle de apólices", this::abrirTelaApolice));
		painelCards.add(criarBotaoModulo("Sinistros", "Abertura e registro de sinistros", this::abrirTelaSinistro));
		painelCards.add(criarBotaoSair());

		painelRoot.add(painelCards, BorderLayout.CENTER);

		// Rodapé / Status Bar
		JPanel painelFooter = new JPanel(new BorderLayout());
		painelFooter.setBackground(new Color(226, 232, 240));
		painelFooter.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));

		JLabel lblStatus = new JLabel("Status: Sistema Operacional | Pronto para operações");
		lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblStatus.setForeground(new Color(71, 85, 105));

		painelFooter.add(lblStatus, BorderLayout.WEST);
		painelRoot.add(painelFooter, BorderLayout.SOUTH);

		setContentPane(painelRoot);
	}

	private JButton criarBotaoModulo(String titulo, String descricao, java.awt.event.ActionListener acao) {
		String html = String.format(
				"<html><center><b style='font-size:13px;'>%s</b><br/><span style='font-size:10px; color:#475569;'>%s</span></center></html>",
				titulo, descricao);

		JButton btn = new JButton(html);
		btn.setFocusPainted(false);
		btn.setBackground(Color.WHITE);
		btn.setForeground(new Color(30, 41, 59));
		btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		btn.addActionListener(acao);
		return btn;
	}

	private JButton criarBotaoSair() {
		String html = "<html><center><b style='font-size:13px; color:#b91c1c;'>Encerrar Sistema</b><br/><span style='font-size:10px; color:#64748b;'>Fechar aplicação</span></center></html>";
		JButton btn = new JButton(html);
		btn.setFocusPainted(false);
		btn.setBackground(Color.WHITE);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(254, 202, 202), 1),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		btn.addActionListener(e -> System.exit(0));
		return btn;
	}

	private void abrirTelaSeguradoPessoa(ActionEvent e) {
		new TelaSeguradoPessoa().setVisible(true);
	}

	private void abrirTelaSeguradoEmpresa(ActionEvent e) {
		new TelaSeguradoEmpresa().setVisible(true);
	}

	private void abrirTelaVeiculo(ActionEvent e) {
		new TelaVeiculo().setVisible(true);
	}

	private void abrirTelaApolice(ActionEvent e) {
		new TelaApolice().setVisible(true);
	}

	private void abrirTelaSinistro(ActionEvent e) {
		new TelaSinistro().setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignored) {
			}
			TelaPrincipal tela = new TelaPrincipal();
			tela.setVisible(true);
		});
	}
}
