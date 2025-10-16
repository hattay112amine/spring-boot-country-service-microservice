package com.countryservice.demo.beans;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name="country")
public class Country {
	
	@Id
	@Column(name="id")
	private int idCountry;
	@Column(name="name_country")
	private String name;
	@Column(name="capital_name")
	private String Capital;
	 public Country(int id, String name, String capital) {
	        this.idCountry = id;
	        this.name = name;
	        this.Capital = capital;
	    }
	public int getIdCountry() {
		return idCountry;
	}
	public void setIdCountry(int idCountry) {
		this.idCountry = idCountry;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCapital() {
		return Capital;
	}
	public void setCapital(String capital) {
		Capital = capital;
	}

}