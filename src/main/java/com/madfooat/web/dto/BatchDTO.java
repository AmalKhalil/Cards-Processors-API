package com.madfooat.web.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BatchDTO {


	private String currency;

	private long nubmerOfValidTransaction;
	
	private double sumOfValidTransaction;
	
	private long nubmerOfInvalidTransaction;
	
	private double sumOfInvalidTransaction;

	private String merchant;

	
}
