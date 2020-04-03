package com.sgma.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
@Component
public class VehicleCrossCuttingLog {
	private static Logger logger = LogManager.getLogger();

	@Before("execution(* com.sgma.services.*.*(..))")
	public void logBeforeExecution(JoinPoint joinPoint) throws Throwable {
		StringBuilder parameters = new StringBuilder();
		parameters.append("Before ");
		if (joinPoint.getSignature()!=null) {
			parameters.append(joinPoint.getSignature()).append(" parameters: ");
		}
		
		if(joinPoint.getArgs()!=null) {
			Object[] signatureArgs = joinPoint.getArgs();
			for (Object signatureArg : signatureArgs) {
				parameters.append(signatureArg.toString()).append(" ");
			}
		}
		logger.info(parameters.toString());
	}

	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut = "execution(* com.sgma.services.*.*(..))", returning = "result")
	public void logAfterExecution(JoinPoint joinPoint, Object result) {
		if (joinPoint.getSignature() != null && result != null) {
			String resultStr = "";
			if(result instanceof CompletableFuture) {
				CompletableFuture<String> task = (CompletableFuture<String>) result;
				try {
					resultStr = task.get();
				} catch (InterruptedException | ExecutionException e) {
					
				}
			} else resultStr = result.toString();
			logger.info("After {} parameters: {}", joinPoint.getSignature(), resultStr);
		}
	}
	
	@AfterThrowing(pointcut = "execution(* com.sgma.services.*.*(..))", throwing = "ex")
    public void logError(JoinPoint joinPoint, Exception ex) {
		String methodName = "";
		if(joinPoint!=null && joinPoint.getSignature()!=null) {
			methodName = joinPoint.getSignature().toLongString();
		}
		
		logger.error("After {}", methodName, ex);
        
    }
}
