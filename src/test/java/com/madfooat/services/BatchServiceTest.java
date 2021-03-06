package com.madfooat.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.madfooat.enums.Role;
import com.madfooat.model.Batch;
import com.madfooat.model.User;
import com.madfooat.model.builder.BatchBuilder;
import com.madfooat.repository.BatchRepository;
import com.madfooat.repository.UserRepository;
import com.madfooat.web.dto.BatchDTO;

@RunWith(SpringRunner.class)
public class BatchServiceTest {

	@TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
  
        @Bean
        public BatchService batchService() {
            return new BatchService();
        }
    }
 
    @Autowired
    private BatchService batchService;
 
    @MockBean
    private BatchRepository batchRepository;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
	private BatchBuilder batchBuilder;
    
    @MockBean
	private SimpMessagingTemplate template;
    
    private List<List<String>> records;
    
    @Before
    public void setUp() {
    	
    	Batch batch1 = new Batch();
    	batch1.setMerchant("M1");
    	
    	Batch batch2 = new Batch();
    	batch2.setMerchant("M1");
    	
    	User user = new User();
    	user.setUserName("M1");
    	user.setRole(Role.Merchant);
    	
    	
    	List<Batch> resultAll = new ArrayList();
    	resultAll.add(batch1);
    	resultAll.add(batch2);
    	
    	List<Batch> resultM1 = new ArrayList();
    	resultM1.add(batch1);
     
        Mockito.when(batchRepository.findByMerchant("M1"))
          .thenReturn(resultM1);
        
        Mockito.when(batchRepository.findAll())
        .thenReturn(resultAll);
        
        Mockito.when(userRepository.findByUserName("M1"))
        .thenReturn(user);
        
        Mockito.when(userRepository.findByUserNameAndRole("M1",Role.Merchant))
        .thenReturn(user);
        
        Batch batch = new Batch();
    	batch1.setMerchant("M1");
    	batch1.setCurrency("EGP");
    	
    	records = buildRecords();
        Mockito.when(batchBuilder.build("M1", records))
        .thenReturn(batch);
    }
    
    @Test
    public void whenMerchant_thenBatchsShouldBeFound() {
        User user = new User();
        user.setUserName("M1");
        user.setRole(Role.Merchant);
        
        List<BatchDTO> found = batchService.getBatches(user);
      
         assertThat(found.size())
          .isEqualTo(1);
         
         assertThat(found.get(0).getMerchant())
         .isEqualTo("M1");
     }
    
    @Test
    public void whenAdmin_thenBatchsShouldBeFound() {
        User user = new User();
        user.setUserName("Admin");
        user.setRole(Role.SuperUser);
        
        List<BatchDTO> found = batchService.getBatches(user);
      
         assertThat(found.size())
          .isEqualTo(2);
     }
    
    @Test
    public void whenProcessBatch_Success() {
        Batch found = batchService.processBatch("M1", records);
      
        assertThat(found)
        .isNotNull();
        
       
        
     }

	private List<List<String>> buildRecords() {
		List<List<String>> records = new ArrayList<List<String>>();
        
        List<String> record = new ArrayList<String>();
        record.add("MARCHENT10ABCDEFGHIJ1234567890123451");
        record.add("1000");
        record.add("EGP");
        record.add("2018.05.30-09:10:10");
        record.add("1234567890");
        
        records.add(record);
        
        record = new ArrayList<String>();
        record.add("MARCHENT10ABCDEFGHIJ1234567890123452");
        record.add("3000");
        record.add("EGP");
        record.add("2018.05.30-09:10:10");
        record.add("1234567890");
        
        records.add(record);
        
        record = new ArrayList<String>();
        record.add("MARCHENT10ABCDEFGHIJ1234567890123453");
        record.add("6000");
        record.add("EGP");
        record.add("2018.05.30-09:10:10");
        record.add("1234567890");
        
        records.add(record);
		return records;
	}
}
