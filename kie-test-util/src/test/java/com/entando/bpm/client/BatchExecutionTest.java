package com.entando.bpm.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.drools.core.ClassObjectSerializationFilter;
import org.drools.core.command.impl.ExecutableCommand;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.AgendaGroupSetFocusCommand;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.command.runtime.rule.GetObjectsCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.junit.Test;
import org.kie.server.api.marshalling.Marshaller;
import org.kie.server.api.marshalling.MarshallerFactory;
import org.kie.server.api.marshalling.MarshallingFormat;

import com.fsi.creditcarddisputecase.AdditionalInformation;
import com.fsi.creditcarddisputecase.Cardholder;

public class BatchExecutionTest {
	private final static transient Logger logger = Logger.getLogger(BatchExecutionTest.class);

	@Test
	public void generateBatchCommand() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		Marshaller marshaller = MarshallerFactory.getMarshaller(classes, MarshallingFormat.JSON,
				BatchExecutionTest.class.getClassLoader());

		Cardholder cardholder = new Cardholder();
		cardholder.setAge(35);
		cardholder.setBalanceRatio(0.2f);
		cardholder.setIncidentCount(2);
		cardholder.setStateCode("VA");
		cardholder.setStatus("SILVER");

		InsertObjectCommand insertCmd = new InsertObjectCommand(cardholder, "cardholder");
		insertCmd.setReturnObject(false);

		List<ExecutableCommand<?>> cmds = new ArrayList<ExecutableCommand<?>>();
		cmds.add(insertCmd);
		
		cmds.add(new AgendaGroupSetFocusCommand("additional-info"));
		cmds.add(new FireAllRulesCommand("additional-info-fired"));
		//cmds.add(new AgendaGroupSetFocusCommand("MAIN"));
		//cmds.add(new QueryCommand("questions", "get-additional-info"));
		/*GetObjectsCommand getObjectsCommand = new GetObjectsCommand();
		getObjectsCommand.setOutIdentifier("additional-questions");
		getObjectsCommand.setFilter(new ClassObjectFilter(AdditionalInformation.class));
		cmds.add(getObjectsCommand);*/
		cmds.add(new GetObjectsCommand(new ClassObjectSerializationFilter(AdditionalInformation.class), "questions"));
		cmds.add(new AgendaGroupSetFocusCommand("cleanup"));
		cmds.add(new FireAllRulesCommand("cleanup-fired"));
		
		BatchExecutionCommandImpl batch = new BatchExecutionCommandImpl(cmds);
		
		String marshalled = marshaller.marshall(batch);
		logger.info(">>> " + marshalled);
	}
}
