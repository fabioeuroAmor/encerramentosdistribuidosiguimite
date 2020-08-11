package org.apache.ignite.encerramentosdistribuidosiguimite.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.TransmissaoDeinformacaoSincrona;



public class TransmissaoDeinformacaoSincronaStartup {
	
	 /**
     * Start up a Maintenance Node.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
    	
    	Ignite ignite = Ignition.start("config/encerramentos-sincrona-cluster.xml");
    
        
        TransmissaoDeinformacaoSincrona transmissaoDeinformacaoSincrona = ignite.services().serviceProxy(TransmissaoDeinformacaoSincrona.SERVICE_NAME,TransmissaoDeinformacaoSincrona.class, false);
        transmissaoDeinformacaoSincrona.enviaMensagemRemota();
        //ignite.close();
    }

}
