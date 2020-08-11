package org.apache.ignite.encerramentosdistribuidosiguimite.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.ExecucaoMetodoCall;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.TransmissaoDeinformacaoSincrona;


public class ExecutarMetodoCallStartup {
	
	 /**
     * Start up a Maintenance Node.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
    	
    	Ignite ignite = Ignition.start("config/executar-call-cluster.xml");

        ExecucaoMetodoCall execucaoMetodoCall = ignite.services().serviceProxy(ExecucaoMetodoCall.SERVICE_NAME,ExecucaoMetodoCall.class, false);
        execucaoMetodoCall.executar();
        //ignite.close();

    }

}
