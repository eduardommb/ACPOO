package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;
import br.edu.cs.poo.ac.seguro.mediators.StringUtils;

public class TelaSeguradoEmpresa extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// Atributo do Mediator (Requisito 4)
	private SeguradoEmpresaMediator mediator = SeguradoEmpresaMediator.getInstancia();

	// Componentes de Entrada - Identificação e Dados Empresariais (Requisito 5)
	private JTextField txtCnpj = new JTextField(15);
	private JTextField txtNome = new JTextField(30);
	private JTextField txtDataAbertura = new JTextField(10); // dd/MM/yyyy
	private JTextField txtFaturamento = new JTextField(12);
	private JTextField txtBonus = new JTextField(12);

	// Componentes de Entrada com Radio Buttons para tipo booleano (Requisito 5)
	private JRadioButton rbLocadoraSim = new JRadioButton("Sim");
	private JRadioButton rbLocadoraNao = new JRadioButton("Não", true);
	private ButtonGroup bgLocadora = new ButtonGroup();

	// Componentes de Entrada - Endereço Empresarial
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

	public TelaSeguradoEmpresa() {
		configurarJanela();
		construirLayout();
		configurarAcoes();
		limparFormulario();
	}

	private void configurarJanela() {
		setTitle("Cadastro de Segurado Pessoa Jurídica (Empresa)");
		setSize(700, 600);
		setMinimumSize(new Dimension(620, 520));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void construirLayout() {
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Topo: Chave de Busca (CNPJ)
		JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		painelBusca.setBorder(BorderFactory.createTitledBorder("Identificação da Empresa"));
		painelBusca.add(new JLabel("CNPJ (14 dígitos):"));
		painelBusca.add(txtCnpj);
		painelBusca.add(btnBuscar);
		painelPrincipal.add(painelBusca, BorderLayout.NORTH);

		// Centro: Formulário com Dados Empresariais e Endereço
		JPanel painelCentro = new JPanel();
		painelCentro.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 5, 4, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Seção 1: Dados Empresariais
		JPanel painelDadosEmpresa = new JPanel(new GridBagLayout());
		painelDadosEmpresa.setBorder(BorderFactory.createTitledBorder("Dados Empresariais"));
		GridBagConstraints gbcD = new GridBagConstraints();
		gbcD.insets = new Insets(4, 5, 4, 5);
		gbcD.fill = GridBagConstraints.HORIZONTAL;

		gbcD.gridx = 0; gbcD.gridy = 0;
		painelDadosEmpresa.add(new JLabel("Razão Social / Nome:"), gbcD);
		gbcD.gridx = 1; gbcD.gridwidth = 3;
		painelDadosEmpresa.add(txtNome, gbcD);

		gbcD.gridx = 0; gbcD.gridy = 1; gbcD.gridwidth = 1;
		painelDadosEmpresa.add(new JLabel("Data Abertura (dd/MM/yyyy):"), gbcD);
		gbcD.gridx = 1;
		painelDadosEmpresa.add(txtDataAbertura, gbcD);

		gbcD.gridx = 2;
		painelDadosEmpresa.add(new JLabel("Faturamento (R$):"), gbcD);
		gbcD.gridx = 3;
		painelDadosEmpresa.add(txtFaturamento, gbcD);

		gbcD.gridx = 0; gbcD.gridy = 2;
		painelDadosEmpresa.add(new JLabel("Bônus Inicial (R$):"), gbcD);
		gbcD.gridx = 1;
		painelDadosEmpresa.add(txtBonus, gbcD);

		gbcD.gridx = 2;
		painelDadosEmpresa.add(new JLabel("Locadora de Veículos:"), gbcD);
		JPanel painelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		bgLocadora.add(rbLocadoraSim);
		bgLocadora.add(rbLocadoraNao);
		painelRadio.add(rbLocadoraSim);
		painelRadio.add(rbLocadoraNao);
		gbcD.gridx = 3;
		painelDadosEmpresa.add(painelRadio, gbcD);

		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
		painelCentro.add(painelDadosEmpresa, gbc);

		// Seção 2: Endereço
		JPanel painelEndereco = new JPanel(new GridBagLayout());
		painelEndereco.setBorder(BorderFactory.createTitledBorder("Endereço Comercial"));
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
		String cnpj = txtCnpj.getText().trim();
		if (StringUtils.ehNuloOuBranco(cnpj)) {
			JOptionPane.showMessageDialog(this, "Informe o CNPJ para realizar a busca.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		SeguradoEmpresa seg = mediator.buscarSeguradoEmpresa(cnpj);
		if (seg == null) {
			JOptionPane.showMessageDialog(this, "Nenhuma empresa encontrada com o CNPJ informado.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
			btnAlterar.setEnabled(false);
			btnExcluir.setEnabled(false);
			btnIncluir.setEnabled(true);
		} else {
			preencherCampos(seg);
			btnAlterar.setEnabled(true);
			btnExcluir.setEnabled(true);
			btnIncluir.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Empresa localizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void executarIncluir() {
		SeguradoEmpresa seg = montarObjetoDaTela();
		if (seg == null) {
			return;
		}

		String erro = mediator.incluirSeguradoEmpresa(seg);
		if (erro != null) {
			JOptionPane.showMessageDialog(this, erro, "Validação / Erro", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Empresa incluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarAlterar() {
		SeguradoEmpresa seg = montarObjetoDaTela();
		if (seg == null) {
			return;
		}

		String erro = mediator.alterarSeguradoEmpresa(seg);
		if (erro != null) {
			JOptionPane.showMessageDialog(this, erro, "Validação / Erro", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Empresa alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarExcluir() {
		String cnpj = txtCnpj.getText().trim();
		if (StringUtils.ehNuloOuBranco(cnpj)) {
			JOptionPane.showMessageDialog(this, "Informe o CNPJ a ser excluído.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente excluir a empresa com CNPJ " + cnpj + "?",
				"Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			String erro = mediator.excluirSeguradoEmpresa(cnpj);
			if (erro != null) {
				JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Empresa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				limparFormulario();
			}
		}
	}

	private SeguradoEmpresa montarObjetoDaTela() {
		String cnpj = txtCnpj.getText().trim();
		String nome = txtNome.getText().trim();

		LocalDate dataAbertura = null;
		String dataStr = txtDataAbertura.getText().trim();
		if (!StringUtils.ehNuloOuBranco(dataStr)) {
			try {
				dataAbertura = LocalDate.parse(dataStr, DATE_FORMATTER);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}

		double faturamento = 0.0;
		String fatStr = txtFaturamento.getText().trim().replace(",", ".");
		if (!StringUtils.ehNuloOuBranco(fatStr)) {
			try {
				faturamento = Double.parseDouble(fatStr);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Valor de faturamento inválido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
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

		boolean ehLocadora = rbLocadoraSim.isSelected();

		Endereco end = new Endereco(
				txtLogradouro.getText().trim(),
				txtCep.getText().trim(),
				txtNumero.getText().trim(),
				txtComplemento.getText().trim(),
				txtPais.getText().trim(),
				(String) cbEstado.getSelectedItem(),
				txtCidade.getText().trim()
		);

		return new SeguradoEmpresa(nome, end, dataAbertura, bonus, cnpj, faturamento, ehLocadora);
	}

	private void preencherCampos(SeguradoEmpresa seg) {
		txtCnpj.setText(seg.getCnpj());
		txtNome.setText(seg.getNome() != null ? seg.getNome() : "");
		txtDataAbertura.setText(seg.getDataAbertura() != null ? seg.getDataAbertura().format(DATE_FORMATTER) : "");
		txtFaturamento.setText(String.valueOf(seg.getFaturamento()));
		txtBonus.setText(seg.getBonus() != null ? seg.getBonus().toString() : "0.00");

		if (seg.isEhLocadoraDeVeiculos()) {
			rbLocadoraSim.setSelected(true);
		} else {
			rbLocadoraNao.setSelected(true);
		}

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
		txtCnpj.setText("");
		txtNome.setText("");
		txtDataAbertura.setText("");
		txtFaturamento.setText("0.00");
		txtBonus.setText("0.00");
		rbLocadoraNao.setSelected(true);

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
		txtCnpj.requestFocus();
	}
}
