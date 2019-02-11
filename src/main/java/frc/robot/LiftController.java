package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LiftController {

	WPI_TalonSRX _lift = new WPI_TalonSRX(30);
	Joystick _joy1;
	double multiplier;
	
	// Height for placing the hatch as well as modes to reach those points
    double[] hatchHeight = new double[]{4096, 8192};
    enum hatchStage{
        STAGE1, STAGE2, STAGE3
    }

	// Height for placing the ball as well as modes to reach those points
    double[] ballHeight = new double[]{5000, 7000};
    enum ballStage{
        CARGOSHIP, STAGE1, STAGE2, STAGE3
    }

	// Modes to place the hatch and the ball
    enum Mode{
        HATCH, BALL
	}
	
	public LiftController(boolean setInverted, Joystick joy){
		invert(setInverted);
		_joy1 = joy;
	}

	public void run(){

	}

	// Used to invert the talon direction
	public void invert(boolean inv) {
		_lift.setInverted(inv);
	}
	
	public double get() {
		double distance = _lift.getSelectedSensorPosition(0) * multiplier;
        return distance;
	}
	
	public void autoControl(Mode mode){
		switch(mode){
			case HATCH:
				break;
			case BALL:
		}
	}
    
    public void manualControl(double speed){
		_lift.set(speed);
	}

	
	public void reset() {
		_lift.getSensorCollection().setPulseWidthPosition(0, 100);
		_lift.getSensorCollection().setQuadraturePosition(0, 100);
		_lift.getSensorCollection().setPulseWidthPosition(0, 100);
		_lift.getSensorCollection().setQuadraturePosition(0, 100);
		_lift.getSensorCollection().setPulseWidthPosition(0, 100);
		_lift.getSensorCollection().setQuadraturePosition(0, 100);
		_lift.getSensorCollection().setPulseWidthPosition(0, 100);
		_lift.getSensorCollection().setQuadraturePosition(0, 100);
	}
	
}

class CTREEncoder implements PIDSource {

    WPI_TalonSRX talon;

    public CTREEncoder(int id){

        talon = new WPI_TalonSRX(id);
    }
       
    public int get() {
        return talon.getSelectedSensorPosition();
    }
    @Override

    public double pidGet() {
        return get();
    }

    @Override

    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;

    }

    @Override

    public void setPIDSourceType (PIDSourceType pidSource){
        
    }
}