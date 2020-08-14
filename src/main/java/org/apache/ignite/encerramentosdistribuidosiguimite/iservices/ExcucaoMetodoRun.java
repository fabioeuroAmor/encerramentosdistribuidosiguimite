package org.apache.ignite.encerramentosdistribuidosiguimite.iservices;

import org.apache.ignite.services.Service;

public interface ExcucaoMetodoRun extends Service{
	
	 /** Service name */
    public static final String SERVICE_NAME = "ExcucaoMetodoRun";
    
    public void executar();
    
    

}
