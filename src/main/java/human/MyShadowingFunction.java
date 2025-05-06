package human;

import java.util.Map;

import it.wldt.adapter.digital.event.DigitalActionWldtEvent;
import it.wldt.adapter.physical.PhysicalAssetDescription;
import it.wldt.adapter.physical.event.PhysicalAssetEventWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetRelationshipInstanceCreatedWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetRelationshipInstanceDeletedWldtEvent;
import it.wldt.core.model.ShadowingFunction;

public class MyShadowingFunction extends ShadowingFunction{

	public MyShadowingFunction(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDigitalTwinBound(Map<String, PhysicalAssetDescription> adaptersPhysicalAssetDescriptionMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDigitalTwinUnBound(Map<String, PhysicalAssetDescription> adaptersPhysicalAssetDescriptionMap,
			String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPhysicalAdapterBidingUpdate(String adapterId,
			PhysicalAssetDescription adapterPhysicalAssetDescription) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPhysicalAssetPropertyVariation(PhysicalAssetPropertyWldtEvent<?> physicalPropertyEventMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPhysicalAssetEventNotification(PhysicalAssetEventWldtEvent<?> physicalAssetEventWldtEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPhysicalAssetRelationshipEstablished(
			PhysicalAssetRelationshipInstanceCreatedWldtEvent<?> physicalAssetRelationshipWldtEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPhysicalAssetRelationshipDeleted(
			PhysicalAssetRelationshipInstanceDeletedWldtEvent<?> physicalAssetRelationshipWldtEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDigitalActionEvent(DigitalActionWldtEvent<?> digitalActionWldtEvent) {
		// TODO Auto-generated method stub
		
	}

}
