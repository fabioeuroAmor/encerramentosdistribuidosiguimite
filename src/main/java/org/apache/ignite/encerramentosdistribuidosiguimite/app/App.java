package org.apache.ignite.encerramentosdistribuidosiguimite.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        Ignite ignite = Ignition.start("config/client-node-config.xml");
    }
}
