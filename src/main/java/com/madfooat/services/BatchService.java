package com.madfooat.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.madfooat.enums.Role;
import com.madfooat.enums.TransactionStatus;
import com.madfooat.exception.ApplicationException;
import com.madfooat.exception.ApplicationExceptionCode;
import com.madfooat.model.Batch;
import com.madfooat.model.Transaction;
import com.madfooat.model.User;
import com.madfooat.model.builder.BatchBuilder;
import com.madfooat.model.mappers.BatchMapper;
import com.madfooat.repository.BatchRepository;
import com.madfooat.repository.UserRepository;
import com.madfooat.web.dto.BatchDTO;

@Service
public class BatchService {

	@Value("${merchant.directory.in}")
	private String inDirectory;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private BatchBuilder batchBuilder;
	
	@Autowired
	private SimpMessagingTemplate template;

	private Validator validator;

	public BatchService() {
		super();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	public List<BatchDTO> getBatches(User loggedInUser){
		List<Batch> result = new ArrayList<Batch>();
		if(Role.Merchant.equals(loggedInUser.getRole()))
			result = batchRepository.findByMerchant(loggedInUser.getUserName());
		else if(Role.SuperUser.equals(loggedInUser.getRole()))
			 batchRepository.findAll().forEach(result::add);
				
		return BatchMapper.INSTANCE.batchToBatchDto(result);
	}
	public void uploadBatchFile(String merchant, String fileName, InputStream inputStream) {

		isMerchantExist(merchant);
		createBatchFile(merchant, fileName, inputStream);

	}

	public Batch processBatch(String merchant, List<List<String>> records) {

		isMerchantExist(merchant);
		Batch batch = batchBuilder.build(merchant, records);
		batch.getTransactions().forEach(transaction -> accumulateTransaction(batch, transaction));
		batchRepository.save(batch);
		this.notifyMerchantBatchIsReady(merchant);
		return batch;

	}
	 
	private void accumulateTransaction(Batch batch, Transaction transaction) {
		validateTransaction(transaction);
		if (TransactionStatus.SUCCESS.equals(transaction.getStatus())) {
			batch.increaseNubmerOfValidTransaction();
			batch.addValidAmount(transaction.getAmount());
		} else {
			batch.increaseNubmerOfInvalidTransaction();
			batch.addInvalidAmount(transaction.getAmount());
		}
	}

	private void isMerchantExist(String merchant) {
		User user = userRepository.findByUserNameAndRole(merchant, Role.Merchant);

		if (user == null)
			throw new ApplicationException("Merchant " + merchant + " Not found in System",
					ApplicationExceptionCode.UNKNOW_MERCHANT);
	}

	private void createBatchFile(String merchant, String fileName, InputStream inputStream) {
		java.nio.file.Path path = Paths.get(inDirectory + "/" + merchant + "/" + fileName);
		try {
			Files.copy(inputStream, path);
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage(), ApplicationExceptionCode.SYSTEM_ERROR);
		}
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
	
	private void notifyMerchantBatchIsReady(String merchant) {
		template.convertAndSendToUser(merchant, "/reply", "Ready");
	}

}
