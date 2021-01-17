package covidController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class CovidController implements VetoableChangeListener{

	private int reducedCapacity;
	
	
	public CovidController(){
		//default value of the capacity
		this.reducedCapacity = 25;
	}

	//method to change capacity 
	public void setReducedCapacity(int n){
		this.reducedCapacity = n;
	}
	
	//method to read the capacity
	public int getReducedCapacity(){
		return reducedCapacity;
	}
	
	
	@Override
	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		//if the event happens (value change)
		if (evt.getPropertyName().equals("numPassengers")){	
			//read the new value
		 	int newValue = (int) evt.getNewValue();
		 	if( newValue > reducedCapacity){
		 		//change of the value not allowed
		 		throw new PropertyVetoException("Too many people!", evt);
		 	}			 
        }
	}
	
	
}
