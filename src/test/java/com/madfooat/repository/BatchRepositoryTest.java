package com.madfooat.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.madfooat.enums.TransactionStatus;
import com.madfooat.model.Batch;
import com.madfooat.model.Transaction;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BatchRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private BatchRepository batchRepository;
    
    @Test
    public void whenFindByMerchant_thenReturnBatch() {
        // given
        Batch batch = new Batch();
        batch.setCurrency("EGP");
        batch.setMerchant("M1");
        batch.setNubmerOfInvalidTransaction(1);
        batch.setNubmerOfValidTransaction(1);
        batch.setSumOfInvalidTransaction(2500.5);
        batch.setSumOfValidTransaction(30000);
        
        Transaction transaction= new Transaction();
        transaction.setAmount(2500);
        transaction.setBatch(batch);
        transaction.setCustomerId("1234567890");
        transaction.setDate(new Date());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionId("MARCHENT10ABCDEFGHIJ1234567890123451");
        batch.getTransactions().add(transaction);
        
        transaction= new Transaction();
        transaction.setAmount(2500);
        transaction.setBatch(batch);
        transaction.setCustomerId("1234567890");
        transaction.setDate(new Date());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionId("MARCHENT10ABCDEFGHIJ1234567890123451");
        batch.getTransactions().add(transaction);
        
        entityManager.persist(batch);
        entityManager.flush();
     
        // when
        List<Batch> found = batchRepository.findByMerchant("M1");
     
        // then
        assertThat(found.size())
          .isEqualTo(1);
        
        assertThat(found.get(0))
        .isEqualTo(batch);
    }
}
