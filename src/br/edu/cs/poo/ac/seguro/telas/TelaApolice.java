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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.StringUtils;

public class TelaApolice extends JFrame {

	private static final long serialVersionUID = 1L;

	// Atributos de persistência (Requisito 4)
	private ApoliceDAO apoliceDAO = new ApoliceDAO();
	private VeiculoDAO veiculoDAO = new VeiculoDAO();

	// Chave de Busca
	private JTextField txtNumero = new JTextField(15);
	private JButton btnBuscar = new JButton("Buscar");

	// Dados do Veículo Associado
	private JTextField txtPlacaVeiculo = new JTextField(10);
	private JButton btnVerificarVeiculo = new JButton("Verificar Veículo");
	private JLabel lblDadosVeiculo = new JLabel("Nenhum veículo selecionado");

	// Valores Financeiros da Apólice (Requisito 5)
	private JTextField txtValorFranquia = new JTextField(12);
	private JTextField txtValorPremio = new JTextField(12);
	private JTextField txtValorMaximoSegurado = new JTextField(12);

	// Botões CRUD (Requisito 3)
	private JButton btnIncluir = new JButton("Incluir");
	private JButton btnAlterar = new JButton("Alterar");
	private JButton btnExcluir = new JButton("Excluir");
	private JButton btnLimpar = new JButton("Limpar");

	public TelaApolice() {
		configurarJanela();
		construirLayout();
		configurarAcoes();
		limparFormulario();
	}

	private void configurarJanela() {
		setTitle("Gestão de Apólices de Seguro");
		setSize(650, 480);
		setMinimumSize(new Dimension(580, 420));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void construirLayout() {
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Topo: Chave de Busca (Número da Apólice)
		JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		painelBusca.setBorder(BorderFactory.createTitledBorder("Identificação da Apólice"));
		painelBusca.add(new JLabel("Número da Apólice:"));
		painelBusca.add(txtNumero);
		painelBusca.add(btnBuscar);
		painelPrincipal.add(painelBusca, BorderLayout.NORTH);

		// Centro: Formulário com Veículo e Valores Financeiros
		JPanel painelCentro = new JPanel();
		painelCentro.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Seção 1: Veículo Segurado
		JPanel painelVeiculo = new JPanel(new GridBagLayout());
		painelVeiculo.setBorder(BorderFactory.createTitledBorder("Veículo Segurado"));
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

		// Seção 2: Condições Financeiras
		JPanel painelFinanceiro = new JPanel(new GridBagLayout());
		painelFinanceiro.setBorder(BorderFactory.createTitledBorder("Condições Financeiras"));
		GridBagConstraints gbcF = new GridBagConstraints();
		gbcF.insets = new Insets(5, 5, 5, 5);
		gbcF.fill = GridBagConstraints.HORIZONTAL;

		gbcF.gridx = 0; gbcF.gridy = 0;
		painelFinanceiro.add(new JLabel("Valor da Franquia (R$):"), gbcF);
		gbcF.gridx = 1;
		painelFinanceiro.add(txtValorFranquia, gbcF);

		gbcF.gridx = 0; gbcF.gridy = 1;
		painelFinanceiro.add(new JLabel("Valor do Prêmio (R$):"), gbcF);
		gbcF.gridx = 1;
		painelFinanceiro.add(txtValorPremio, gbcF);

		gbcF.gridx = 0; gbcF.gridy = 2;
		painelFinanceiro.add(new JLabel("Valor Máximo Segurado (R$):"), gbcF);
		gbcF.gridx = 1;
		painelFinanceiro.add(txtValorMaximoSegurado, gbcF);

		gbc.gridx = 0; gbc.gridy = 1;
		painelCentro.add(painelFinanceiro, gbc);

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
			JOptionPane.showMessageDialog(this, "Informe o número da apólice para realizar a busca.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Apolice ap = apoliceDAO.buscar(numero);
		if (ap == null) {
			JOptionPane.showMessageDialog(this, "Nenhuma apólice encontrada com o número informado.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
			btnAlterar.setEnabled(false);
			btnExcluir.setEnabled(false);
			btnIncluir.setEnabled(true);
		} else {
			preencherCampos(ap);
			btnAlterar.setEnabled(true);
			btnExcluir.setEnabled(true);
			btnIncluir.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Apólice localizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void executarIncluir() {
		Apolice ap = montarObjetoDaTela();
		if (ap == null) {
			return;
		}

		boolean sucesso = apoliceDAO.incluir(ap);
		if (!sucesso) {
			JOptionPane.showMessageDialog(this, "Já existe uma apólice cadastrada com este número.", "Erro na Inclusão", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Apólice cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarAlterar() {
		Apolice ap = montarObjetoDaTela();
		if (ap == null) {
			return;
		}

		boolean sucesso = apoliceDAO.alterar(ap);
		if (!sucesso) {
			JOptionPane.showMessageDialog(this, "Apólice não encontrada para alteração.", "Erro na Alteração", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Apólice alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
			limparFormulario();
		}
	}

	private void executarExcluir() {
		String numero = txtNumero.getText().trim();
		if (StringUtils.ehNuloOuBranco(numero)) {
			JOptionPane.showMessageDialog(this, "Informe o número da apólice a ser excluída.", "Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"Deseja realmente excluir a apólice número " + numero + "?",
				"Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			boolean sucesso = apoliceDAO.excluir(numero);
			if (!sucesso) {
				JOptionPane.showMessageDialog(this, "Apólice não encontrada para exclusão.", "Erro", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Apólice excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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

	private Apolice montarObjetoDaTela() {
		String numero = txtNumero.getText().trim();
		if (StringUtils.ehNuloOuBranco(numero)) {
			JOptionPane.showMessageDialog(this, "O número da apólice deve ser informado.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		Veiculo v = verificarVeiculo();
		if (v == null) {
			JOptionPane.showMessageDialog(this, "É obrigatório associar um veículo válido e cadastrado à apólice.", "Veículo Inválido", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		BigDecimal franquia = parseBigDecimal(txtValorFranquia.getText().trim(), "Valor da Franquia");
		if (franquia == null) return null;

		BigDecimal premio = parseBigDecimal(txtValorPremio.getText().trim(), "Valor do Prêmio");
		if (premio == null) return null;

		BigDecimal maximoSegurado = parseBigDecimal(txtValorMaximoSegurado.getText().trim(), "Valor Máximo Segurado");
		if (maximoSegurado == null) return null;

		Apolice apolice = new Apolice(v, franquia, premio, maximoSegurado);
		apolice.setNumero(numero);
		return apolice;
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
			JOptionPane.showMessageDialog(this, nomeCampo + " inválido. Digite um número decimal válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	private void preencherCampos(Apolice ap) {
		txtNumero.setText(ap.getNumero());
		if (ap.getVeiculo() != null) {
			txtPlacaVeiculo.setText(ap.getVeiculo().getPlaca());
			verificarVeiculo();
		} else {
			txtPlacaVeiculo.setText("");
			lblDadosVeiculo.setText("Nenhum veículo associado.");
			lblDadosVeiculo.setForeground(Color.DARK_GRAY);
		}

		txtValorFranquia.setText(ap.getValorFranquia() != null ? ap.getValorFranquia().toPlainString() : "0.00");
		txtValorPremio.setText(ap.getValorPremio() != null ? ap.getValorPremio().toPlainString() : "0.00");
		txtValorMaximoSegurado.setText(ap.getValorMaximoSegurado() != null ? ap.getValorMaximoSegurado().toPlainString() : "0.00");
	}

	private void limparFormulario() {
		txtNumero.setText("");
		txtPlacaVeiculo.setText("");
		lblDadosVeiculo.setText("Nenhum veículo selecionado");
		lblDadosVeiculo.setForeground(new Color(51, 65, 85));

		txtValorFranquia.setText("0.00");
		txtValorPremio.setText("0.00");
		txtValorMaximoSegurado.setText("0.00");

		btnIncluir.setEnabled(true);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		txtNumero.requestFocus();
	}
}
