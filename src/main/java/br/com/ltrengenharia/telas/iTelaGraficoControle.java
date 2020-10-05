package br.com.ltrengenharia.telas;

import java.time.LocalDate;
import java.util.List;
import javafx.scene.chart.XYChart;

public interface iTelaGraficoControle {
  void init();
  
  void createSeries(List<XYChart.Series<String, Integer>> paramList);
  
  void baixarNSeguidores();
  
  void carregarDados(XYChart.Series<String, Integer> paramSeries, LocalDate paramLocalDate1, LocalDate paramLocalDate2, int paramInt);
}
