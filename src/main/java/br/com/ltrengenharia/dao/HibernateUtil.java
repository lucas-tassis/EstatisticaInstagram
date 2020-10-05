package br.com.ltrengenharia.dao;

import br.com.ltrengenharia.beans.Registro;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
  private static SessionFactory sf = null;
  
  private static SessionFactory getSessionFactory() {
    Configuration configuration = (new Configuration())
      .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
      .setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect")
      .setProperty("hibernate.show_sql", "false")
      .setProperty("hibernate.format_sql", "true")
      .setProperty("hibernate.hbm2ddl.auto", "update")
      .setProperty("hibernate.connection.url", "jdbc:sqlite:resources/Estatisticas Game Master.db")
      .setProperty("hibernate.jdbc.time_zone", "UTC")
      .setProperty("hibernate.event.merge.entity_copy_observer", "allow");
    configurarClasses(configuration);
    sf = configuration.buildSessionFactory();
    return sf;
  }
  
  private static void configurarClasses(Configuration configuration) {
    configuration.addAnnotatedClass(Registro.class);
  }
  
  public static Session openSession() {
    if (sf == null)
      getSessionFactory(); 
    return sf.openSession();
  }
}
