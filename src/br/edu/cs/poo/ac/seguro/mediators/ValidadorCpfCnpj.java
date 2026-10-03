package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {

	private ValidadorCpfCnpj() {
	}

	public static boolean ehCpfValido(String cpf) {
		if (StringUtils.ehNuloOuBranco(cpf)) {
			return false;
		}
		if (!StringUtils.temSomenteNumeros(cpf)) {
			return false;
		}
		if (cpf.length() != 11) {
			return false;
		}

		boolean todosIguais = true;
		for (int i = 1; i < 11; i++) {
			if (cpf.charAt(i) != cpf.charAt(0)) {
				todosIguais = false;
				break;
			}
		}
		if (todosIguais) {
			return false;
		}

		int soma1 = 0;
		for (int i = 0; i < 9; i++) {
			soma1 += (cpf.charAt(i) - '0') * (10 - i);
		}
		int resto1 = soma1 % 11;
		int d1 = (resto1 < 2) ? 0 : (11 - resto1);
		if ((cpf.charAt(9) - '0') != d1) {
			return false;
		}

		int soma2 = 0;
		for (int i = 0; i < 10; i++) {
			soma2 += (cpf.charAt(i) - '0') * (11 - i);
		}
		int resto2 = soma2 % 11;
		int d2 = (resto2 < 2) ? 0 : (11 - resto2);
		return (cpf.charAt(10) - '0') == d2;
	}

	public static boolean ehCnpjValido(String cnpj) {
		if (StringUtils.ehNuloOuBranco(cnpj)) {
			return false;
		}
		if (!StringUtils.temSomenteNumeros(cnpj)) {
			return false;
		}
		if (cnpj.length() != 14) {
			return false;
		}

		boolean todosIguais = true;
		for (int i = 1; i < 14; i++) {
			if (cnpj.charAt(i) != cnpj.charAt(0)) {
				todosIguais = false;
				break;
			}
		}
		if (todosIguais) {
			return false;
		}

		int[] pesos1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		int soma1 = 0;
		for (int i = 0; i < 12; i++) {
			soma1 += (cnpj.charAt(i) - '0') * pesos1[i];
		}
		int resto1 = soma1 % 11;
		int d1 = (resto1 < 2) ? 0 : (11 - resto1);
		if ((cnpj.charAt(12) - '0') != d1) {
			return false;
		}

		int[] pesos2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		int soma2 = 0;
		for (int i = 0; i < 13; i++) {
			soma2 += (cnpj.charAt(i) - '0') * pesos2[i];
		}
		int resto2 = soma2 % 11;
		int d2 = (resto2 < 2) ? 0 : (11 - resto2);
		return (cnpj.charAt(13) - '0') == d2;
	}
}
