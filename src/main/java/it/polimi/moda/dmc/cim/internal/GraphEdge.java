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

import java.util.ArrayList;
import java.util.List;

import it.polimi.moda.dmc.cim.Relationship.Link;

import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.graph.DefaultEdge;

public class GraphEdge extends DefaultEdge implements EdgeNameProvider<GraphEdge>{
	
	private String name;
	private String id;
	private List<String> attributes=new ArrayList<String>();
	
	private GraphEdgeCardinality card1;
	private GraphEdgeCardinality card2;
	
	public GraphEdge(String id,String name,List<String> attributes,
			         GraphNode n1,int minCard1,int maxCard1,
			         GraphNode n2,int minCard2,int maxCard2) {
		if(id!=null && 
		   name!=null && 
		   n1!=null && 
		   n2!=null) {
			this.id=id;
			this.name=name;
			if(attributes!=null) this.attributes=attributes;
			
			this.card1=new GraphEdgeCardinality(n1,minCard1,maxCard1);
			this.card2=new GraphEdgeCardinality(n2,minCard2, maxCard2);
		}
	}
	
	public GraphEdge(GraphEdge edge) {
		this.id=new String(edge.getId());
		this.name=new String(edge.getName());
		for (String attribute : edge.attributes) {
			this.attributes.add(new String(attribute));
		}
		this.card1=new GraphEdgeCardinality(edge.card1);
		this.card2=new GraphEdgeCardinality(edge.card2);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<GraphEdgeCardinality> getCardinalities() {
		List<GraphEdgeCardinality> rit=new ArrayList<GraphEdgeCardinality>();
		rit.add(this.card1);
		rit.add(this.card2);
		return rit;
	}
	
	public GraphEdgeCardinality getCardinality(GraphNode node) {
		GraphEdgeCardinality rit=null;
		
		if(this.card1.getNode().equals(node)) rit=this.card1;
		else
			if(this.card2.getNode().equals(node)) rit=this.card2;
		return rit;
	}
	
	public GraphEdgeCardinality getCardinality(String nodeId) {
		GraphEdgeCardinality rit=null;
		
		if(this.card1.getNode().getId().equalsIgnoreCase(nodeId)) rit=this.card1;
		else 
			if(this.card2.getNode().getId().equalsIgnoreCase(nodeId)) rit=this.card2;
		return rit;
	}

	public String getEdgeName(GraphEdge arg0) {
		String rit="";
		
		rit+="id="+this.id+" name="+this.name+this.card1+" "+this.card2;
		return rit;
	}
	
	
	
	
	
	
	
	


}
