package com.madfooat.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Batch {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@NotNull
	private String currency;

	private long nubmerOfValidTransaction;
	
	private double sumOfValidTransaction;
	
	private long nubmerOfInvalidTransaction;
	
	private double sumOfInvalidTransaction;

	private String merchant;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "batch_id")
	private List<Transaction> transactions = new ArrayList<>();
	
	public void increaseNubmerOfValidTransaction() {
		this.nubmerOfValidTransaction = this.nubmerOfValidTransaction + 1;
	}
	
	public void increaseNubmerOfInvalidTransaction() {
		this.nubmerOfInvalidTransaction = this.nubmerOfInvalidTransaction + 1;
	}
	
	public void addValidAmount(double amount) {
		this.sumOfValidTransaction = this.sumOfValidTransaction + amount;
	}

	public void addInvalidAmount(double amount) {
		this.sumOfInvalidTransaction = this.sumOfInvalidTransaction + amount;
	}
	
	public List<Map<String, Object>> getOutputMap() {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		Map<String, Object> row = new LinkedHashMap<>();
		row.put("Status", "S");
		row.put("Total", this.nubmerOfValidTransaction);
		row.put("Sum", this.sumOfValidTransaction);

		result.add(row);

		row = new LinkedHashMap<>();
		row.put("Status", "F");
		row.put("Total", this.nubmerOfInvalidTransaction);
		row.put("Sum", this.sumOfInvalidTransaction);
		result.add(row);

		return result;
	}
}
