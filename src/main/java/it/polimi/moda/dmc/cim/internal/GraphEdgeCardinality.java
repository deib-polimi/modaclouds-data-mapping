/**
 * Copyright 2014 deib-polimi
 * Contact: deib-polimi <santo.lombardo@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.polimi.moda.dmc.cim.internal;

import java.lang.management.GarbageCollectorMXBean;

public class GraphEdgeCardinality {
	
	public static int UNBOUNDED=-1;
	
	private GraphNode node;
	private int minCard=0;
	private int maxCard=UNBOUNDED;
	
	public GraphEdgeCardinality(GraphNode node,int minCard) {
		this.minCard=minCard;
		this.node=node;
	}
	
	public GraphEdgeCardinality(GraphNode node,int minCard, int maxCard) {
		this.minCard=minCard;
		this.maxCard=maxCard;
		this.node=node;
	}
	
	public GraphEdgeCardinality(GraphEdgeCardinality card) {
		this.minCard=card.minCard;
		this.maxCard=card.maxCard;
		this.node=new GraphNode(card.getNode());
	}
	
	public GraphNode getNode() {
		return node;
	}

	public void setNode(GraphNode node) {
		this.node = node;
	}

	public int getMinCard() {
		return minCard;
	}

	public int getMaxCard() {
		return maxCard;
	}
	
	public String toString() {
		String rit="nodeId="+this.node.getId()+" min="+this.minCard+" max="+this.maxCard;
		return rit;
	}
	
	

}
