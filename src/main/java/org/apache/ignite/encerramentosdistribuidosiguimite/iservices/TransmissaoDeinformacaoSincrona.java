package org.apache.ignite.encerramentosdistribuidosiguimite.iservices;

import org.apache.ignite.services.Service;

public interface TransmissaoDeinformacaoSincrona  extends Service{
	
	 /** Service name */
    public static final String SERVICE_NAME = "TransmissaoDeinformacaoSincrona";
    
    public void enviaMensagemRemota();

}
