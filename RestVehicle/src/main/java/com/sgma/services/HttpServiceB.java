package com.sgma.services;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class HttpServiceB {
	
	@Async
	public CompletableFuture<String> sendMessage(String message) {
		/* assume that some message sends via rest client and gets "httpResponse" */ 
		String httpResponse = "httpResponse";
		return CompletableFuture.completedFuture(httpResponse);
	}
}
