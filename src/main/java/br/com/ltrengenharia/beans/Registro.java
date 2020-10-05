package br.com.ltrengenharia.beans;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data
public class Registro {
  @EmbeddedId
  RegistroID id;
  
  private int nSeguidores;
}