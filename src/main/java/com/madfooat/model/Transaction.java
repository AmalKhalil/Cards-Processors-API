package com.madfooat.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.madfooat.enums.TransactionStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@NotNull
	@Size(min = 36, max =36)
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String	transactionId;
	
	@NotNull
	@Max(value = 50000)
	private double	amount;
	
	@NotNull
	@Past
	private Date date;
	
	@CreationTimestamp
	private Date processingDate;
	
	@NotNull
	@Size(min = 10, max =10)
	@Pattern(regexp = "^[0-9]+$")
	private	String CustomerId;

	private String error;
	
	@Transient
	private String currency;
	
	@NotNull
	private TransactionStatus status;
	
	@OneToOne
	private Batch batch;
}
