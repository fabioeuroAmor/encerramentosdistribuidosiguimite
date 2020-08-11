package org.apache.ignite.encerramentosdistribuidosiguimite.iservices;

import org.apache.ignite.services.Service;

public interface TransmissaoDeinformacaoAssincrona extends Service{
	
	 /** Service name */
    public static final String SERVICE_NAME = "TransmissaoDeinformacaoAssincrona";
    
    public void enviaMensagemRemotaAsssincrona();

}
