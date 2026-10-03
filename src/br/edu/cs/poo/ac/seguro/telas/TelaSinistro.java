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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.StringUtils;

public class TelaSinistro extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	// Atributos de persistência (Requisito 4)
	private SinistroDAO sinistroDAO = new SinistroDAO();
	private VeiculoDAO veiculoDAO = new VeiculoDAO();

	// Chave de Busca
	private JTextField txtNumero = new JTextField(15);
	private JButton btnBuscar = new JButton("Buscar");

	// Veículo Envolvido
	private JTextField txtPlacaVeiculo = new JTextField(10);
	private JButton btnVerificarVeiculo = new JButton("Verificar Veículo");
	private JLabel lblDadosVeiculo = new JLabel("Nenhum veículo selecionado");

	// Componentes aderentes à natureza dos dados (Requisito 5)
	// Dropdown list para o Enum TipoSinistro
	private JComboBox<TipoSinistro> cbTipo = new JComboBox<>(TipoSinistro.values());

	// Campos temporais e de auditoria
	private JTextField txtDataHoraSinistro = new JTextField(16); // dd/MM/yyyy HH:mm
	private JTextField txtDataHoraRegistro = new JTextField(16); // dd/MM/yyyy HH:mm
	private JTextField txtUsuarioRegistro = new JTextField(20);
	private JTextField txtValorSinistro = new JTextField(12);

	// Botões CRUD (Requisito 3)
	private JButton btnIncluir = new JButton("Incluir");
	private JButton btnAlterar = new JButton("Alterar");
	private JButton btnExcluir = new JButton("Excluir");
	private JButton btnLimpar = new JButton("Limpar");

	public TelaSinistro() {
		configurarJanela();
		construirLayout();
		configurarAcoes();
		limparFormulario();
	}

	private void configurarJanela() {
		setTitle("Registro e Controle de Sinistros");
		setSize(680, 560);
		setMinimumSize(new Dimension(600, 500));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void construirLayout() {
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Topo: Chave de Busca (Número do Sinistro)
		JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		painelBusca.setBorder(BorderFactory.createTitledBorder("Identificação do Sinistro"));
		painelBusca.add(new JLabel("Número do Sinistro:"));
		painelBusca.add(txtNumero);
		painelBusca.add(btnBuscar);
		painelPrincipal.add(painelBusca, BorderLayout.NORTH);

		// Centro: Formulário com Veículo e Detalhes da Ocorrência
		JPanel painelCentro = new JPanel();
		painelCentro.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Seção 1: Veículo do Sinistro
		JPanel painelVeiculo = new JPanel(new GridBagLayout());
		painelVeiculo.setBorder(BorderFactory.createTitledBorder("Veículo Envolvido"));
		GridBagConstraints gbcV = new GridBagConstraints();
		gbcV.insets = new Insets(5, 5, 5, 5);
		gbcV.fill = GridBagConstraints.HORIZONTAL;

		gbcV.gridx = 0; gbcV.gridy = 0;
		painelVeiculo.add(new JLabel("Placa do Veículo:"), gbcV);

		gbcV.gridx = 1;
		painelVeiculo.add(txtPlacaVeiculo, gbcV);

		gbcV.gridx = 2;
		painelVeiculo.add(btnVerificarVeiculo, gbcV);

		gbcV.gridx = 0; gbcV.gridy = 1; gbcV.gridwidth = 3;
		lblDadosVeiculo.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblDadosVeiculo.setForeground(new Color(51, 65, 85));
		painelVeiculo.add(lblDadosVeiculo, gbcV);

		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
		painelCentro.add(painelVeiculo, gbc);

		// Seção 2: Dados da Ocorrência e Registro
		JPanel painelOcorrencia = new JPanel(new GridBagLayout());
		painelOcorrencia.setBorder(BorderFactory.createTitledBorder("Dados da Ocorrência e Perícia"));
		GridBagConstraints gbcO = new GridBagConstraints();
		gbcO.insets = new Insets(5, 5, 5, 5);
		gbcO.fill = GridBagConstraints.HORIZONTAL;

		// Linha 0: Tipo do Sinistro (Enum dropdown list - Requisito 5)
		gbcO.gridx = 0; gbcO.gridy = 0;
		painelOcorrencia.add(new JLabel("Tipo do Sinistro:"), gbcO);

		gbcO.gridx = 1; gbcO.gridwidth = 3;
		cbTipo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof TipoSinistro) {
					TipoSinistro t = (TipoSinistro) value;
					setText(t.getCodigo() + " - " + t.getNome() + " (" + t.name() + ")");
				}
				return this;
			}
		});
		painelOcorrencia.add(cbTipo, gbcO);

		// Linha 1: Data/Hora Ocorrência e Data/Hora Registro
		gbcO.gridx = 0; gbcO.gridy = 1; gbcO.gridwidth = 1;
		painelOcorrencia.add(new JLabel("Data/Hora Ocorrência:"), gbcO);
		gbcO.gridx = 1;
		painelOcorrencia.add(txtDataHoraSinistro, gbcO);

		gbcO.gridx = 2;
		painelOcorrencia.add(new JLabel("Data/Hora Registro:"), gbcO);
		gbcO.gridx = 3;
		painelOcorrencia.add(txtDataHoraRegistro, gbcO);

		// Linha 2: Valor Estimado e Usuário do Registro
		gbcO.gridx = 0; gbcO.gridy = 2;
		painelOcorrencia.add(new JLabel("Valor do Prejuízo (R$):"), gbcO);
		gbcO.gridx = 1;
		painelOcorrencia.add(txtValorSinistro, gbcO);

		gbcO.gridx = 2;
		painelOcorrencia.add(new JLabel("Usuário Responsável:"), gbcO);
		gbcO.gridx = 3;
		painelOcorrencia.add(txtUsuarioRegistro, gbcO);

		gbc.gridx = 0; gbc.gridy = 1;
		painelCentro.add(painelOcorrencia, gbc);

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

		btnVerificarVeiculo.addActionListener(e -> verificarVeiculo());
	}

	private void executarBuscar() {
		String numero = txtNumero.getText().trim();
		if (StringUtils.ehNuloOuBranco(numero)) {
			JOptionPane.showMessageDialog(this, "Informe o número do sinistro para realizar a busca.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Sinistro sin = sinistroDAO.buscar(numero);
		if (sin == null) {
			JOptionPane.showMessageDialog(this, "Nenhum sinistro encontrado com o número informado.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
			btnAlterar.setEnabled(false);
			btnExcluir.setEnabled(false);
			btnIncluir.setEnabled(true);
		} else {
			preencherCampos(sin);
			btnAlterar.setEnabled(true);
			btnExcluir.setEnabled(true);
			btnIncluir.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Sinistro localizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void executarIncluir() {
		Sinistro sin = montarObjetoDaTela();
		if (sin == null) {
			return;
		}

		boolean sucesso = sinistroDAO.incluir(sin);
		if (!sucesso) {
			JOptionPane.showMessageDialog(this, "Já existe um sinistro registrado com este número.", "Erro na Inclusão", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Sinistro registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarAlterar() {
		Sinistro sin = montarObjetoDaTela();
		if (sin == null) {
			return;
		}

		boolean sucesso = sinistroDAO.alterar(sin);
		if (!sucesso) {
			JOptionPane.showMessageDialog(this, "Sinistro não encontrado para alteração.", "Erro na Alteração", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Sinistro alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarExcluir() {
		String numero = txtNumero.getText().trim();
		if (StringUtils.ehNuloOuBranco(numero)) {
			JOptionPane.showMessageDialog(this, "Informe o número do sinistro a ser excluído.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente excluir o sinistro número " + numero + "?",
				"Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			boolean sucesso = sinistroDAO.excluir(numero);
			if (!sucesso) {
				JOptionPane.showMessageDialog(this, "Sinistro não encontrado para exclusão.", "Erro", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Sinistro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				limparFormulario();
			}
		}
	}

	private Veiculo verificarVeiculo() {
		String placa = txtPlacaVeiculo.getText().trim();
		if (StringUtils.ehNuloOuBranco(placa)) {
			lblDadosVeiculo.setText("Informe a placa do veículo.");
			lblDadosVeiculo.setForeground(Color.RED);
			return null;
		}

		Veiculo v = veiculoDAO.buscar(placa);
		if (v != null) {
			String prop = "";
			if (v.getProprietarioPessoa() != null) {
				prop = "Proprietário: " + v.getProprietarioPessoa().getNome();
			} else if (v.getProprietarioEmpresa() != null) {
				prop = "Proprietário: " + v.getProprietarioEmpresa().getNome();
			}
			String cat = v.getCategoria() != null ? v.getCategoria().getNome() : "";
			lblDadosVeiculo.setText(String.format("Veículo: Ano %d | Categoria: %s | %s", v.getAno(), cat, prop));
			lblDadosVeiculo.setForeground(new Color(22, 101, 52));
			return v;
		} else {
			lblDadosVeiculo.setText("Veículo não encontrado com a placa informada.");
			lblDadosVeiculo.setForeground(Color.RED);
			return null;
		}
	}

	private Sinistro montarObjetoDaTela() {
		String numero = txtNumero.getText().trim();
		if (StringUtils.ehNuloOuBranco(numero)) {
			JOptionPane.showMessageDialog(this, "O número do sinistro deve ser informado.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		Veiculo v = verificarVeiculo();
		if (v == null) {
			JOptionPane.showMessageDialog(this, "É obrigatório associar um veículo válido e cadastrado ao sinistro.", "Veículo Inválido", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		TipoSinistro tipo = (TipoSinistro) cbTipo.getSelectedItem();

		LocalDateTime dataSinistro = parseLocalDateTime(txtDataHoraSinistro.getText().trim(), "Data/Hora do Sinistro");
		if (dataSinistro == null) return null;

		LocalDateTime dataRegistro = parseLocalDateTime(txtDataHoraRegistro.getText().trim(), "Data/Hora do Registro");
		if (dataRegistro == null) return null;

		String usuario = txtUsuarioRegistro.getText().trim();
		if (StringUtils.ehNuloOuBranco(usuario)) {
			JOptionPane.showMessageDialog(this, "O usuário de registro deve ser informado.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		BigDecimal valor = parseBigDecimal(txtValorSinistro.getText().trim(), "Valor do Sinistro");
		if (valor == null) return null;

		return new Sinistro(numero, v, dataSinistro, dataRegistro, usuario, valor, tipo);
	}

	private LocalDateTime parseLocalDateTime(String str, String nomeCampo) {
		if (StringUtils.ehNuloOuBranco(str)) {
			JOptionPane.showMessageDialog(this, nomeCampo + " deve ser informada (formato dd/MM/yyyy HH:mm).", "Campo Obrigatório", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		try {
			return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, nomeCampo + " inválida. Utilize o formato dd/MM/yyyy HH:mm (Ex: 03/10/2026 14:30).", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	private BigDecimal parseBigDecimal(String str, String nomeCampo) {
		if (StringUtils.ehNuloOuBranco(str)) {
			JOptionPane.showMessageDialog(this, nomeCampo + " deve ser informado.", "Campo Obrigatório", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		try {
			BigDecimal val = new BigDecimal(str.replace(",", "."));
			if (val.compareTo(BigDecimal.ZERO) < 0) {
				JOptionPane.showMessageDialog(this, nomeCampo + " não pode ser negativo.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			return val;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, nomeCampo + " inválido. Digite um valor numérico válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	private void preencherCampos(Sinistro sin) {
		txtNumero.setText(sin.getNumero());
		if (sin.getVeiculo() != null) {
			txtPlacaVeiculo.setText(sin.getVeiculo().getPlaca());
			verificarVeiculo();
		} else {
			txtPlacaVeiculo.setText("");
			lblDadosVeiculo.setText("Nenhum veículo associado.");
			lblDadosVeiculo.setForeground(Color.DARK_GRAY);
		}

		if (sin.getTipo() != null) {
			cbTipo.setSelectedItem(sin.getTipo());
		}

		txtDataHoraSinistro.setText(sin.getDataHoraSinistro() != null ? sin.getDataHoraSinistro().format(DATE_TIME_FORMATTER) : "");
		txtDataHoraRegistro.setText(sin.getDataHoraRegistro() != null ? sin.getDataHoraRegistro().format(DATE_TIME_FORMATTER) : "");
		txtUsuarioRegistro.setText(sin.getUsuarioRegistro() != null ? sin.getUsuarioRegistro() : "");
		txtValorSinistro.setText(sin.getValorSinistro() != null ? sin.getValorSinistro().toPlainString() : "0.00");
	}

	private void limparFormulario() {
		txtNumero.setText("");
		txtPlacaVeiculo.setText("");
		lblDadosVeiculo.setText("Nenhum veículo selecionado");
		lblDadosVeiculo.setForeground(new Color(51, 65, 85));

		cbTipo.setSelectedItem(TipoSinistro.COLISAO);
		txtDataHoraSinistro.setText("");
		txtDataHoraRegistro.setText(LocalDateTime.now().format(DATE_TIME_FORMATTER));
		txtUsuarioRegistro.setText(System.getProperty("user.name", "operador"));
		txtValorSinistro.setText("0.00");

		btnIncluir.setEnabled(true);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		txtNumero.requestFocus();
	}
}
