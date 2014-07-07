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
package it.polimi.moda.dmc;

import it.polimi.moda.dmc.cim.Er;
import it.polimi.moda.dmc.cim.internal.GraphModel;
import it.polimi.moda.dmc.cpim.fdm.FDMManager;
import it.polimi.moda.dmc.cpim.fdm.Fdm;
import it.polimi.moda.dmc.cpim.gdm.Gdm;
import it.polimi.moda.dmc.cpim.hdm.Hdm;

public class CIM2CPIM {
	
	private GraphModel model=null;
	
	public CIM2CPIM(Er erModel) {
		this.model=new GraphModel(erModel);
	}
	
	public Hdm generateHDM(String modelName,String rootNode) {
		Hdm rit=null;
		if(this.model!=null && modelName!=null && rootNode!=null) {
			rit=this.model.generateHDM(modelName, rootNode);
		}
		return rit;
	}
	
	public Gdm generateGDM(String modelName) {
		Gdm rit=null;
		if(this.model!=null && modelName!=null) {
			rit=this.model.generateGDM(modelName);
		}
		return rit;
	}
	
	public Fdm generateFDM(String modelName) {
		Fdm rit=null;
		if(this.model!=null && modelName!=null) {
			rit=this.model.generateFDM(modelName);
		}
		return rit;
	}
	
}
