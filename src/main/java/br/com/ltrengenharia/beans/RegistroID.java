package br.com.ltrengenharia.beans;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class RegistroID implements Serializable {
	private String username;
	private LocalDate data;
}