/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
 
package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  NetworkTableEntry tv = table.getEntry("tv");
  NetworkTableEntry camTran = table.getEntry("camtran");


  Joystick _joystick = new Joystick(0);
  


  double angledDistanceFromCamera = 0;
  double x = 0;
  double isThereATarget = 0;
  double driveSpeed = 0;
  static double maxDriveSpeed = 0.5;
  static double minDriveSpeed = 0.3;
  double driveTurn = 0;
  static double minTurnSpeed = 0.5;
  double[] limelightSolvePNPStuff = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  double driveDistance;
  double cameraAngle;
  double distance;

  //LiftController _lift = new LiftController(false, _joystick);
  
  ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  DriveTrain drive = new DriveTrain();

  double turnAngle;

  double distanceToDrive = 0;

  enum driveModes{
    MANUAL, AUTO
  }

  enum autoModes{
    GETVALUES, DRIVE, WAITFORINPUT
  }

  @Override
  public void robotInit() {
    gyro.calibrate();
  }

  @Override
  public void robotPeriodic() {

    //System.out.println(autoModes);
    //System.out.println(drive.get());
    //read values periodically
    x = tx.getDouble(0.0); //angle from crosshair (-27° to 27°) in the x direction
    double y = ty.getDouble(0.0); //angle from crosshair (-27° to 27°) in the y direction
    double area = ta.getDouble(0.0);
    double width = thor.getDouble(0.0);
    double height = tvert.getDouble(0.0);
    double skew1 = ts1.getDouble(0.0);
    double skew0 = ts0.getDouble(0.0);
    isThereATarget = tv.getDouble(0.0);
    if (height > 0 && width > 0){
      angledDistanceFromCamera = (741.913*5.75/height + 806.929*14/width)/2;
      cameraAngle = Math.acos(26/angledDistanceFromCamera);
      //distance = Math.pow((angledDistanceFromCamera*angledDistanceFromCamera - 23*23),1/2);
      distance = angledDistanceFromCamera*Math.sin(cameraAngle);

    }else if(height > 0){
      angledDistanceFromCamera = (741.913*5.75/height);
      cameraAngle = Math.acos(26/*distance in inches from camera to the level of tape*//angledDistanceFromCamera);
      //distance = Math.pow((angledDistanceFromCamera*angledDistanceFromCamera - 23*23),1/2);
      distance = angledDistanceFromCamera*Math.sin(cameraAngle);
    }else if(width > 0){
      angledDistanceFromCamera = (806.929*14/width);
      cameraAngle = Math.acos(26/angledDistanceFromCamera);
      //distance = Math.pow((angledDistanceFromCamera*angledDistanceFromCamera - 23*23),1/2);
      distance = angledDistanceFromCamera*Math.sin(cameraAngle);
    }else{
      angledDistanceFromCamera = 0.0;
      cameraAngle = 0.0;
      distance = 0.0;
    }
    limelightSolvePNPStuff = camTran.getDoubleArray(limelightSolvePNPStuff);
    
    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
    SmartDashboard.putNumber("LimelightWidth", width);
    SmartDashboard.putNumber("LimelightHeight", height);
    SmartDashboard.putNumber("AngledLimelightDistance",angledDistanceFromCamera);
    SmartDashboard.putNumber("ActualLimelightDistance", distance);
    SmartDashboard.putNumber("LimelightSkew1", skew1);
    SmartDashboard.putNumber("LimelightSkew0", skew0);
    //SmartDashboard.putNumberArray("DOES THIS WORK", limelightSolvePNPStuff);
    SmartDashboard.putNumber("x distance", limelightSolvePNPStuff[0]);
    //SmartDashboard.putNumber("DOES THIS WORK1", limelightSolvePNPStuff[1]);
    /*
    SmartDashboard.putNumber("y distanc", limelightSolvePNPStuff[2]);
    SmartDashboard.putNumber("DOES THIS WORK3", limelightSolvePNPStuff[3]);
    SmartDashboard.putNumber("DOES THIS WORK4", limelightSolvePNPStuff[4]);
    SmartDashboard.putNumber("DOES THIS WORK5", limelightSolvePNPStuff[5]);*/
    
    SmartDashboard.putNumber("Gyro", gyro.getAngle());
    
    //System.out.println(test);

  }

  @Override
  public void disabledInit() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
  }


  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  driveModes driveModes;
  autoModes autoModes;

  @Override
  public void teleopInit() {
    drive.reset();
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
    driveModes = driveModes.MANUAL;
    autoModes = autoModes.GETVALUES;
  }

  @Override
  public void teleopPeriodic() {
    switch(driveModes){
      case MANUAL:
        drive.run(_joystick.getY(), _joystick.getZ());
        
        break;
      case AUTO:
        switch(autoModes){
          case GETVALUES:

            break;
          case DRIVE:

            break;
          case WAITFORINPUT:
            
            break;
        }
        break;
    }
    
  }
    

}

