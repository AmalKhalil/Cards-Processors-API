package com.madfooat.model.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.madfooat.enums.TransactionStatus;
import com.madfooat.exception.ApplicationException;
import com.madfooat.exception.ApplicationExceptionCode;
import com.madfooat.model.Transaction;

@Service
public class TransactionBuilder {

	@Value("${transaction.dateformat}")
	private String dateFormat;

	public Transaction built(List<String> record) {
		validateRecord(record);
		Transaction transaction = new Transaction();
		transaction.setTransactionId(extractTransactionId(record));
		transaction.setAmount(extractAmount(record));
		transaction.setDate(extractDate(record));
		transaction.setCustomerId(extractCustomerId(record));
		transaction.setStatus(TransactionStatus.FAIL);
		transaction.setCurrency(extractCurrency(record));
		return transaction;
	}
	
	private String extractCurrency(List<String> record) {
		return record.get(2);
	}

	private String extractTransactionId(List<String> record) {
		return record.get(0);
	}

	private Double extractAmount(List<String> record) {
		return Double.valueOf(record.get(1));
	}

	private Date extractDate(List<String> record) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			return format.parse(record.get(3));
		} catch (ParseException e) {
			throw new ApplicationException(
					"Invalid transaction date formate. " + record.get(3) + " should follow " + dateFormat,
					ApplicationExceptionCode.INVALIDE_DATE_FORMAT);

		}
	}

	private String extractCustomerId(List<String> record) {
		return record.get(4);
	}
	
	private void validateRecord(List<String> record) {
		if (record.size() != 5)
			throw new ApplicationException("Invalid Record length " + record,
					ApplicationExceptionCode.INVALIDE_RECORD_SIZE);
	}

}
