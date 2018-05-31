package com.madfooat.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.madfooat.enums.Role;
import com.madfooat.enums.TransactionStatus;
import com.madfooat.exception.ApplicationException;
import com.madfooat.exception.ApplicationExceptionCode;
import com.madfooat.model.Batch;
import com.madfooat.model.Transaction;
import com.madfooat.model.User;
import com.madfooat.repository.BatchRepository;
import com.madfooat.repository.UserRepository;;

@Service
public class TransactionService {

	private static final Logger LOGGER = Logger.getLogger(TransactionService.class);

	@Value("${transaction.dateformat}")
	private String dateFormat;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BatchRepository batchRepository;

	private Validator validator;

	public TransactionService() {
		super();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	public List<Map<String, Object>> processBatch(String merchantName, List<List<String>> records) {

		isMerchantExist(merchantName);

		Batch batch = createBatch(merchantName, records);

		for (Transaction transaction : batch.getTransactions()) {

			validateTransaction(transaction);

			calculateOutput(batch, transaction);

		}
		
		batchRepository.save(batch);
		
		return prepareOutput(batch);

	}
	
	private void isMerchantExist(String merchantName) {
		User merchant = userRepository.findByUserNameAndRole(merchantName, Role.Merchant);

		if (merchant == null)
			throw new ApplicationException("Merchant " + merchantName + " Not found in System",
					ApplicationExceptionCode.UNKNOW_MERCHANT);
	}

	private List<Map<String, Object>> prepareOutput(Batch batch) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> row = new LinkedHashMap<>(); 
		row.put("Status", "S");
		row.put("Total", batch.getNubmerOfValidTransaction());
		row.put("Sum", batch.getSumOfValidTransaction());
		
		result.add(row);
		
		row = new LinkedHashMap<>(); 
		row.put("Status", "F");
		row.put("Total", batch.getNubmerOfInvalidTransaction());
		row.put("Sum", batch.getSumOfInvalidTransaction());
		result.add(row);
		
		return result;
	}

	private void calculateOutput(Batch batch, Transaction transaction) {
		if (TransactionStatus.SUCCESS.equals(transaction.getStatus())) {
			batch.increaseNubmerOfValidTransaction();
			batch.addValidAmount(transaction.getAmount());
		} else {
			batch.increaseNubmerOfInvalidTransaction();
			batch.addInvalidAmount(transaction.getAmount());
		}
	}

	private Batch createBatch(String merchantName, List<List<String>> records) {
		
		if (records == null || records.size() == 0)
			throw new ApplicationException("Empty Data : " , ApplicationExceptionCode.EMPTY_FILE);

		
		Batch batch = new Batch();
		batch.setMerchant(merchantName);
		
		for (List<String> record : records) {

			validateRecord(record);

			validateCurrency(batch, extractCurrency(record));

			Transaction transaction = parseRecord(record);
			
			transaction.setBatch(batch);
			batch.getTransactions().add(transaction);
		}
		
		return batch;
	}

	private void validateCurrency(Batch batch, String transactionCurrency) {
		if (batch.getCurrency() == null)
			batch.setCurrency(transactionCurrency);

		else if (!batch.getCurrency().equals(transactionCurrency))
			throw new ApplicationException("Invalid transaction currency. It should be  " + transactionCurrency
					+ " instead of " + transactionCurrency, ApplicationExceptionCode.INVALIDE_TRANSACTION_CURRENCY);
	}

	private Transaction parseRecord(List<String> record) {
		Transaction transaction = new Transaction();

		transaction.setTransactionId(extractTransactionId(record));
		transaction.setAmount(extractAmount(record));
		transaction.setDate(extractDate(record));
		transaction.setCustomerId(extractCustomerId(record));
		transaction.setStatus(TransactionStatus.FAIL);
		return transaction;
	}

	private void validateRecord(List<String> record) {
		if (record.size() != 5)
			throw new ApplicationException("Invalid Record length " + record,
					ApplicationExceptionCode.INVALIDE_RECORD_SIZE);
	}

	private void validateTransaction(Transaction transaction) {

		Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
		if (violations.size() == 0) {
			transaction.setStatus(TransactionStatus.SUCCESS);
		} else {
			transaction.setStatus(TransactionStatus.FAIL);
			transaction.setError(violations.toString());
		}
	}

	private String extractTransactionId(List<String> record) {
		return record.get(0);
	}

	private Double extractAmount(List<String> record) {
		return Double.valueOf(record.get(1));
	}

	private String extractCurrency(List<String> record) {
		return record.get(2);
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

}
