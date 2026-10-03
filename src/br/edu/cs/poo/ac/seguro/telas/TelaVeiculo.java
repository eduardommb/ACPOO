package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Year;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;
import br.edu.cs.poo.ac.seguro.mediators.StringUtils;

public class TelaVeiculo extends JFrame {

	private static final long serialVersionUID = 1L;

	// Atributos de persistência e mediators (Requisito 4)
	private VeiculoDAO veiculoDAO = new VeiculoDAO();
	private SeguradoPessoaMediator seguradoPessoaMediator = SeguradoPessoaMediator.getInstancia();
	private SeguradoEmpresaMediator seguradoEmpresaMediator = SeguradoEmpresaMediator.getInstancia();

	// Componentes visuais - Identificação (Chave)
	private JTextField txtPlaca = new JTextField(10);
	private JButton btnBuscar = new JButton("Buscar");

	// Componentes visuais aderentes à natureza dos dados (Requisito 5)
	private JSpinner spnAno = new JSpinner(new SpinnerNumberModel(Year.now().getValue(), 1900, 2100, 1));
	private JComboBox<CategoriaVeiculo> cbCategoria = new JComboBox<>(CategoriaVeiculo.values());

	// Tipo de Proprietário usando Radio Buttons (Requisito 5)
	private JRadioButton rbPessoaFisica = new JRadioButton("Pessoa Física (CPF)", true);
	private JRadioButton rbPessoaJuridica = new JRadioButton("Pessoa Jurídica (CNPJ)");
	private ButtonGroup bgTipoProprietario = new ButtonGroup();

	private JLabel lblDocumento = new JLabel("CPF do Proprietário:");
	private JTextField txtDocumento = new JTextField(15);
	private JButton btnVerificarProprietario = new JButton("Verificar");
	private JLabel lblNomeProprietario = new JLabel("Nenhum proprietário selecionado");

	// Botões CRUD (Requisito 3)
	private JButton btnIncluir = new JButton("Incluir");
	private JButton btnAlterar = new JButton("Alterar");
	private JButton btnExcluir = new JButton("Excluir");
	private JButton btnLimpar = new JButton("Limpar");

	public TelaVeiculo() {
		configurarJanela();
		construirLayout();
		configurarAcoes();
		limparFormulario();
	}

	private void configurarJanela() {
		setTitle("Cadastro de Veículos");
		setSize(650, 480);
		setMinimumSize(new Dimension(580, 420));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void construirLayout() {
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Topo: Chave de Busca (Placa)
		JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		painelBusca.setBorder(BorderFactory.createTitledBorder("Identificação do Veículo"));
		painelBusca.add(new JLabel("Placa do Veículo:"));
		painelBusca.add(txtPlaca);
		painelBusca.add(btnBuscar);
		painelPrincipal.add(painelBusca, BorderLayout.NORTH);

		// Centro: Formulário com Dados do Veículo e Proprietário
		JPanel painelCentro = new JPanel();
		painelCentro.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Seção 1: Dados do Veículo
		JPanel painelDadosVeiculo = new JPanel(new GridBagLayout());
		painelDadosVeiculo.setBorder(BorderFactory.createTitledBorder("Características do Veículo"));
		GridBagConstraints gbcV = new GridBagConstraints();
		gbcV.insets = new Insets(5, 5, 5, 5);
		gbcV.fill = GridBagConstraints.HORIZONTAL;

		gbcV.gridx = 0; gbcV.gridy = 0;
		painelDadosVeiculo.add(new JLabel("Ano de Fabricação:"), gbcV);
		gbcV.gridx = 1;
		spnAno.setEditor(new JSpinner.NumberEditor(spnAno, "####"));
		painelDadosVeiculo.add(spnAno, gbcV);

		gbcV.gridx = 0; gbcV.gridy = 1;
		painelDadosVeiculo.add(new JLabel("Categoria:"), gbcV);
		gbcV.gridx = 1;
		cbCategoria.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof CategoriaVeiculo) {
					CategoriaVeiculo cat = (CategoriaVeiculo) value;
					setText(cat.getCodigo() + " - " + cat.getNome() + " (" + cat.name() + ")");
				}
				return this;
			}
		});
		painelDadosVeiculo.add(cbCategoria, gbcV);

		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
		painelCentro.add(painelDadosVeiculo, gbc);

		// Seção 2: Proprietário (Vinculado a Segurados)
		JPanel painelProprietario = new JPanel(new GridBagLayout());
		painelProprietario.setBorder(BorderFactory.createTitledBorder("Proprietário do Veículo (Segurado)"));
		GridBagConstraints gbcP = new GridBagConstraints();
		gbcP.insets = new Insets(5, 5, 5, 5);
		gbcP.fill = GridBagConstraints.HORIZONTAL;

		// Radio Buttons para escolha do tipo de proprietário
		bgTipoProprietario.add(rbPessoaFisica);
		bgTipoProprietario.add(rbPessoaJuridica);
		JPanel painelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		painelRadios.add(rbPessoaFisica);
		painelRadios.add(rbPessoaJuridica);

		gbcP.gridx = 0; gbcP.gridy = 0; gbcP.gridwidth = 3;
		painelProprietario.add(painelRadios, gbcP);

		gbcP.gridx = 0; gbcP.gridy = 1; gbcP.gridwidth = 1;
		painelProprietario.add(lblDocumento, gbcP);

		gbcP.gridx = 1;
		painelProprietario.add(txtDocumento, gbcP);

		gbcP.gridx = 2;
		painelProprietario.add(btnVerificarProprietario, gbcP);

		gbcP.gridx = 0; gbcP.gridy = 2; gbcP.gridwidth = 3;
		lblNomeProprietario.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblNomeProprietario.setForeground(new Color(51, 65, 85));
		painelProprietario.add(lblNomeProprietario, gbcP);

		gbc.gridx = 0; gbc.gridy = 1;
		painelCentro.add(painelProprietario, gbc);

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

		rbPessoaFisica.addActionListener(e -> {
			lblDocumento.setText("CPF do Proprietário:");
			verificarProprietario();
		});

		rbPessoaJuridica.addActionListener(e -> {
			lblDocumento.setText("CNPJ do Proprietário:");
			verificarProprietario();
		});

		btnVerificarProprietario.addActionListener(e -> verificarProprietario());
	}

	private void executarBuscar() {
		String placa = txtPlaca.getText().trim();
		if (StringUtils.ehNuloOuBranco(placa)) {
			JOptionPane.showMessageDialog(this, "Informe a placa do veículo para buscar.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Veiculo v = veiculoDAO.buscar(placa);
		if (v == null) {
			JOptionPane.showMessageDialog(this, "Nenhum veículo encontrado com a placa informada.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
			btnAlterar.setEnabled(false);
			btnExcluir.setEnabled(false);
			btnIncluir.setEnabled(true);
		} else {
			preencherCampos(v);
			btnAlterar.setEnabled(true);
			btnExcluir.setEnabled(true);
			btnIncluir.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Veículo localizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void executarIncluir() {
		Veiculo v = montarObjetoDaTela();
		if (v == null) {
			return;
		}

		boolean sucesso = veiculoDAO.incluir(v);
		if (!sucesso) {
			JOptionPane.showMessageDialog(this, "Já existe um veículo cadastrado com esta placa.", "Erro na Inclusão", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarAlterar() {
		Veiculo v = montarObjetoDaTela();
		if (v == null) {
			return;
		}

		boolean sucesso = veiculoDAO.alterar(v);
		if (!sucesso) {
			JOptionPane.showMessageDialog(this, "Veículo não encontrado para alteração.", "Erro na Alteração", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Veículo alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarExcluir() {
		String placa = txtPlaca.getText().trim();
		if (StringUtils.ehNuloOuBranco(placa)) {
			JOptionPane.showMessageDialog(this, "Informe a placa do veículo a ser excluído.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente excluir o veículo de placa " + placa + "?",
				"Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			boolean sucesso = veiculoDAO.excluir(placa);
			if (!sucesso) {
				JOptionPane.showMessageDialog(this, "Veículo não encontrado para exclusão.", "Erro", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				limparFormulario();
			}
		}
	}

	private boolean verificarProprietario() {
		String doc = txtDocumento.getText().trim();
		if (StringUtils.ehNuloOuBranco(doc)) {
			lblNomeProprietario.setText("Informe o documento do proprietário.");
			lblNomeProprietario.setForeground(Color.RED);
			return false;
		}

		if (rbPessoaFisica.isSelected()) {
			SeguradoPessoa pessoa = seguradoPessoaMediator.buscarSeguradoPessoa(doc);
			if (pessoa != null) {
				lblNomeProprietario.setText("Proprietário: " + pessoa.getNome() + " (CPF: " + pessoa.getCpf() + ")");
				lblNomeProprietario.setForeground(new Color(22, 101, 52));
				return true;
			} else {
				lblNomeProprietario.setText("Pessoa física não encontrada para o CPF informado.");
				lblNomeProprietario.setForeground(Color.RED);
				return false;
			}
		} else {
			SeguradoEmpresa empresa = seguradoEmpresaMediator.buscarSeguradoEmpresa(doc);
			if (empresa != null) {
				lblNomeProprietario.setText("Proprietário: " + empresa.getNome() + " (CNPJ: " + empresa.getCnpj() + ")");
				lblNomeProprietario.setForeground(new Color(22, 101, 52));
				return true;
			} else {
				lblNomeProprietario.setText("Empresa não encontrada para o CNPJ informado.");
				lblNomeProprietario.setForeground(Color.RED);
				return false;
			}
		}
	}

	private Veiculo montarObjetoDaTela() {
		String placa = txtPlaca.getText().trim();
		if (StringUtils.ehNuloOuBranco(placa)) {
			JOptionPane.showMessageDialog(this, "Placa do veículo deve ser informada.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		int ano = (Integer) spnAno.getValue();
		CategoriaVeiculo categoria = (CategoriaVeiculo) cbCategoria.getSelectedItem();

		String doc = txtDocumento.getText().trim();
		SeguradoPessoa pessoa = null;
		SeguradoEmpresa empresa = null;

		if (rbPessoaFisica.isSelected()) {
			pessoa = seguradoPessoaMediator.buscarSeguradoPessoa(doc);
			if (pessoa == null) {
				JOptionPane.showMessageDialog(this, "Proprietário Pessoa Física com CPF '" + doc + "' não foi encontrado.", "Proprietário Inválido", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			empresa = seguradoEmpresaMediator.buscarSeguradoEmpresa(doc);
			if (empresa == null) {
				JOptionPane.showMessageDialog(this, "Proprietário Empresa com CNPJ '" + doc + "' não foi encontrada.", "Proprietário Inválido", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}

		return new Veiculo(placa, ano, empresa, pessoa, categoria);
	}

	private void preencherCampos(Veiculo v) {
		txtPlaca.setText(v.getPlaca());
		spnAno.setValue(v.getAno());
		if (v.getCategoria() != null) {
			cbCategoria.setSelectedItem(v.getCategoria());
		}

		if (v.getProprietarioPessoa() != null) {
			rbPessoaFisica.setSelected(true);
			lblDocumento.setText("CPF do Proprietário:");
			txtDocumento.setText(v.getProprietarioPessoa().getCpf());
			lblNomeProprietario.setText("Proprietário: " + v.getProprietarioPessoa().getNome() + " (CPF: " + v.getProprietarioPessoa().getCpf() + ")");
			lblNomeProprietario.setForeground(new Color(22, 101, 52));
		} else if (v.getProprietarioEmpresa() != null) {
			rbPessoaJuridica.setSelected(true);
			lblDocumento.setText("CNPJ do Proprietário:");
			txtDocumento.setText(v.getProprietarioEmpresa().getCnpj());
			lblNomeProprietario.setText("Proprietário: " + v.getProprietarioEmpresa().getNome() + " (CNPJ: " + v.getProprietarioEmpresa().getCnpj() + ")");
			lblNomeProprietario.setForeground(new Color(22, 101, 52));
		} else {
			txtDocumento.setText("");
			lblNomeProprietario.setText("Nenhum proprietário associado.");
			lblNomeProprietario.setForeground(Color.DARK_GRAY);
		}
	}

	private void limparFormulario() {
		txtPlaca.setText("");
		spnAno.setValue(Year.now().getValue());
		cbCategoria.setSelectedItem(CategoriaVeiculo.BASICO);
		rbPessoaFisica.setSelected(true);
		lblDocumento.setText("CPF do Proprietário:");
		txtDocumento.setText("");
		lblNomeProprietario.setText("Nenhum proprietário selecionado");
		lblNomeProprietario.setForeground(new Color(51, 65, 85));

		btnIncluir.setEnabled(true);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		txtPlaca.requestFocus();
	}
}
