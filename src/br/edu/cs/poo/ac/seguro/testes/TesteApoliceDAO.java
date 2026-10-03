package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class TesteApoliceDAO extends TesteDAO {
	private ApoliceDAO dao = new ApoliceDAO();

	protected Class getClasse() {
		return Apolice.class;
	}

	@Test
	public void teste01() {
		String numero = "10";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		cadastro.incluir(ap, numero);
		Apolice buscada = dao.buscar(numero);
		Assertions.assertNotNull(buscada);
	}

	@Test
	public void teste02() {
		String numero = "20";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		cadastro.incluir(ap, numero);
		Apolice buscada = dao.buscar("11");
		Assertions.assertNull(buscada);
	}

	@Test
	public void teste03() {
		String numero = "30";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		cadastro.incluir(ap, numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
	}

	@Test
	public void teste04() {
		String numero = "40";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		cadastro.incluir(ap, numero);
		boolean ret = dao.excluir("41");
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste05() {
		String numero = "50";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		boolean ret = dao.incluir(ap);
		Assertions.assertTrue(ret);
		Apolice buscada = dao.buscar(numero);
		Assertions.assertNotNull(buscada);
	}

	@Test
	public void teste06() {
		String numero = "60";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		cadastro.incluir(ap, numero);
		boolean ret = dao.incluir(ap);
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste07() {
		String numero = "70";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		boolean ret = dao.alterar(ap);
		Assertions.assertFalse(ret);
		Apolice buscada = dao.buscar(numero);
		Assertions.assertNull(buscada);
	}

	@Test
	public void teste08() {
		String numero = "80";
		Apolice ap = new Apolice(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		ap.setNumero(numero);
		cadastro.incluir(ap, numero);
		Apolice apAlterada = new Apolice(null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
		apAlterada.setNumero(numero);
		boolean ret = dao.alterar(apAlterada);
		Assertions.assertTrue(ret);
	}

}
