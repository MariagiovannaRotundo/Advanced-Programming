package busBoard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.*;

import covidController.CovidController;
import bus.Bus;


@SuppressWarnings("serial")
public class BusBoard extends JFrame implements ActionListener, PropertyChangeListener, Serializable{

	private JSpinner newPeople;
	private JButton prenota;
	private JLabel passengers;
	private JLabel doorOpen;
	
	private Timer timer;
	private Color defaultColor;
	private Bus bus;
	private CovidController controller;
	
	
	//creation of the interface when the object BusBoard is created
	public BusBoard(Bus b, CovidController cont){
		
		Container c=getContentPane();
		JPanel p=new JPanel();
		p.setLayout(new GridLayout(4,1,5,20));
		
		bus = b;
		controller = cont;
		
		bus.addVetoableChangeListener(controller);
		bus.addPropertyChangeListener(this);
		
		passengers = new JLabel("Passengers: " + bus.getNumPassengers());
		passengers.setFont(new Font("sansserif",Font.PLAIN,25));
		passengers.setHorizontalAlignment(SwingConstants.CENTER);
		
		doorOpen = new JLabel("Door Open: "+ bus.getDoorOpen());
		doorOpen.setFont(new Font("sansserif",Font.PLAIN, 25));
		doorOpen.setHorizontalAlignment(SwingConstants.CENTER);
		
		//default value,lower bound,upper bound,increment by
		SpinnerModel sm = new SpinnerNumberModel(1, 1, 5, 1); 
	    newPeople = new JSpinner(sm);
		newPeople.setFont(new Font("sansserif",Font.PLAIN,30));
		
		prenota = new JButton("Prenota");
		prenota.setFont(new Font("sansserif",Font.PLAIN,30));
		defaultColor = prenota.getBackground();
		
		p.add(passengers);
		p.add(doorOpen);
		p.add(newPeople);
		p.add(prenota);
		
		c.add(p);
		
		setSize(300,400);
		setLocation(550, 5);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		//setting listener for the button to book
		prenota.setActionCommand("prenota");
		prenota.addActionListener(this);
		
		bus.activate();
	}
	
	
	public void actionPerformed(ActionEvent e){
		//handle the event associate to the button
		if("prenota".equals(e.getActionCommand())){
			newPeople.setEnabled(false);
			prenota.setEnabled(false);
			//change the color of the button
			prenota.setBackground(Color.yellow);
			
			//the action listener is "this"
			//keep the button of another color for 2 seconds
			timer = new Timer(2000 ,this);
	        timer.setRepeats(false);
			timer.setActionCommand("timeout");
			timer.start();
		}
		
		//after the 2 seconds
		if("timeout".equals(e.getActionCommand())){
			//increase the number of passengers
			bus.setNumPassengers(bus.getNumPassengers() + (int)newPeople.getValue());
			
			//set the number of people for the booking to the original(default) value
			newPeople.setValue(((SpinnerNumberModel) newPeople.getModel()).getMinimum());
			//change the color of the button to the original
			prenota.setBackground(defaultColor);
			newPeople.setEnabled(true);
			prenota.setEnabled(true);
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//number of passenger is changed: update the label
		if (evt.getPropertyName().equals("numPassengers")){	
			passengers.setText("Passengers: " + evt.getNewValue());
        }
		
		//state of doors is changed: update the label
		if (evt.getPropertyName().equals("doorOpen")){	
			doorOpen.setText("Door Open: "+ evt.getNewValue());
        }
		
	}
	
	
}
