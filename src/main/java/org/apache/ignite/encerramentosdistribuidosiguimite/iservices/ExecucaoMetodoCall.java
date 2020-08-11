package org.apache.ignite.encerramentosdistribuidosiguimite.iservices;

import org.apache.ignite.services.Service;

public interface ExecucaoMetodoCall extends Service{
	
	 /** Service name */
    public static final String SERVICE_NAME = "ExecucaoMetodoCall";
    
    public void executar();

}
