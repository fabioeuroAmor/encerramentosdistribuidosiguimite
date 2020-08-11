package org.apache.ignite.encerramentosdistribuidosiguimite.services;

import org.apache.ignite.*;
import org.apache.ignite.encerramentosdistribuidosiguimite.domains.Maintenance;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.ExecucaoMetodoCall;
import org.apache.ignite.encerramentosdistribuidosiguimite.iservices.TransmissaoDeinformacaoSincrona;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.ServiceContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;


/*
 * Isso serve para distribuir informações nos nós do cluster
 * 
 */
public class ExecucaoCallImpl implements ExecucaoMetodoCall {
	
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
	public void executar() {

		Collection<IgniteCallable<Integer>> calls = new ArrayList<>();

		// Iterate through all words in the sentence and create callable jobs.
		for (String word : "How many characters".split(" "))
			calls.add(word::length);
		//
		// Execute collection of callables on the cluster.
		Collection<Integer> res = ignite.compute().call(calls);

		// Add all the word lengths received from cluster nodes.
		int total = res.stream().mapToInt(Integer::intValue).sum();

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
                externalConnect = new ServerSocket(8092);

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
