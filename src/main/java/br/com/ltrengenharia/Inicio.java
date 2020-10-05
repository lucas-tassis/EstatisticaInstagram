package br.com.ltrengenharia;

import br.com.ltrengenharia.telas.TelaGrafico;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Inicio {
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private static String username;
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private static String password;
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private static List<String> perfis; 

	public static void main(String[] arg) {
		testaDiretorio();
		configFrameworks(); 
		TelaGrafico.main(arg);
	}

	private static void configFrameworks() {
		configPerfis();
		Logger.getRootLogger().setLevel(Level.WARN);
	}

	private static void configPerfis() {
		perfis = new ArrayList<String>();
		perfis.add("gamemaster.vix");
		perfis.add("tabernageek"); 
		perfis.add("boards4.u");
		perfis.add("mesasecreta");
		perfis.add("lojanerds");
		perfis.add("velhinhodorpg");
		perfis.add("dungeoncapixaba");
		perfis.add("boardgamessp");
	}

	private static void testaDiretorio() {
		String caminho = System.getProperty("user.dir");
		File diretorio = new File(String.valueOf(caminho) + "/resources");
		if (!diretorio.exists())
			diretorio.mkdirs();
	}
}