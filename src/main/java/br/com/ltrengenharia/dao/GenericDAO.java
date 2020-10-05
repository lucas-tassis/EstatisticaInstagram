package br.com.ltrengenharia.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.hibernate.Session;

public abstract class GenericDAO {
  private static Session session;
  
  public static <T> List<T> listarTodos(Class<T> entidade, String ordem) {
    List<T> result;
    session = HibernateUtil.openSession();
    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<T> cq = cb.createQuery(entidade);
      Root<T> root = cq.from(entidade);
      List<Order> orderList = new ArrayList<Order>();
      orderList.add(cb.desc((Expression)root.get(ordem)));
      cq.orderBy(orderList);
      result = session.createQuery(cq).getResultList();
    } finally {
      session.close();
    } 
    return result;
  }
  
  public static <T> void saveOrUpdate(T registro) {
    session = HibernateUtil.openSession();
    try {
      session.beginTransaction();
      session.saveOrUpdate(registro);
      session.getTransaction().commit();
      session.close();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    } 
  }
  
  public static <T> void merge(T registro) {
    session = HibernateUtil.openSession();
    try {
      session.beginTransaction();
      session.merge(registro);
      session.getTransaction().commit();
      session.close();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    } 
  }
  
  public static <T> void delete(T registro) {
    session = HibernateUtil.openSession();
    try {
      session.beginTransaction();
      session.delete(registro);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    } 
  }
  
  public <T> void refresh(T entidade) {
    session = HibernateUtil.openSession();
    try {
      session.beginTransaction();
      session.refresh(entidade);
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
      e.printStackTrace();
    } finally {
      session.close();
    } 
  }
  
  public static <T> T getByUsername(Class<T> entidade, String username) {
    T result;
    session = HibernateUtil.openSession();
    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<T> cq = cb.createQuery(entidade);
      Root<T> root = cq.from(entidade);
      cq.select((Selection)root).where((Expression)cb.equal((Expression)root.get("username"), username));
      result = session.createQuery(cq).getResultList().get(0);
    } finally {
      session.close();
    } 
    return result;
  }
  
  public static <T> T getByPK(Class<T> entidade, long pk) {
    T result;
    session = HibernateUtil.openSession();
    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<T> cq = cb.createQuery(entidade);
      Root<T> root = cq.from(entidade);
      cq.select((Selection)root).where((Expression)cb.equal((Expression)root.get("pk"), Long.valueOf(pk)));
      result = session.createQuery(cq).getResultList().get(0);
    } finally {
      session.close();
    } 
    return result;
  }
}
