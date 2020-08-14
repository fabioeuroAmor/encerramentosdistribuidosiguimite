package org.apache.ignite.encerramentosdistribuidosiguimite.filters;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;

public class ExecutarRunFilter implements IgnitePredicate<ClusterNode>{

	/**
     * Checks if {@code node} needs to be considered as a Maintenance Service Node.
     *
     * @param node Cluster node instance.
     *
     * @return {@code true} if the node has to be considered as Maintenance Service Node, {@code false} otherwise.
     */
    public boolean apply(ClusterNode node) {
        Boolean dataNode = node.attribute("execucaoMetodoRun.service.node");

        return dataNode != null && dataNode;
    }

}
