package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;
import br.edu.cs.poo.ac.seguro.mediators.StringUtils;

public class TelaSeguradoPessoa extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// Atributo do Mediator (Requisito 4)
	private SeguradoPessoaMediator mediator = SeguradoPessoaMediator.getInstancia();

	// Componentes de Entrada - Dados Pessoais (Requisito 5)
	private JTextField txtCpf = new JTextField(15);
	private JTextField txtNome = new JTextField(30);
	private JTextField txtDataNascimento = new JTextField(10); // dd/MM/yyyy
	private JTextField txtRenda = new JTextField(12);
	private JTextField txtBonus = new JTextField(12);

	// Componentes de Entrada - Endereço
	private JTextField txtLogradouro = new JTextField(25);
	private JTextField txtNumero = new JTextField(8);
	private JTextField txtComplemento = new JTextField(15);
	private JTextField txtCep = new JTextField(10);
	private JTextField txtCidade = new JTextField(18);
	private JComboBox<String> cbEstado = new JComboBox<>(new String[] {
			"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
			"MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
			"RS", "RO", "RR", "SC", "SP", "SE", "TO"
	});
	private JTextField txtPais = new JTextField("Brasil", 15);

	// Botões de Ação do CRUD (Requisito 3)
	private JButton btnBuscar = new JButton("Buscar");
	private JButton btnIncluir = new JButton("Incluir");
	private JButton btnAlterar = new JButton("Alterar");
	private JButton btnExcluir = new JButton("Excluir");
	private JButton btnLimpar = new JButton("Limpar");

	public TelaSeguradoPessoa() {
		configurarJanela();
		construirLayout();
		configurarAcoes();
		limparFormulario();
	}

	private void configurarJanela() {
		setTitle("Cadastro de Segurado Pessoa Física");
		setSize(680, 560);
		setMinimumSize(new Dimension(600, 500));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void construirLayout() {
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Topo: Campo Chave e Busca
		JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		painelBusca.setBorder(BorderFactory.createTitledBorder("Identificação do Segurado"));
		painelBusca.add(new JLabel("CPF:"));
		painelBusca.add(txtCpf);
		painelBusca.add(btnBuscar);
		painelPrincipal.add(painelBusca, BorderLayout.NORTH);

		// Centro: Formulário com Dados Pessoais e Endereço
		JPanel painelCentro = new JPanel();
		painelCentro.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 5, 4, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Seção 1: Dados Pessoais
		JPanel painelDadosPessoais = new JPanel(new GridBagLayout());
		painelDadosPessoais.setBorder(BorderFactory.createTitledBorder("Dados Pessoais"));
		GridBagConstraints gbcP = new GridBagConstraints();
		gbcP.insets = new Insets(4, 5, 4, 5);
		gbcP.fill = GridBagConstraints.HORIZONTAL;

		gbcP.gridx = 0; gbcP.gridy = 0;
		painelDadosPessoais.add(new JLabel("Nome Completo:"), gbcP);
		gbcP.gridx = 1; gbcP.gridwidth = 3;
		painelDadosPessoais.add(txtNome, gbcP);

		gbcP.gridx = 0; gbcP.gridy = 1; gbcP.gridwidth = 1;
		painelDadosPessoais.add(new JLabel("Data Nasc. (dd/MM/yyyy):"), gbcP);
		gbcP.gridx = 1;
		painelDadosPessoais.add(txtDataNascimento, gbcP);

		gbcP.gridx = 2;
		painelDadosPessoais.add(new JLabel("Renda (R$):"), gbcP);
		gbcP.gridx = 3;
		painelDadosPessoais.add(txtRenda, gbcP);

		gbcP.gridx = 0; gbcP.gridy = 2;
		painelDadosPessoais.add(new JLabel("Bônus Inicial (R$):"), gbcP);
		gbcP.gridx = 1;
		painelDadosPessoais.add(txtBonus, gbcP);

		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
		painelCentro.add(painelDadosPessoais, gbc);

		// Seção 2: Endereço
		JPanel painelEndereco = new JPanel(new GridBagLayout());
		painelEndereco.setBorder(BorderFactory.createTitledBorder("Endereço Residencial"));
		GridBagConstraints gbcE = new GridBagConstraints();
		gbcE.insets = new Insets(4, 5, 4, 5);
		gbcE.fill = GridBagConstraints.HORIZONTAL;

		gbcE.gridx = 0; gbcE.gridy = 0;
		painelEndereco.add(new JLabel("Logradouro:"), gbcE);
		gbcE.gridx = 1; gbcE.gridwidth = 3;
		painelEndereco.add(txtLogradouro, gbcE);

		gbcE.gridx = 0; gbcE.gridy = 1; gbcE.gridwidth = 1;
		painelEndereco.add(new JLabel("Número:"), gbcE);
		gbcE.gridx = 1;
		painelEndereco.add(txtNumero, gbcE);

		gbcE.gridx = 2;
		painelEndereco.add(new JLabel("Complemento:"), gbcE);
		gbcE.gridx = 3;
		painelEndereco.add(txtComplemento, gbcE);

		gbcE.gridx = 0; gbcE.gridy = 2;
		painelEndereco.add(new JLabel("CEP (8 dígitos):"), gbcE);
		gbcE.gridx = 1;
		painelEndereco.add(txtCep, gbcE);

		gbcE.gridx = 2;
		painelEndereco.add(new JLabel("Cidade:"), gbcE);
		gbcE.gridx = 3;
		painelEndereco.add(txtCidade, gbcE);

		gbcE.gridx = 0; gbcE.gridy = 3;
		painelEndereco.add(new JLabel("Estado (UF):"), gbcE);
		gbcE.gridx = 1;
		cbEstado.setSelectedItem("PE");
		painelEndereco.add(cbEstado, gbcE);

		gbcE.gridx = 2;
		painelEndereco.add(new JLabel("País:"), gbcE);
		gbcE.gridx = 3;
		painelEndereco.add(txtPais, gbcE);

		gbc.gridx = 0; gbc.gridy = 1;
		painelCentro.add(painelEndereco, gbc);

		painelPrincipal.add(painelCentro, BorderLayout.CENTER);

		// Rodapé: Painel de Botões de Ação do CRUD
		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
		btnIncluir.setFont(new Font("SansSerif", Font.BOLD, 12));
		painelBotoes.add(btnIncluir);
		painelBotoes.add(btnAlterar);
		painelBotoes.add(btnExcluir);
		painelBotoes.add(btnLimpar);

		painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

		setContentPane(painelPrincipal);
	}

	private void configurarAcoes() {
		btnBuscar.addActionListener(e -> executarBuscar());
		btnIncluir.addActionListener(e -> executarIncluir());
		btnAlterar.addActionListener(e -> executarAlterar());
		btnExcluir.addActionListener(e -> executarExcluir());
		btnLimpar.addActionListener(e -> limparFormulario());
	}

	private void executarBuscar() {
		String cpf = txtCpf.getText().trim();
		if (StringUtils.ehNuloOuBranco(cpf)) {
			JOptionPane.showMessageDialog(this, "Informe o CPF para realizar a busca.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		SeguradoPessoa seg = mediator.buscarSeguradoPessoa(cpf);
		if (seg == null) {
			JOptionPane.showMessageDialog(this, "Nenhum segurado encontrado com o CPF informado.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
			btnAlterar.setEnabled(false);
			btnExcluir.setEnabled(false);
			btnIncluir.setEnabled(true);
		} else {
			preencherCampos(seg);
			btnAlterar.setEnabled(true);
			btnExcluir.setEnabled(true);
			btnIncluir.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Segurado localizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void executarIncluir() {
		SeguradoPessoa seg = montarObjetoDaTela();
		if (seg == null) {
			return;
		}

		String erro = mediator.incluirSeguradoPessoa(seg);
		if (erro != null) {
			JOptionPane.showMessageDialog(this, erro, "Validação / Erro", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Segurado incluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarAlterar() {
		SeguradoPessoa seg = montarObjetoDaTela();
		if (seg == null) {
			return;
		}

		String erro = mediator.alterarSeguradoPessoa(seg);
		if (erro != null) {
			JOptionPane.showMessageDialog(this, erro, "Validação / Erro", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Segurado alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarExcluir() {
		String cpf = txtCpf.getText().trim();
		if (StringUtils.ehNuloOuBranco(cpf)) {
			JOptionPane.showMessageDialog(this, "Informe o CPF a ser excluído.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente excluir o segurado com CPF " + cpf + "?",
				"Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			String erro = mediator.excluirSeguradoPessoa(cpf);
			if (erro != null) {
				JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Segurado excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				limparFormulario();
			}
		}
	}

	private SeguradoPessoa montarObjetoDaTela() {
		String cpf = txtCpf.getText().trim();
		String nome = txtNome.getText().trim();

		LocalDate dataNasc = null;
		String dataStr = txtDataNascimento.getText().trim();
		if (!StringUtils.ehNuloOuBranco(dataStr)) {
			try {
				dataNasc = LocalDate.parse(dataStr, DATE_FORMATTER);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}

		double renda = 0.0;
		String rendaStr = txtRenda.getText().trim().replace(",", ".");
		if (!StringUtils.ehNuloOuBranco(rendaStr)) {
			try {
				renda = Double.parseDouble(rendaStr);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Valor de renda inválido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}

		BigDecimal bonus = BigDecimal.ZERO;
		String bonusStr = txtBonus.getText().trim().replace(",", ".");
		if (!StringUtils.ehNuloOuBranco(bonusStr)) {
			try {
				bonus = new BigDecimal(bonusStr);
			} catch (Exception ex) {
				bonus = BigDecimal.ZERO;
			}
		}

		Endereco end = new Endereco(
				txtLogradouro.getText().trim(),
				txtCep.getText().trim(),
				txtNumero.getText().trim(),
				txtComplemento.getText().trim(),
				txtPais.getText().trim(),
				(String) cbEstado.getSelectedItem(),
				txtCidade.getText().trim()
		);

		return new SeguradoPessoa(nome, end, dataNasc, bonus, cpf, renda);
	}

	private void preencherCampos(SeguradoPessoa seg) {
		txtCpf.setText(seg.getCpf());
		txtNome.setText(seg.getNome() != null ? seg.getNome() : "");
		txtDataNascimento.setText(seg.getDataNascimento() != null ? seg.getDataNascimento().format(DATE_FORMATTER) : "");
		txtRenda.setText(String.valueOf(seg.getRenda()));
		txtBonus.setText(seg.getBonus() != null ? seg.getBonus().toString() : "0.00");

		Endereco end = seg.getEndereco();
		if (end != null) {
			txtLogradouro.setText(end.getLogradouro() != null ? end.getLogradouro() : "");
			txtNumero.setText(end.getNumero() != null ? end.getNumero() : "");
			txtComplemento.setText(end.getComplemento() != null ? end.getComplemento() : "");
			txtCep.setText(end.getCep() != null ? end.getCep() : "");
			txtCidade.setText(end.getCidade() != null ? end.getCidade() : "");
			if (end.getEstado() != null) {
				cbEstado.setSelectedItem(end.getEstado().toUpperCase());
			}
			txtPais.setText(end.getPais() != null ? end.getPais() : "Brasil");
		}
	}

	private void limparFormulario() {
		txtCpf.setText("");
		txtNome.setText("");
		txtDataNascimento.setText("");
		txtRenda.setText("0.00");
		txtBonus.setText("0.00");
		txtLogradouro.setText("");
		txtNumero.setText("");
		txtComplemento.setText("");
		txtCep.setText("");
		txtCidade.setText("");
		cbEstado.setSelectedItem("PE");
		txtPais.setText("Brasil");

		btnIncluir.setEnabled(true);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		txtCpf.requestFocus();
	}
}
