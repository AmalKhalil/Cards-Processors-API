package com.madfooat.routes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.madfooat.services.BatchService;

@Component
public class MarchentRoute extends RouteBuilder {

	
	@Value("${merchant.directory.in}")
    private String inDirectory;
	
	@Value("${merchant.directory.out}")
    private String outDirectory;
	
	@Autowired
	private BatchService batchService;
	
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
					String merchant = file.getParentFile().getName();
					exchange.setProperty("Merchant", merchant);
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
					List<Map<String, Object>> output = batchService.processBatch(merchant, records).getOutputMap();
					exchange.getOut().setBody(output);
					
			}
		});
		
		//Write CSV Output
		defination.marshal().csv();
		
		defination.to("file:"+outDirectory);
		
		defination.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				String merchant = (String) exchange.getProperty("Merchant");
				batchService.createDirectoryIfNotExist(Paths.get(outDirectory + File.separator + merchant));
				Files.move(Paths.get(outDirectory + File.separator + exchange.getIn().getMessageId()), 
						Paths.get(outDirectory + File.separator + merchant+ File.separator + exchange.getIn().getMessageId()));
			}
		});
	}

}
