/*
 * KAM Navigator Plugin
 *
 * URLs: http://openbel.org/
 * Copyright (C) 2012, Selventa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openbel.belframework.kam.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.openbel.belframework.kam.KAMNetwork;
import org.openbel.belframework.webservice.KAMService;
import org.openbel.belframework.webservice.KAMServiceFactory;

import com.selventa.belframework.ws.client.EdgeDirectionType;
import com.selventa.belframework.ws.client.KamEdge;
import com.selventa.belframework.ws.client.KamNode;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.task.Task;

/**
 * Package-protected {@link Task task} to add {@link KamEdge kam edges} from a
 * node expansion to a {@link CyNetwork cytoscape network}.
 * 
 * <p>
 * This {@link Task task} should be called by
 * {@link KAMTasks#expandNodes(KAMNetwork, Set, EdgeDirectionType)}.
 * </p>
 * 
 * @author James McMahon &lt;jmcmahon@selventa.com&gt;
 */
final class ExpandNodesTask extends AddEdgesTask {

    private final EdgeDirectionType direction;
    private final Set<CyNode> cynodes;
    private final KAMService kamService;

    ExpandNodesTask(KAMNetwork kamNetwork, Set<CyNode> cynodes,
            EdgeDirectionType direction) {
        super(kamNetwork, null);
        this.cynodes = cynodes;
        this.direction = direction;
        this.kamService = KAMServiceFactory.getInstance().getKAMService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<KamEdge> getEdgesToAdd() {
        final Collection<KamNode> kamNodes = kamNetwork.getKAMNodes(cynodes);
        List<KamEdge> edges = new ArrayList<KamEdge>();
        for (KamNode kamNode : kamNodes) {
            if (halt) {
                break;
            }

            edges.addAll(kamService.getAdjacentKamEdges(
                    kamNetwork.getDialectHandle(), kamNode, direction, null));
        }
        return edges;
    }

}
