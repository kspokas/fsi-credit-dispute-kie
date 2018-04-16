package com.entando.bpm.client;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

public class ExecuteDMNTest {
	private final static transient Logger logger = Logger.getLogger(ExecuteDMNTest.class);
	
	@Test
	public void testRunDMC() {
		KieServices kieServices = KieServices.Factory.get();

		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		DMNRuntime dmnRuntime = kieContainer.newKieSession("default").getKieRuntime( DMNRuntime.class );
		
		DMNModel dmnModel = 
				dmnRuntime.getModel("http://www.entando.com/dmn/definitions/_c540edb3-4b27-4577-8863-1cf0267762b3", 
						"credit-dispute-simple-input");
		
		
		DMNContext dmnContext = dmnRuntime.newContext();
		dmnContext.set("Age", 25);
		dmnContext.set("Incident Count", 3);
		dmnContext.set("Cardholder Status", "GOLD");
		dmnContext.set("Fraud Amount", 500);
	
		DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
		for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {
			logger.info("Credit Routing Decision '" + dr.getDecisionName() + "' : " + dr.getResult());
		}
	}
}
