package com.sgma.services;

import java.util.concurrent.CompletableFuture;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class DBServiceA {
	
	@Async
	public CompletableFuture<String> getData(long id) throws Exception {
		
		if(id==0) {
			throw new Exception("An unexpected error has occurred");
		}
		/* assume that some data has been retrieved from DB by id */ 
		String resultData = "resultData";
		
		return CompletableFuture.completedFuture(resultData);
	}
}
