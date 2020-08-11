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
import org.apache.ignite.compute.ComputeTaskFuture;
import org.apache.ignite.encerramentosdistribuidosiguimite.domains.Maintenance;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.TransmissaoDeinformacaoAssincrona;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.ServiceContext;

public class TransmissaoDeinformacaoAssincronaImpl implements TransmissaoDeinformacaoAssincrona{
	
	 @IgniteInstanceResource
	 private Ignite ignite;
	 
	 /** Maintenance IDs generator */
	 private IgniteAtomicSequence sequence;
	
	
	 /** Processor that accepts requests from external apps that don't use Apache Ignite API. */
    private ExternalCallsProcessor externalCallsProcessor; 
  
    
    /** Reference to the cache. */
    private IgniteCache<Integer, Maintenance> maintCache;

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

	@Override
	public void enviaMensagemRemotaAsssincrona() {
		
		final Ignite ignite = Ignition.ignite();

		// Limit broadcast to remote nodes only and 
		// enable asynchronous mode.
		IgniteCompute compute = ignite.compute(ignite.cluster().forRemotes()).withAsync();

		// Print out hello message on remote nodes in the cluster group.
		compute.broadcast(() -> System.out.println("Hello Node assincrona: " + Ignition.localIgnite().cluster().localNode().id()));

		ComputeTaskFuture<?> fut = compute.future();

		fut.listen(f -> System.out.println("Finished sending broadcast job."));
		
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
             externalConnect = new ServerSocket(8091);

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
