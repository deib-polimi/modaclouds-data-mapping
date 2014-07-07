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
package it.polimi.moda.run;

import it.polimi.moda.dmc.CIM2CPIM;
import it.polimi.moda.dmc.cim.ERManager;
import it.polimi.moda.dmc.cpim.fdm.FDMManager;
import it.polimi.moda.dmc.cpim.gdm.GDMManager;
import it.polimi.moda.dmc.cpim.hdm.HDMManager;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class Main {
	
	public static final String FDM="fdm";
	public static final String GDM="gdm";
	public static final String HDM="hdm";
	
	
	public static final String ROOT="root";
	
	public static final String SOURCE="source";
	public static final String TYPE="type";
	public static final String TARGET="target";
	public static final String MODEL_NAME="name";
	
	public static void main(String[] args) throws Exception {
		HashMap<String, String> params=new HashMap<String, String>();
		
		for (String string : args) 
		{
			String[] split=string.split("=");
			if(split.length==2) params.put(split[0].trim(), split[1].trim());
			else
			{
				System.out.println("Parameter format is wrong <name>=<value>");
				throw new Exception("Parameter format is wrong <name>=<value>");
			}
		}
		
		String sourcePath=params.get(SOURCE);
		String targetPath=params.get(TARGET);
		String type=params.get(TYPE);
		String root=params.get(ROOT);
		String name=params.get(MODEL_NAME);
		
		if(sourcePath==null) 
		{
			System.out.println("Source file is null");
			throw new Exception("Source file is null");
		}
		
		if(type!=null) 
		{
			if(!type.equalsIgnoreCase(GDM) &&
				!type.equalsIgnoreCase(FDM) &&
				!type.equalsIgnoreCase(HDM)) 
			{
				System.out.println("Type is not supported, uses <gdm>, <hdm> or <fdm>");
				throw new Exception("Type is not supported, uses <gdm>, <hdm> or <fdm>");
			}
			else
			{
				if(type.equalsIgnoreCase(HDM) && root==null) 
				{
					System.out.println("HDM model requires a root entity has been defined");
					throw new Exception("HDM model requires a root entity has been defined");
				}
			}
		}
		else
		{
			System.out.println("Type is null");
			throw new Exception("Type is null");
		}
		
		if(targetPath==null) 
		{
			System.out.println("Target file is null");
			throw new Exception("Target file is null");
		}
		
		
		ERManager erManager=new ERManager();
		erManager.loadXML(new FileReader(new File(sourcePath)));
		CIM2CPIM converter=new CIM2CPIM(erManager.getModel());
		
		if(type.equals(GDM)) 
		{
			GDMManager man=new GDMManager(converter.generateGDM(name));
			man.toFile(targetPath);
		}
		else
		{
			if(type.equals(FDM)) 
			{
				FDMManager man=new FDMManager(converter.generateFDM(name));
				man.toFile(targetPath);
			}
			else
			{
				if(type.equals(HDM)) 
				{
					HDMManager man=new HDMManager(converter.generateHDM(name,root));
					man.toFile(targetPath);
				}
			}
		}
	}
}
