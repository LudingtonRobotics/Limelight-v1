package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain{

    WPI_TalonSRX _rghtFront = new WPI_TalonSRX(10); // Masters are single digits
    WPI_TalonSRX _rghtFollower = new WPI_TalonSRX(11); // Followers are the same id as the master but with a 0 added
    WPI_TalonSRX _leftFront = new WPI_TalonSRX(20);
    WPI_TalonSRX _leftFollower = new WPI_TalonSRX(21);
  
    DifferentialDrive _diffDrive = new DifferentialDrive(_leftFront, _rghtFront);
    double exactEncoderValue;

    public DriveTrain(){
        _rghtFront.configFactoryDefault();
        _rghtFollower.configFactoryDefault();
        _leftFront.configFactoryDefault();
        _leftFollower.configFactoryDefault();
        _rghtFollower.follow(_rghtFront);
        _leftFollower.follow(_leftFront);
    }

    public void run(double x, double z){
        _diffDrive.arcadeDrive(-x/2, z/2);
    }
    
    public void reset() {
		_rghtFront.getSensorCollection().setPulseWidthPosition(0, 100);
		_rghtFront.getSensorCollection().setQuadraturePosition(0, 100);
	}

    public double get(){
        exactEncoderValue = _rghtFront.getSelectedSensorPosition(0);
        double driveDistance = ((exactEncoderValue/4096) * (8*Math.PI));
        return driveDistance;
    }
}