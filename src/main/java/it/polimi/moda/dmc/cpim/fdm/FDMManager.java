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
package it.polimi.moda.dmc.cpim.fdm;

import it.polimi.moda.dmc.cim.ERManager;
import it.polimi.moda.dmc.cpim.fdm.ObjectFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FDMManager {
	private static Logger LOG=LoggerFactory.getLogger(ERManager.class);
	private Fdm model=null;
	
	public FDMManager() {
	}
	
	public FDMManager(Fdm fdm) {
		if(fdm!=null) this.model=fdm;
	}
	
	public void loadXML(Reader in) {
		LOG.info("Log data");
		JAXBContext jaxbContext;
		try 
		{
			jaxbContext = JAXBContext.newInstance(Fdm.class);
			Unmarshaller jaxbU=jaxbContext.createUnmarshaller();
			this.model=(Fdm)jaxbU.unmarshal(in);
		} 
		catch (JAXBException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}
	
	public Fdm getModel() {
		return this.model;
	}
	
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
		File f=new File("D:\\eclipseWS\\modacloudsSVN\\dmc\\xml\\fdmExample.xml");
		FDMManager fdm=new FDMManager();
		try 
		{
			fdm.loadXML(new FileReader(f));
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
