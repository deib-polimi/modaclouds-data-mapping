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

import org.jgrapht.ext.VertexNameProvider;

import it.polimi.moda.dmc.cim.Entity;
import it.polimi.moda.dmc.cim.WithAttribute;
import it.polimi.moda.dmc.cim.WithAttribute.Attribute;

public class GraphNode implements VertexNameProvider<GraphNode> {
	
	private String name;
	private String id;
	
	private List<String> attributes=new ArrayList<String>();
	private List<String> keys=new ArrayList<String>();
	
		
	public GraphNode(String id,String name) {
		if(id!=null && name!=null) {
			this.id=id;
			this.name=name;
		}
	}
	
	public GraphNode(GraphNode node) {
		this.id=new String(node.getId());
		this.name=new String(node.getName());
		for (String attribute : node.attributes) {
			this.attributes.add(new String(attribute));
		}
		for (String attribute : node.attributes) {
			this.keys.add(new String(attribute));
		}
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getKeys() {
		return keys;
	}
	
	public void addAttribute(String name) {
		if(name!=null) this.attributes.add(name);
	}
	
	public void addKey(String name) {
		if(name!=null) this.keys.add(name);
	}

	public String getVertexName(GraphNode arg0) {
		String rit="";
		rit+="id="+this.id+" name="+this.name+" key="+this.keys +"attributes="+this.attributes;
		return rit;
	}
	
	
}
