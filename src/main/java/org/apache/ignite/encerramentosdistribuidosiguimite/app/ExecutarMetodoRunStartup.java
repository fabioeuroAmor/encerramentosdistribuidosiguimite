package org.apache.ignite.encerramentosdistribuidosiguimite.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.ExcucaoMetodoRun;

public class ExecutarMetodoRunStartup {
	
	public static void main(String[] args) throws IgniteException {
		
		Ignite ignite = Ignition.start("config/executar-run-cluster.xml");
		
		ExcucaoMetodoRun execucaoMetodoRun = ignite.services().serviceProxy(ExcucaoMetodoRun.SERVICE_NAME,ExcucaoMetodoRun.class, false);
		
		execucaoMetodoRun.executar();
		
		
	}

}
