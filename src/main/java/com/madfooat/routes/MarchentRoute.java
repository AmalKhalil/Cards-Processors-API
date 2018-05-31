package com.madfooat.routes;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madfooat.exception.ApplicationException;
import com.madfooat.services.TransactionService;

@Component
public class MarchentRoute extends RouteBuilder {

	
	@Value("${merchant.directory.in}")
    private String inDirectory;
	
	@Value("${merchant.directory.out}")
    private String outDirectory;
	
	@Autowired
	private TransactionService transactionService;
	
	@Override
	public void configure() throws Exception {
		RouteDefinition defination = from("file:"+inDirectory+"?recursive=true&moveFailed=.fialed");

		defination.onException(ApplicationException.class).handled(false).maximumRedeliveries(1);
		
		// Get Merchant
		defination.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
					Message message = exchange.getIn();
					File file = message.getBody(File.class);
					exchange.setProperty("Merchant", file.getParentFile().getName());
			}
		});
		//Parse CSV Input
		defination.unmarshal().csv();
		
		//Process Transactions
		defination.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
					Message message = exchange.getIn();
					
					List<List<String>> records = message.getBody(List.class);
					String merchant = (String) exchange.getProperty("Merchant");
					List<Map<String, Object>> output = transactionService.processBatch(merchant, records);
					exchange.getOut().setBody(output);
					
			}
		});
		
		//Write CSV Output
		defination.marshal().csv();
		defination.to("file:"+outDirectory);
	}

}
