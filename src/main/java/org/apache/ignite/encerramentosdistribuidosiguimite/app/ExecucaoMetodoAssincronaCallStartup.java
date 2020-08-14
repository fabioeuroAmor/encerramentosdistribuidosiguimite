package org.apache.ignite.encerramentosdistribuidosiguimite.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.ExecucaoMetodoAssincronaCall;


public class ExecucaoMetodoAssincronaCallStartup {
	
	public static void main(String[] args) throws IgniteException{
		Ignite ignite = Ignition.start("config/executar-assincrona-call-cluster.xml");
		
		ExecucaoMetodoAssincronaCall execucaoMetodoAssincronaCall = ignite.services().serviceProxy(ExecucaoMetodoAssincronaCall.SERVICE_NAME,ExecucaoMetodoAssincronaCall.class, false);
		execucaoMetodoAssincronaCall.execucaoAssincronaCall();
	}

}
