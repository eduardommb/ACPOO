package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;

public class TesteSinistroDAO extends TesteDAO{
	private SinistroDAO dao = new SinistroDAO();
	protected Class getClasse() {
		return Sinistro.class;
	}
	
	@Test
	public void teste01() {
		String numero = "10";
		cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO), numero);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNotNull(si);
	}
	@Test
	public void teste02() {
		String numero = "20";
		cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO), numero);
		Sinistro si = dao.buscar("11");
		Assertions.assertNull(si);
	}
	@Test
	public void teste03() {
		String numero = "30";
		cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
	}
	@Test
	public void teste04() {
		String numero = "40";
		cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), null, "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO), numero);
		boolean ret = dao.excluir("41");
		Assertions.assertFalse(ret);
	}
	@Test
	public void teste05() {
		String numero = "50";
		boolean ret = dao.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO));
		Assertions.assertTrue(ret);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNotNull(si);
	}
	@Test
	public void teste06() {
		String numero = "60";
		Sinistro si = new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO);	
		cadastro.incluir(si, numero);
		boolean ret = dao.incluir(si);
		Assertions.assertFalse(ret);
	}
	@Test
	public void teste07() {
		String numero = "70";
		boolean ret = dao.alterar(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO));
		Assertions.assertFalse(ret);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNull(si);
	}
	@Test
	public void teste08() {
		String numero = "80";
		Sinistro si = new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIO", BigDecimal.ZERO, TipoSinistro.COLISAO);	
		cadastro.incluir(si, numero);
		si = new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), "USUARIA", BigDecimal.ZERO, TipoSinistro.INCENDIO);	
		boolean ret = dao.alterar(si);
		Assertions.assertTrue(ret);
	}
	
}
