package br.com.ltrengenharia.dao;

import br.com.ltrengenharia.beans.Registro;
import br.com.ltrengenharia.beans.RegistroID;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.hibernate.Session;

public class RegistroDAO extends GenericDAO {
  private static Session session;
  
  private static CriteriaBuilder cb;
  
  private static CriteriaQuery<Registro> cq;
  
  private static Root<Registro> root;
  
  private static void initComponents() {
    session = HibernateUtil.openSession();
    cb = session.getCriteriaBuilder();
    cq = cb.createQuery(Registro.class);
    root = cq.from(Registro.class);
  }
  
  public static Registro getByDateOrRetunrNew(LocalDate date, String username) {
    Registro result = getByDate(date, username);
    if (result == null) {
      result = new Registro();
      RegistroID id = new RegistroID();
      id.setData(LocalDate.now());
      id.setUsername(username);
      result.setId(id);
    } 
    session.close();
    return result;
  }
  
  private static Registro getByDate(LocalDate date, String username) {
    try {
      initComponents();
      cq.select((Selection)root).where(new Predicate[] { cb.equal((Expression)root.get("id").get("data"), date), 
            cb.equal((Expression)root.get("id").get("username"), username) });
      Registro r = (Registro)session.createQuery(cq).getSingleResult();
      return r;
    } catch (Exception e) {
      return null;
    } 
  }
  
  public static List<Registro> getAllDayByUsername(String username, LocalDate inicio, LocalDate fim) {
    initComponents();
    cq.select((Selection)root).where(new Predicate[] { cb.equal((Expression)root.get("id").get("username"), username), 
          cb.greaterThanOrEqualTo((Expression)root.get("id").get("data"), inicio), 
          cb.lessThanOrEqualTo((Expression)root.get("id").get("data"), fim) });
    List<Registro> result = session.createQuery(cq).getResultList();
    session.close();
    return result;
  }
  
  public static List<Registro> getWeekByUsername(String username, LocalDate inicio, LocalDate fim) {
    List<Registro> result = new ArrayList<Registro>();
    while (inicio.isBefore(fim)) {
      Registro r = getByDate(inicio, username);
      if (r != null)
        result.add(r); 
      inicio = inicio.plusDays(7L);
    } 
    result.add(getActualDay(username));
    return result;
  }
  
  public static List<Registro> getFortnightByUsername(String username, LocalDate inicio, LocalDate fim) {
    List<Registro> result = new ArrayList<Registro>();
    while (inicio.isBefore(fim)) {
      Registro r = getByDate(inicio, username);
      if (r != null)
        result.add(r); 
      inicio = inicio.plusDays(15L);
    } 
    result.add(getActualDay(username));
    return result;
  }
  
  public static List<Registro> getMouthByUsername(String username, LocalDate inicio, LocalDate fim) {
    List<Registro> result = new ArrayList<Registro>();
    while (inicio.isBefore(fim)) {
      Registro r = getByDate(inicio, username);
      if (r != null)
        result.add(r); 
      inicio = inicio.plusMonths(1L);
    } 
    result.add(getActualDay(username));
    return result;
  }
  
  private static Registro getActualDay(String username) {
    Registro r = getByDate(LocalDate.now(), username);
    session.close();
    return r;
  }
}
