package human;

import java.util.Random;

public class Sensor {
private Random rnd;
    
    private double glycaemia; 	//  
    private double triglycerides; 	// 
    private double creatinine; 		
    private double sodium; //


	public Sensor() {
		this.rnd = new Random(System.currentTimeMillis());
		this.glycaemia     = 0;
		this.triglycerides = 0;
		this.creatinine    = 0;
		this.sodium        = 0;
	}
	
	private void generateSensorGlycaemia() {
		this.glycaemia =  this.rnd.nextDouble(40,221) ;
    }

	private void generateSensorTriglycerides() {
		this.triglycerides =  this.rnd.nextDouble(70,201) ;
	}
	
	private void generateSensorCreatinine() {
		this.creatinine =  this.rnd.nextDouble(40,140) ;
	}
	
	private void generateSensorSodium() {
		this.sodium =  this.rnd.nextDouble(40,140) ;
	}

	public double getGlycaemia() {
		this.generateSensorGlycaemia();
		return glycaemia;
	}

	public double getTriglycerides() {
		this.generateSensorTriglycerides();
		return triglycerides;
	}

	public double getCreatinine() {
		this.generateSensorCreatinine();
		return creatinine;
	}

	public double getSodium() {
		this.generateSensorSodium();
		return sodium;
	}

}
