package human;

import it.wldt.core.engine.DigitalTwin;
import it.wldt.exception.EventBusException;
import it.wldt.exception.ModelException;
import it.wldt.exception.WldtDigitalTwinStateException;
import it.wldt.exception.WldtRuntimeException;
import it.wldt.exception.WldtWorkerException;

public class Human {

	private PersonalData Date;
	private DigitalTwin myDT;
	private MyShadowingFunction mySF;
	private MyPhysicalAdapter myPA;
	
	public Human(PersonalData p,String id_SF,String id_DT) throws ModelException, EventBusException, WldtRuntimeException, WldtWorkerException, WldtDigitalTwinStateException {
		this.Date = p;
		this.mySF = new MyShadowingFunction(id_SF);
		this.myDT = new DigitalTwin(id_DT,mySF);
		
	}
	
}
