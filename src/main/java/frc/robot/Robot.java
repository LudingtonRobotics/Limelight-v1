/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.  Jason                                                             */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");
  NetworkTableEntry thor = table.getEntry("thor");
  NetworkTableEntry tvert = table.getEntry("tvert");
  NetworkTableEntry ts1 = table.getEntry("ts1");
  NetworkTableEntry ts0 = table.getEntry("ts0");

  Joystick _joystick = new Joystick(0);

  WPI_TalonSRX _rghtFront = new WPI_TalonSRX(10); // Masters are single digits
  WPI_TalonSRX _rghtFollo = new WPI_TalonSRX(11); // Follos are the same id as the master but with a 0 added
  WPI_TalonSRX _leftFront = new WPI_TalonSRX(20);
  WPI_TalonSRX _leftFollo = new WPI_TalonSRX(21);
  
  DifferentialDrive _diffDrive = new DifferentialDrive(_leftFront, _rghtFront);
  double distance = 0;
  double x = 0;

  @Override
  public void robotInit() {
      _rghtFront.configFactoryDefault();
      _rghtFollo.configFactoryDefault();
      _leftFront.configFactoryDefault();
      _leftFollo.configFactoryDefault();
      _rghtFollo.follow(_rghtFront);
      _leftFollo.follow(_leftFront);
  }

  @Override
  public void robotPeriodic() {
  //read values periodically
  x = tx.getDouble(0.0);
  double y = ty.getDouble(0.0);
  double area = ta.getDouble(0.0);
  double width = thor.getDouble(0.0);
  double height = tvert.getDouble(0.0);
  double skew1 = ts1.getDouble(0.0);
  double skew0 = ts0.getDouble(0.0);
  distance = (272.695621739*5.75/height + 264*14/width)/2;
  
  


  
  //post to smart dashboard periodically
  SmartDashboard.putNumber("LimelightX", x);
  SmartDashboard.putNumber("LimelightY", y);
  SmartDashboard.putNumber("LimelightArea", area);
  SmartDashboard.putNumber("LimelightWidth", width);
  SmartDashboard.putNumber("LimelightHeight", height);
  SmartDashboard.putNumber("LimelightDistance",distance);
  SmartDashboard.putNumber("LimelightSkew1", skew1);
  SmartDashboard.putNumber("LimelightSkew0", skew0);
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
    _diffDrive.arcadeDrive((_joystick.getY()*-1)/2, _joystick.getZ()/1.5);
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    if (distance > 24){
      _diffDrive.arcadeDrive(_joystick.getY()/2, _joystick.getZ()/1.5);
    }
    if(_joystick.getRawButton(5)){
      table.getEntry("ledMode").setNumber(3); //LEDs on
      //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
      if (distance <12){
        //drop ball in cargo ship, or put on hatch panel, i'll figure this out later
      }else{
        _diffDrive.arcadeDrive(0.1*(distance-12), -x/27);
      }
    }else if(_joystick.getRawButton(6)){
      table.getEntry("ledMode").setNumber(1); //LEDs off
      //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    }else if(_joystick.getRawButton(4)){
      table.getEntry("ledMode").setNumber(2); //LEDs blind everybody that come in their path
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
