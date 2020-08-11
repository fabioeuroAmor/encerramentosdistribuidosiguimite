package org.apache.ignite.encerramentosdistribuidosiguimite.services;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.encerramentosdistribuidosiguimite.domains.Maintenance;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.TransmissaoDeinformacaoSincrona;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.ServiceContext;




/*
 * Isso serve para distribuir informações nos nós do cluster
 * 
 */
public class TransmissaoDeinformacaoSincronaImpl implements TransmissaoDeinformacaoSincrona{
	
	 @IgniteInstanceResource
	 private Ignite ignite;
	
	
	     /** Maintenance IDs generator */
	    private IgniteAtomicSequence sequence;
	    
	    /** Processor that accepts requests from external apps that don't use Apache Ignite API. */
	    private ExternalCallsProcessor externalCallsProcessor;
	    
	    /** Reference to the cache. */
	    private IgniteCache<Integer, Maintenance> maintCache;
	


	@Override
	public void enviaMensagemRemota() {
//		 Ignite ignite =Ignition.start();
//		 ignite = Ignition.ignite(); 
		
		//final Ignite ignite = Ignition.ignite();
		 
		    //Limit broadcast to remote nodes only.
			//Limite a transmissão apenas para nós remotos.
			IgniteCompute compute = ignite.compute(ignite.cluster().forRemotes());
			
			// Print out hello message on remote nodes in the cluster group.
			//Imprima mensagem (Hello Node) em nós remotos no grupo de clusters. Lembra que broadcast é falar com todos os nós dos cluster
			compute.broadcast(() -> System.out.println("Hello Node sicrona: " + Ignition.localIgnite().cluster().localNode().id()));
		
	}

	@Override
	public void cancel(ServiceContext ctx) {
		 System.out.println("Stopping Maintenance Service on node:" + ignite.cluster().localNode());

	        // Stopping external requests processor.
	        externalCallsProcessor.interrupt();
		
	}

	@Override
	public void init(ServiceContext ctx) throws Exception {
	     System.out.println("Initializing Maintenance Service on node:" + ignite.cluster().localNode());

	        /**
	         * It's assumed that the cache has already been deployed. To do that, make sure to start Data Nodes with
	         * a respective cache configuration.
	         */
	        maintCache = ignite.cache("maintenance");

	        /** Processor that accepts requests from external apps that don't use Apache Ignite API. */
	        externalCallsProcessor = new ExternalCallsProcessor();

	        externalCallsProcessor.start();
		
	}

	@Override
	public void execute(ServiceContext ctx) throws Exception {
		  System.out.println("Executing Maintenance Service on node:" + ignite.cluster().localNode());

	        /**
	         * Getting the sequence that will be used for IDs generation.
	         */
	        sequence = ignite.atomicSequence("MaintenanceIds", 1, true);
		
	}
	
	   /**
     * Thread that accepts request from external applications that don't use Apache Ignite service grid API.
     */
    private class ExternalCallsProcessor extends Thread {
        /** Server socket to accept external connections. */
        private ServerSocket externalConnect;

        /** {@inheritDoc} */
        @Override public void run() {
            try {
                externalConnect = new ServerSocket(8090);

                while (!isInterrupted()) {
                    Socket socket = externalConnect.accept();

                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    // Getting vehicleId.
                    int vehicleId = dis.readInt();

                   // List<Maintenance> result = getMaintenanceRecords(vehicleId);

                    ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());

                    // Writing the result into the socket.
                   // dos.writeObject(result);

                    dis.close();
                    dos.close();
                    socket.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** {@inheritDoc} */
        @Override public void interrupt() {
            super.interrupt();

            try {
                externalConnect.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
