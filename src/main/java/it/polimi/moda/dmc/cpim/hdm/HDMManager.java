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
package it.polimi.moda.dmc.cpim.hdm;

import it.polimi.moda.dmc.cim.ERManager;
import it.polimi.moda.dmc.cim.Er;
import it.polimi.moda.dmc.cpim.hdm.ObjectFactory;
import it.polimi.moda.dmc.cpim.hdm.Relationship.Attribute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDMManager {
	
	private static Logger LOG=LoggerFactory.getLogger(ERManager.class);
	private Hdm model=null;
	
	public HDMManager() {
		ObjectFactory of=new ObjectFactory();
		this.model=of.createHdm();
	}
	
	public HDMManager(Hdm hdm) {
		if(hdm!=null) this.model=hdm;
	}
	
	public void loadXML(Reader in) {
		LOG.info("Log data");
		JAXBContext jaxbContext;
		try 
		{
			jaxbContext = JAXBContext.newInstance(Hdm.class);
			Unmarshaller jaxbU=jaxbContext.createUnmarshaller();
			this.model=(Hdm)jaxbU.unmarshal(in);
		} 
		catch (JAXBException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}
	
	public Hdm getModel() {
		return this.model;
	}
	
//	public void createHdm(String id) {
//		ObjectFactory of=new ObjectFactory();
//		this.model=of.createHdm();
//		this.model.id=id;
//	}
//	
//	private Child _getChildById(Child parent,String id) {
//		Child rit=null;
//		if(parent!=null) {
//			if(parent.getId().equals(id)) rit=parent;
//			else 
//			{
//				List<Relationship> relationships=parent.getRelationship();
//				if(relationships!=null) {
//					for (Relationship rel : relationships) {
//						for (Child child : rel.getChild()) {
//							rit=this._getChildById(child, id);
//							if(rit!=null) break;
//						}
//					}
//				}
//			}
//		}
//		return rit;
//	}
//	
//	public Child geChildById(String id) {
//		Child rit=null;
//		if(id!=null) {
//			rit=this._getChildById(this.model.getChild(), id);
//		}
//		return rit;
//	}
//	
//	private Relationship _getRelationshipdById(Relationship parent,String id) {
//		Relationship rit=null;
//		if(parent!=null) {
//			if(parent.getId().equals(id)) rit=parent;
//			else 
//			{
//				List<Child> children=parent.getChild();
//				if(children!=null) {
//					for (Child child : children) {
//						for (Relationship rel : child.getRelationship()) {
//							rit=this._getRelationshipdById(rel, id);
//						if(rit!=null) break;
//						}
//					}
//				}
//			}
//		}
//		return rit;
//	}
//	
//	private Relationship getRelationshipdById(String id) {
//		Relationship rit=null;
//		if(id!=null) {
//			if(this.model.getChild()!=null) {
//				List<Relationship> relationship=this.model.getChild().getRelationship();
//				if(relationship!=null) {
//					for (Relationship rel : relationship) {
//						rit=this._getRelationshipdById(rel, id);
//						if(rit!=null) break;
//					}
//				}
//			}
//		}
//		return rit;
//	}
//	
//	public void addRelationship(String childId,String id,List<String> attributes) {
//		if(childId!=null && id!=null && attributes!=null) {
//			Child child=this.geChildById(childId);
//			if(child!=null) {
//				Relationship relationship=new Relationship();
//				relationship.setId(id);
//				
//				List<Relationship.Attribute> atts=new ArrayList<Relationship.Attribute>();
//				for (String attribute : attributes) {
//					Relationship.Attribute att=new Relationship.Attribute();
//					att.setName(attribute);
//					atts.add(att);
//					
//					relationship.attribute=atts;
//				}
//				
//				child.getRelationship().add(relationship);
//			}
//		}
//	}
//	
//	public void addChild(String relationshipId,String id,List<String> attributes) {
//		if(relationshipId!=null && id!=null && attributes!=null) {
//			Relationship relationship=this.getRelationshipdById(relationshipId);
//			if(relationship!=null) {
//				Child child=new Child();
//				child.setId(id);
//				
//				List<Child.Attribute> atts=new ArrayList<Child.Attribute>();
//				for (String attribute : attributes) {
//					Child.Attribute att=new Child.Attribute();
//					att.setName(attribute);
//					atts.add(att);
//					
//					child.attribute=atts;
//				}
//				
//				relationship.getChild().add(child);
//			}
//			
//		}
//	}
//	
//	public void addRoot(String id,List<String> attributes) {
//		if(id!=null && attributes!=null) {
//			Child child=new Child();
//			child.setId(id);
//			
//			List<Child.Attribute> atts=new ArrayList<Child.Attribute>();
//			for (String attribute : attributes) {
//				Child.Attribute att=new Child.Attribute();
//				att.setName(attribute);
//				atts.add(att);
//				
//				child.attribute=atts;
//			}
//			
//			this.model.setChild(child);	
//		}
//	}
	
	public String toString() {

		StringWriter strWriter=new StringWriter();
		JAXBContext jc;
		try 
		{
			jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
			Marshaller m=jc.createMarshaller();
			m.marshal(this.model, strWriter);
			strWriter.close();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strWriter.toString();
	}
	
	public void toFile(String path) throws Exception {

		FileWriter fout=new FileWriter(new File(path));
		JAXBContext jc;
		jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
		Marshaller m=jc.createMarshaller();
		m.marshal(this.model, fout);
		fout.close();
	}
	
	
	public static void main(String[] args) {
		File f=new File("D:\\eclipseWS\\modacloudsSVN\\dmc\\xml\\hdmExample.xml");
		HDMManager hdm=new HDMManager();
		try 
		{
			hdm.loadXML(new FileReader(f));
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
