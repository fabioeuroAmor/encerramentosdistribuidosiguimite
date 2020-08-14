package org.apache.ignite.encerramentosdistribuidosiguimite.iservices;

import org.apache.ignite.services.Service;

public interface ExecucaoMetodoAssincronaCall extends Service{
	
	 /** Service name */
    public static final String SERVICE_NAME = "ExecucaoMetodoAssincronaCall";
    
    public void execucaoAssincronaCall();

}
