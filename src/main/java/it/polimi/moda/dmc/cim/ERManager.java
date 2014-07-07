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
package it.polimi.moda.dmc.cim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.UnmarshalException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ERManager {
	private static Logger LOG=LoggerFactory.getLogger(ERManager.class);
	private Er model=null;
	
	public void loadXML(Reader in) {
		LOG.info("Log data");
		JAXBContext jaxbContext;
		try 
		{
			jaxbContext = JAXBContext.newInstance(Er.class);
			Unmarshaller jaxbU=jaxbContext.createUnmarshaller();
			this.model=(Er)jaxbU.unmarshal(in);
		} 
		catch (JAXBException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}
	
	public Er getModel() {
		return this.model;
	}
	
	
	
	public static void main(String[] args) {
		File f=new File("D:\\eclipseWS\\modacloudsSVN\\dmc\\xml\\erExample.xml");
		ERManager er=new ERManager();
		try 
		{
			er.loadXML(new FileReader(f));
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
