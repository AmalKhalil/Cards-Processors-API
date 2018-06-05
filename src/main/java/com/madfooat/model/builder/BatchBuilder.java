package com.madfooat.model.builder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madfooat.exception.ApplicationException;
import com.madfooat.exception.ApplicationExceptionCode;
import com.madfooat.model.Batch;
import com.madfooat.model.Transaction;

@Service
public class BatchBuilder {
	
	@Autowired
	private TransactionBuilder transactionBuilder;

	public Batch build(String merchant, List<List<String>> records) {

		validateRecord(records);
		
		Batch batch = new Batch();
		batch.setMerchant(merchant);

		for (List<String> record : records) {
			Transaction transaction = transactionBuilder.built(record);
			validateCurrency(batch, transaction.getCurrency());

			transaction.setBatch(batch);
			batch.getTransactions().add(transaction);
		}

		return batch;
	
	}
	
	private void validateRecord(List<List<String>> records) {
		if (records == null || records.size() == 0)
			throw new ApplicationException("Empty Data : ", ApplicationExceptionCode.EMPTY_FILE);

	}
	
	private void validateCurrency(Batch batch, String transactionCurrency) {
		if (batch.getCurrency() == null)
			batch.setCurrency(transactionCurrency);

		else if (!batch.getCurrency().equals(transactionCurrency))
			throw new ApplicationException("Invalid transaction currency. It should be  " + transactionCurrency
					+ " instead of " + transactionCurrency, ApplicationExceptionCode.INVALIDE_TRANSACTION_CURRENCY);
	}
}
