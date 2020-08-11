package org.apache.ignite.encerramentosdistribuidosiguimite.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.TransmissaoDeinformacaoAssincrona;

public class TransmissaoDeinformacaoAssincronaStartup {
	
	 /**
     * Start up a Maintenance Node.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
    	
    	Ignite ignite = Ignition.start("config/encerramentos-assincrona-cluster.xml");
    	
    	 TransmissaoDeinformacaoAssincrona transmissaoDeinformacaoAssincrona = ignite.services().serviceProxy(TransmissaoDeinformacaoAssincrona.SERVICE_NAME,TransmissaoDeinformacaoAssincrona.class, false);
    	 transmissaoDeinformacaoAssincrona.enviaMensagemRemotaAsssincrona();
    	 //ignite.close();
    }

}
