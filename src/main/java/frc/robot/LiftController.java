package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class LiftController {

	WPI_TalonSRX _lift;
	boolean inv;
    double multiplier;
    
    double[] hatchHeight = new double[]{4096, 8192};
    enum hatchStage{
        STAGE1, STAGE2, STAGE3
    }

    double[] ballHeight = new double[]{5000, 7000};
    enum ballStage{
        STAGE1, STAGE2, STAGE3
    }

    enum mode{
        HATCH, BALL
    }
	
	public void invert(boolean in) {
		inv = in;
	}
	
	public double get() {
		double distance = _lift.getSelectedSensorPosition(0) * multiplier;
		distance = inv ? -distance : distance;
        return distance;
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