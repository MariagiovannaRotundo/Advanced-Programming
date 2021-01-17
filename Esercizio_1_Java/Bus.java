package bus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.Random;

import javax.swing.Timer;

public class Bus implements ActionListener{

	private int capacity;
	private boolean doorOpen;
	private int numPassengers;
	Timer timer;
	
	//object for lock (for multithread)
	Object sincro = new Object();

	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private VetoableChangeSupport veto = new VetoableChangeSupport(this);
	
	public Bus(){
		//set default values
		this.capacity = 50;
		this.doorOpen = false;
		this.numPassengers = 20;
		//timer for close the door: 3 seconds
		timer = new Timer(3000 ,this);
	}

	//method to set the capacity
	public void setCapacity(int n){
		this.capacity = n;
	}
	
	//method to get the capacity
	public int getCapacity(){
		return this.capacity;
	}
	
	//method to change the state of the door
	public void setDoorOpen(boolean b){
		synchronized(sincro){
			boolean old = this.doorOpen;
			this.doorOpen = b;
			changes.firePropertyChange("doorOpen", old, b);
		}
	}
	
	//method to get the state of the door
	public boolean getDoorOpen(){
		return this.doorOpen;
	}

	//method to change the number of passengers
	public void setNumPassengers(int n){
		synchronized(sincro){
			if(n < this.numPassengers){
				//if decrease the number of passengers
				int old = this.numPassengers;
		        this.numPassengers = n;
				changes.firePropertyChange("numPassengers", old, n);
			}
			//if increase the number of passengers
			else if(n > this.numPassengers && n <= this.capacity){
				if(veto!=null){
					try{
						veto.fireVetoableChange("numPassengers", this.numPassengers, n);
					
						timer.stop();
						this.setDoorOpen(true);	
				        
						timer.setActionCommand("closedoor");
				        timer.setRepeats(false);
				        timer.start();
				        
				        int old = this.numPassengers;
				        this.numPassengers = n;
						changes.firePropertyChange("numPassengers", old, n);
				        
				        
					}
					catch(PropertyVetoException eveto){
						//if the change is blocked by the veto
						System.out.println("Veto!");
					} 
	
				}
			}
		}
	}
	
	
	//method to get the number of passengers
	public int getNumPassengers(){
		return this.numPassengers;
	}
	
	public void addVetoableChangeListener(VetoableChangeListener listener) {
    	veto.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
    	veto.removeVetoableChangeListener(listener);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener){
    	changes.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener){
    	changes.removePropertyChangeListener(listener);
    }
	
    //method to activate the timer to decrease the number of passengers
	public void activate(){
		Timer timer = new Timer(7000 ,this);
		timer.setActionCommand("activate");
        timer.setRepeats(true);
        timer.start();
	}
	
	
	public void actionPerformed(ActionEvent e){
		
		//event to close the door
		if("closedoor".equals(e.getActionCommand())){
			this.setDoorOpen(false);
		}
		
		//event to decrease the number of passengers
		if("activate".equals(e.getActionCommand())){
			Random rand = new Random();
			int n = rand.nextInt(this.numPassengers + 1);
			if(n!=0)
				this.setNumPassengers(this.numPassengers - n);
		}
	}

}
