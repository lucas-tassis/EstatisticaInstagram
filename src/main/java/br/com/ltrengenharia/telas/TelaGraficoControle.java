package br.com.ltrengenharia.telas;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUser;

import br.com.ltrengenharia.Inicio;
import br.com.ltrengenharia.beans.Registro;
import br.com.ltrengenharia.dao.GenericDAO;
import br.com.ltrengenharia.dao.RegistroDAO;
import br.com.ltrengenharia.dialogs.Dialogs;
import br.com.ltrengenharia.exceptions.LoginException;
import br.com.ltrengenharia.instagram.Login;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TelaGraficoControle implements iTelaGraficoControle {
	private List<String> perfis;

	public static final int DIARIO = 0;

	public static final int SEMANAL = 1;

	public static final int QUINZENAL = 2;

	public static final int MENSAL = 3;

	public void init() {
		setPerfis();
	}

	private void setPerfis() {
		this.perfis = Inicio.getPerfis();
	}

	public void createSeries(List<XYChart.Series<String, Integer>> series) {
		for (String s : this.perfis) {
			XYChart.Series<String, Integer> serie = new XYChart.Series();
			serie.setName(s);
			series.add(serie);
		}
	}

	public void baixarNSeguidores() {
		Login login = new Login();
		try {
			login.logar("testeltr6", "ltr121212");
			SalvarNSeguidores(login.getInstagram4j());
		} catch (LoginException e) {
			Dialogs.showErro("Falha ao logar " + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Dialogs.showErro("ClientProtocolException " + e.getMessage());
			e.printStackTrace();
			e.printStackTrace();
		} catch (IOException e) {
			Dialogs.showErro("Perfil nlocalizado " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void SalvarNSeguidores(Instagram4j instagram) throws ClientProtocolException, IOException {
		for (String p : this.perfis) {
			InstagramUser iu = ((InstagramSearchUsernameResult) instagram
					.sendRequest((InstagramRequest) new InstagramSearchUsernameRequest(p))).getUser();
			Registro r = RegistroDAO.getByDateOrRetunrNew(LocalDate.now(), p);
			r.setNSeguidores(iu.getFollower_count());
			GenericDAO.saveOrUpdate(r);
		}
		Dialogs.showInformacao("Dados salvos");
	}

	public void carregarDados(XYChart.Series<String, Integer> serie, LocalDate inicio, LocalDate fim, int periodo) {
		List<Registro> dados = new ArrayList<Registro>();
		switch (periodo) {
		case 0:
			dados = RegistroDAO.getAllDayByUsername(serie.getName(), inicio, fim);
			Collections.reverse(dados);
			break;
		case 1:
			dados = RegistroDAO.getWeekByUsername(serie.getName(), inicio, fim);
			break;
		case 2:
			dados = RegistroDAO.getFortnightByUsername(serie.getName(), inicio, fim);
			break;
		case 3:
			dados = RegistroDAO.getMouthByUsername(serie.getName(), inicio, fim);
			break;
		}
		for (Registro r : dados) {
			XYChart.Data<String, Integer> data = new XYChart.Data("" + r.getId().getData(),r.getNSeguidores());
			data.setNode((Node) new HoveredThresholdNode(r.getNSeguidores()));
			serie.getData().add(data);
		}
	}

	private class HoveredThresholdNode extends StackPane {
		HoveredThresholdNode(int value) {
			setPrefSize(30, 20);
			final Label label = new Label("" + value);
			label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			getChildren().setAll(label);
			toFront();

		}
	}
}
