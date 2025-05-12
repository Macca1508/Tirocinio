package human;

import java.util.Random;

public class SmartWatch {
	
	private Random rnd;
    
    private int systolicPressure; 	// massima 
    private int diastolicPressure; 	// minima 
    private int beatsMinute; 		// battiti al minuto  40-220


	public SmartWatch() {
		this.rnd = new Random(System.currentTimeMillis());
		this.beatsMinute        = 0;
		this.diastolicPressure  = 0;
		this.systolicPressure   = 0;
	}
	
	private void generateSwatchBeastMinute() {
		beatsMinute =  this.rnd.nextInt(40,221) ;
    }

	private void generateSwatchDiastolicPressure() {
		diastolicPressure =  this.rnd.nextInt(70,201) ;
	}
	
	private void generateSwatchSystolicPressure() {
		systolicPressure =  this.rnd.nextInt(40,140) ;
	}

	
	
	public int getBeatsMinute() {
		generateSwatchBeastMinute();
		return beatsMinute;
	}

	public int getDiastolicPressure() {
		generateSwatchDiastolicPressure();
		return diastolicPressure;
	}
	
	public int getSystolicPressure() {
		generateSwatchSystolicPressure();
		return systolicPressure;
	}

	@Override
	public String toString() {
		return "SmartWatch [Pressione sistolica =" + systolicPressure + ", Pressione diastolica =" + diastolicPressure + ", Battiti al minuto"+ beatsMinute +"]";
	}
}
