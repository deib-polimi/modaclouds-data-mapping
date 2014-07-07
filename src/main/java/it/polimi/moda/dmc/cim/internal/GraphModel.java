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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import it.polimi.moda.dmc.cim.ERManager;
import it.polimi.moda.dmc.cim.Entity;
import it.polimi.moda.dmc.cim.Er;
import it.polimi.moda.dmc.cim.Relationship;
import it.polimi.moda.dmc.cim.WithAttribute;
import it.polimi.moda.dmc.cim.WithAttribute.Attribute;
import it.polimi.moda.dmc.cpim.fdm.FDMManager;
import it.polimi.moda.dmc.cpim.fdm.Fdm;
import it.polimi.moda.dmc.cpim.gdm.GDMManager;
import it.polimi.moda.dmc.cpim.gdm.Gdm;
import it.polimi.moda.dmc.cpim.hdm.Child;
import it.polimi.moda.dmc.cpim.hdm.HDMManager;
import it.polimi.moda.dmc.cpim.hdm.Hdm;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.CrossComponentIterator;
import org.jgrapht.traverse.GraphIterator;
import org.xml.sax.SAXException;

public class GraphModel {
	private UndirectedGraph<GraphNode, GraphEdge> graph=new Multigraph<GraphNode, GraphEdge>(GraphEdge.class);
	
	public GraphModel(Er er) {
		this.parseERModel(er);
	}
	
	private void addNode(GraphNode node) {
		if(node!=null) this.graph.addVertex(node);
	}
	
	private void addEdge(GraphEdge edge) {
		if(edge!=null) {
			GraphNode n1=edge.getCardinalities().get(0).getNode();
			GraphNode n2=edge.getCardinalities().get(1).getNode();
			
			this.graph.addEdge(n1,n2,edge);
		}
	}
	
	
	private void parseERModel(Er er) {
		if(er!=null) {
			
			HashMap<String, GraphNode> vertexList=new HashMap<String, GraphNode>();
			
			
			//Add all entities
			for (Entity entity : er.getEntity()) {
				GraphNode node=new GraphNode(entity.getId(),entity.getName());
				this.addNode(node);
				
				if(entity.getAttribute()!=null) {
					for(WithAttribute.Attribute attribute : entity.getAttribute()) {
						node.addAttribute(attribute.getName());
					}
				}
				
				if(entity.getKey()!=null) {
					for(Object key:entity.getKey()) {
						node.addKey(((WithAttribute.Attribute)key).getName());
					}
				}
				
				this.graph.addVertex(node);
				vertexList.put(node.getId(), node);
			}
			
			//Add all edges
			for (Relationship rel : er.getRelationship()) {
				
				//TODO
				//Binary relationship assumption
				
				Entity e1=(Entity)rel.getLink().get(0).getId();
				int e1Min=(int)rel.getLink().get(0).getMinCard();
				int e1Max=GraphEdgeCardinality.UNBOUNDED;
				if(rel.getLink().get(0).getMaxCard()!=null) e1Max=rel.getLink().get(0).getMaxCard().intValue();
				
				Entity e2=(Entity)rel.getLink().get(1).getId();
				int e2Min=(int)rel.getLink().get(1).getMinCard();
				int e2Max=GraphEdgeCardinality.UNBOUNDED;
				if(rel.getLink().get(1).getMaxCard()!=null) e2Max=rel.getLink().get(1).getMaxCard().intValue();
				
				
				List<String> attributes=new ArrayList<String>();
				if(rel.getAttribute()!=null) {
					for (WithAttribute.Attribute  attribute: rel.getAttribute()) {
						attributes.add(attribute.getName());
					}
				}
				
				
				GraphEdge edge=new GraphEdge(rel.getId(),
						                     rel.getName(),
						                     attributes,
						                     vertexList.get(e1.getId()),
						                     e1Min,
						                     e1Max,
						                     vertexList.get(e2.getId()),
						                     e2Min,
						                     e2Max);
				
				this.addEdge(edge);
			}
		}
	}
	
	
	
	
	private GraphNode getNodeById(String id) {
		GraphNode rit=null;
		if(id!=null) {
			Set<GraphNode> nodes=this.graph.vertexSet();
			for (GraphNode node : nodes) {
				if(node.getId().equals(id)) {
					rit=node;
					break;
				}
			}
		}
		return rit;
	}
	
	private boolean existAttribute(String value,Element el) {
		boolean rit=false;
		if(el!=null) {
			List<Element> children=el.getChildren("attribute");
			
			for(int i=0;i<children.size();i++) {
				Element element=children.get(i);
				String v=element.getAttributeValue("name");
				if(v.equalsIgnoreCase(value)) {
					rit=true;
					break;
				}
			}
		}
		return rit;
	}
	
	
	private void _generateHDM(Element el, 
			             	 GraphNode node,
			             	 Set<String> visitedEdges) {
		
		if(el!=null && node!=null && visitedEdges !=null) {
			
			Set<GraphEdge> edges=this.graph.edgesOf(node);
			List<GraphNode> nodes=new Vector<GraphNode>();
			HashMap<String, Element> children=new HashMap<String, Element>();
			for (GraphEdge edge : edges) {
				
				GraphNode target=null;
				if(this.graph.getEdgeSource(edge).equals(node)) target=this.graph.getEdgeTarget(edge);
				else target=this.graph.getEdgeSource(edge);
				
				if(!visitedEdges.contains(edge.getId())) {
					nodes.add(target);
					//Do something
					System.out.println("Source:"+node.getId()+" Edge:"+edge.getId()+" Target:"+target.getId());
					
					//Create JDOM tree
					el.setAttribute("id",node.getId());
					for (String attribute : node.getAttributes()) {
						if(!this.existAttribute(attribute, el))
						{
							Element treeAttribute=new Element("attribute");
							treeAttribute.setAttribute("name",attribute);
							el.addContent(treeAttribute);
						}
						
					}
					
					Element edgeTree=new Element("relationship");
					edgeTree.setAttribute("id", edge.getId());
					for (String attribute : edge.getAttributes()) {
						if(!this.existAttribute(attribute, edgeTree))
						{
							Element treeAttribute=new Element("attribute");
							treeAttribute.setAttribute("name",attribute);
							edgeTree.addContent(treeAttribute);
						}
					}
					
					
					Element targetTree=new Element("child");
					targetTree.setAttribute("id",target.getId());
					for (String attribute : target.getAttributes()) {
						if(!this.existAttribute(attribute, targetTree)) {
							Element treeAttribute=new Element("attribute");
							treeAttribute.setAttribute("name",attribute);
							targetTree.addContent(treeAttribute);
						}	
					}
					
					el.addContent(edgeTree);
					edgeTree.addContent(targetTree);
					//
					
					children.put(target.getId(),targetTree);

				}
				visitedEdges.add(edge.getId());
			}
			for (GraphNode n : nodes) {
				this._generateHDM(children.get(n.getId()),n, visitedEdges);
			}
		}
	}
	
	public Hdm generateHDM(String modelName,String rootNode) {
		Hdm rit=null;
		
		if(modelName!=null && rootNode!=null) {
			Element hdm=new Element("hdm");
			hdm.setAttribute("id",modelName);
			
			Element root=new Element("child");
			root.setAttribute("id",this.getNodeById(rootNode).getId());
			if(root!=null) {
				this._generateHDM(root,this.getNodeById(rootNode),new HashSet<String>());
				hdm.addContent(root);
				
				HDMManager manager=new HDMManager();
				XMLOutputter outputter=new XMLOutputter(Format.getPrettyFormat());
				String out=outputter.outputString(hdm);
				manager.loadXML(new StringReader(out));
				rit=manager.getModel();
			}
		}
		return rit;
	}
	
	public Gdm generateGDM(String modelName) {
		Gdm rit=null;
		
		if(modelName!=null && this.graph!=null) {
			Element root=new Element("gdm");
			root.setAttribute("name",modelName);
			
			for (GraphNode node : this.graph.vertexSet()) {
				Element el=new Element("node");
				el.setAttribute("id",node.getId());
				el.setAttribute("name",node.getName());
				
				for (String attribute : node.getAttributes()) {
					Element elAttribute=new Element("attribute");
					elAttribute.setAttribute("name",attribute);
					el.addContent(elAttribute);
				}
				
				StringBuilder keys=new StringBuilder();
				for (String key : node.getKeys()) {
					keys.append(key+" ");
				}
				
				el.setAttribute("key",keys.toString().trim());
				
				root.addContent(el);
			}
			
			for (GraphEdge edge : this.graph.edgeSet()) {
				Element el=new Element("edge");
				el.setAttribute("id",edge.getId());
				el.setAttribute("name",edge.getName());
				el.setAttribute("node1",edge.getCardinalities().get(0).getNode().getId());
				el.setAttribute("node1MinCard",""+edge.getCardinalities().get(0).getMinCard());
				if(edge.getCardinalities().get(0).getMaxCard()!=GraphEdgeCardinality.UNBOUNDED) 
					el.setAttribute("node1MaxCard",""+edge.getCardinalities().get(0).getMaxCard());
				
				el.setAttribute("node2",edge.getCardinalities().get(1).getNode().getId());
				el.setAttribute("node2MinCard",""+edge.getCardinalities().get(1).getMinCard());
				if(edge.getCardinalities().get(1).getMaxCard()!=GraphEdgeCardinality.UNBOUNDED) 
					el.setAttribute("node2MaxCard",""+edge.getCardinalities().get(1).getMaxCard());
				root.addContent(el);
			}
			
			GDMManager manager=new GDMManager();
			XMLOutputter outputter=new XMLOutputter(Format.getPrettyFormat());
			String out=outputter.outputString(root);
			manager.loadXML(new StringReader(out));
			rit=manager.getModel();
		}
		return rit;
	}
	
	public Fdm generateFDM(String modelName) {
		Fdm rit=null;
		
		if(modelName!=null && this.graph!=null) {
			Element root=new Element("fdm");
			root.setAttribute("name",modelName);
			
			for (GraphNode node : this.graph.vertexSet()) {
				Element el=new Element("table");
				el.setAttribute("id",node.getId());
				el.setAttribute("name",node.getName());
				
				StringBuilder keys=new StringBuilder();
				for (String key : node.getKeys()) {
					keys.append(key+" ");
				}
				
				el.setAttribute("key",keys.toString().trim());
				
				for (String attribute : node.getAttributes()) {
					Element elAttribute=new Element("column");
					elAttribute.setAttribute("id",attribute);
					el.addContent(elAttribute);
				}

				root.addContent(el);
			}
			
			FDMManager manager=new FDMManager();
			XMLOutputter outputter=new XMLOutputter(Format.getPrettyFormat());
			String out=outputter.outputString(root);
			manager.loadXML(new StringReader(out));
			rit=manager.getModel();
		}
		return rit;
	}
	
	public String toString() {
		return this.toGraphMX(this.graph);
	}
	
	public String toGraphMX(Graph g) {
		StringWriter stOut=new StringWriter();
		
		GraphMLExporter<GraphNode, GraphEdge> exporter=new GraphMLExporter<GraphNode, GraphEdge>();
		try 
		{
			exporter.export(stOut, this.graph);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return stOut.toString();
	}
	
	public static void main(String[] args) {
		ERManager erManager=new ERManager();
		try 
		{
			erManager.loadXML(new FileReader(new File("D:\\eclipseWS\\modacloudsSVN\\dmc\\xml\\erExample.xml")));
			GraphModel model=new GraphModel(erManager.getModel());
			System.out.println(model.toString());
			
//			Hdm hdm=model.generateHDM("test","B");
//			HDMManager manager=new HDMManager(hdm);
//			
//			System.out.println(manager.toString());
			
			
//			Gdm gdm=model.generateGDM("test");
//			GDMManager manager=new GDMManager(gdm);
//			
//			System.out.println(manager.toString());
			
			Fdm fdm=model.generateFDM("test");
			FDMManager manager=new FDMManager(fdm);
			
			System.out.println(manager.toString());
			
			
//			model.recursive(model.getNodeById("B"));
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
		
}
