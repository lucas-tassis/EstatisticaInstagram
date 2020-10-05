package br.com.ltrengenharia.telas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaGrafico extends Application {
	private AnchorPane pane;
	private Scene scene;
	private Stage stage;
	
	private LineChart graficoLinha;
	private NumberAxis eixo;
	
	private DatePicker dtInicio;
	private DatePicker dtFim;
	
	private List<XYChart.Series<String, Integer>> series;
	
	private Button BaixarDados;
	
	private ToggleGroup group;
	private RadioButton diario;
	private RadioButton semanal;
	private RadioButton quinzenal;
	private RadioButton mensal;

	private iTelaGraficoControle controle;

	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setTitle("Estatísticas");
		initPanes();
		initScene();
		initButton();
		initListeners();
		initRadio();
		initDataPicker();
		initGrafico();
		controle = new TelaGraficoControle();
		controle.init();
		addSerie();
		configStage();
		initLayout(); 
	}

	private void initPanes() {
		pane = new AnchorPane();
		pane.setPrefSize(800, 550);
		pane.getStyleClass().add("paneGrafico");
	}

	private void addToPanel(Control c) {
		pane.getChildren().add(c);
	}

	private void addToPanel(Chart c) {
		pane.getChildren().add(c);
	}

	private void initScene() {
		scene = new Scene((Parent) pane);
		scene.getStylesheets().add(getClass().getResource("telagrafico.css").toExternalForm());
	}

	private void initButton() {
		BaixarDados = new Button("Atualizar");
		BaixarDados.getStyleClass().add("btSalvar");
		setToolTip((Control) BaixarDados, "Salvar a quantidade de seguidores atual dos perfis");
		addToPanel((Control) BaixarDados);
	}

	private void setToolTip(Control c, String s) {
		Tooltip tooltip = new Tooltip();
		tooltip.setText(s);
		tooltip.setShowDelay(new Duration(100.0D));
		c.setTooltip(tooltip);
	}

	private void initListeners() {
		BaixarDados.setOnAction(e -> { 
			Thread thread = new Thread(() -> {
				scene.setCursor(Cursor.WAIT);
				controle.baixarNSeguidores(); 
				testaThreadAndResetSeries();
				scene.setCursor(Cursor.DEFAULT);
			});
			thread.start();
		});

	}

	private void testaThreadAndResetSeries() {
		if (Platform.isFxApplicationThread()) {
			graficoLinha.getData().removeAll(series);
			if (diario.isSelected()) {
				for (XYChart.Series<String, Integer> serie : series) {
					controle.carregarDados(serie, dtInicio.getValue(), dtFim.getValue(),
							TelaGraficoControle.DIARIO);
				}
			} else if (semanal.isSelected()) {
				for (XYChart.Series<String, Integer> serie : series) {
					controle.carregarDados(serie, dtInicio.getValue(), dtFim.getValue(),
							TelaGraficoControle.SEMANAL);
				}
			} else if (quinzenal.isSelected()) {
				for (XYChart.Series<String, Integer> serie : series) {
					controle.carregarDados(serie, dtInicio.getValue(), dtFim.getValue(),
							TelaGraficoControle.QUINZENAL);
				}
			} else {
				for (XYChart.Series<String, Integer> serie : series) {
					controle.carregarDados(serie, dtInicio.getValue(), dtFim.getValue(),
							TelaGraficoControle.MENSAL);
				}
			}
			addSerie();
		} else {
			Platform.runLater(new Runnable() {
				public void run() {
					graficoLinha.getData().removeAll(series);
					addSerie();
				}
			});
		}
	}

	private void initRadio() {
		group = new ToggleGroup();
		diario = new RadioButton("Diario");
		diario.setToggleGroup(group);
		diario.setSelected(true);
		addToPanel((Control) diario);
		setCorBrancoTexto((Control) diario);
		semanal = new RadioButton("Semanal");
		semanal.setToggleGroup(group);
		addToPanel((Control) semanal);
		setCorBrancoTexto((Control) semanal);
		quinzenal = new RadioButton("Quinzenal");
		quinzenal.setToggleGroup(group);
		addToPanel((Control) quinzenal);
		setCorBrancoTexto((Control) quinzenal);
		mensal = new RadioButton("Mensal");
		mensal.setToggleGroup(group);
		addToPanel((Control) mensal);
		setCorBrancoTexto((Control) mensal);
	}

	private void initDataPicker() {
		dtInicio = new DatePicker(LocalDate.now().minusYears(1L));
		addToPanel((Control) dtInicio);
		dtFim = new DatePicker(LocalDate.now());
		addToPanel((Control) dtFim);
	}

	private void setCorBrancoTexto(Control c) {
		c.setStyle("-fx-text-fill: white;");
	}

	private void initGrafico() {
		eixo = new NumberAxis();
		eixo.setAutoRanging(true);
		eixo.setTickUnit(100.0D);
		graficoLinha = new LineChart((Axis) new CategoryAxis(), (Axis) eixo);
		graficoLinha.getStyleClass().add("axis");
		addToPanel((Chart) graficoLinha);
	}

	private void addSerie() {
		series = new ArrayList<Series<String, Integer>>();
		controle.createSeries(series);
		if (diario.isSelected()) {
			for (XYChart.Series<String, Integer> serie : series)
				controle.carregarDados(serie, (LocalDate) dtInicio.getValue(), (LocalDate) dtFim.getValue(), 0);
		} else if (semanal.isSelected()) {
			for (XYChart.Series<String, Integer> serie : series)
				controle.carregarDados(serie, (LocalDate) dtInicio.getValue(), (LocalDate) dtFim.getValue(), 1);
		} else if (quinzenal.isSelected()) {
			for (XYChart.Series<String, Integer> serie : series)
				controle.carregarDados(serie, (LocalDate) dtInicio.getValue(), (LocalDate) dtFim.getValue(), 2);
		} else {
			for (XYChart.Series<String, Integer> serie : series)
				controle.carregarDados(serie, (LocalDate) dtInicio.getValue(), (LocalDate) dtFim.getValue(), 3);
		}
		graficoLinha.getData().addAll(series);
	}

	private void initLayout() {
		int distanciaV = 30;
		int distanciaH = 30;
		int margem = 10;
		dtInicio.setLayoutX(margem);
		dtInicio.setLayoutY(margem);
		dtFim.setLayoutX(pane.getWidth() - dtFim.getWidth() - (margem * 2));
		dtFim.setLayoutY(margem);
		diario.setLayoutX(240.0D);
		diario.setLayoutY(dtInicio.getLayoutY() + distanciaV);
		diario.setPrefWidth(100.0D);
		semanal.setLayoutX(diario.getLayoutX() + diario.getWidth() + distanciaH);
		semanal.setLayoutY(diario.getLayoutY());
		semanal.setPrefWidth(100.0D);
		quinzenal.setLayoutX(semanal.getLayoutX() + semanal.getWidth() + distanciaH);
		quinzenal.setLayoutY(diario.getLayoutY());
		quinzenal.setPrefWidth(100.0D);
		mensal.setLayoutX(quinzenal.getLayoutX() + quinzenal.getWidth() + distanciaH);
		mensal.setLayoutY(diario.getLayoutY());
		mensal.setPrefWidth(100.0D);
		graficoLinha.setLayoutX(margem);
		graficoLinha.setLayoutY(diario.getLayoutY() + distanciaV);
		graficoLinha.setPrefWidth(780.0D);
		BaixarDados.setLayoutX((pane.getWidth() - BaixarDados.getWidth()) / 2.0D);
		BaixarDados.setLayoutY(graficoLinha.getLayoutY() + graficoLinha.getHeight() + distanciaV);
	}

	private void configStage() {
		stage.setScene(scene);
		stage.setResizable(false);
		Image applicationIcon = new Image(getClass().getResourceAsStream("icone.jpg"));
		stage.getIcons().add(applicationIcon);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
