package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LiftController {

	WPI_TalonSRX _lift = new WPI_TalonSRX(30);
	Joystick _joy1;
	int step = 1;
	// PID values
	double P = 0.0004, I = 0, D = 0;
	// CTREEnocder weird stuff
	CTREEncoder enc = new CTREEncoder(_lift);
	// PID controller
	PIDController liftPID = new PIDController(P, I, D, enc, _lift);
	
	// Height for placing the hatch as well as modes to reach those points
    double[] hatchHeight = new double[]{4096, 8192};
    enum hatchStage{
        STAGE1, STAGE2, STAGE3
	}
	hatchStage hatch;

	// Height for placing the ball as well as modes to reach those points
    double[] ballHeight = new double[]{5000, 7000};
    enum ballStage{
        CARGOSHIP, STAGE1, STAGE2, STAGE3
	}
	ballStage ball;

	// Modes to place the hatch and the ball
    enum Mode{
        MANUAL, AUTOHATCH, AUTOBALL
	}
	Mode mode;
	
	public LiftController(boolean setInverted, Joystick joy){
		invert(setInverted);
		_joy1 = joy;
		liftPID.reset();
		liftPID.setOutputRange(-0.5, 0.5);
		hatch = null;
		ball = null;
		mode = null;
	}

	public void run(){
		// Manual control
		if(_joy1.getRawButton(3) || _joy1.getRawButton(4)){
			mode = Mode.MANUAL;
		}else if(_joy1.getRawButton(7) || _joy1.getRawButton(8) || _joy1.getRawButton(9))
			mode = Mode.AUTOHATCH;
		else if(_joy1.getRawButton(10)
		 || _joy1.getRawButton(11) || _joy1.getRawButton(12) || _joy1.getRawButton(13))
		 	mode = Mode.AUTOBALL;

		 switch(mode){
			case MANUAL:
				manualControl();
				break;
			case AUTOHATCH:
				autoHatch();
				break;
			case AUTOBALL:
				autoBall();
				break;
		}
	}

	// Used to invert the talon direction
	public void invert(boolean inv) {
		_lift.setInverted(inv);
	}
	
	public double get() {
		double distance = _lift.getSelectedSensorPosition(0);
        return distance;
	}

	public void autoHatch(){
		if(_joy1.getRawButton(7)
	}
	
	public void autoBall(){

	}

    public void manualControl(){
		if(_joy1.getRawButton(3)){
			_lift.set(.5);
			liftPID.setSetpoint(get());
		}
		else if(_joy1.getRawButton(4)){
			_lift.set(-.3);
			liftPID.setSetpoint(get());
		}else
			liftPID.enable();
		
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

    public CTREEncoder(WPI_TalonSRX talon){

        this.talon = talon;
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