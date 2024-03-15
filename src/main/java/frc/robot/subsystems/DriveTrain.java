package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.SparkRelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.ADIS16470_IMU.CalibrationTime;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DriveTrain extends SubsystemBase {

        CANSparkMax mc1;
        CANSparkMax mc2;
        CANSparkMax mc3;
        CANSparkMax mc4;

        //ADIS16470_IMU gyro = new ADIS16470_IMU(IMUAxis.kX, IMUAxis.kY, IMUAxis.kZ, SPI.Port.kOnboardCS0, CalibrationTime._128ms);
        AHRS gyro1 = new AHRS(SerialPort.Port.kUSB);

        //RelativeEncoder e;

        //RelativeEncoder leftEncoder;
        //RelativeEncoder rightEncoder;

        DifferentialDrive d;

        //LimelightStuff
        private boolean limelightHasTarget;
        private double limelightSpeed;
        private double limelightTurn;

        private double tv;
        private double tx;
        private double ty;
        private double ta;
        //NetworkTableInstance.getDefault().getTable("limelight")

    public DriveTrain() {

        mc1 = new CANSparkMax(1, MotorType.kBrushed);
        mc2 = new CANSparkMax(2, MotorType.kBrushed);
        mc3 = new CANSparkMax(3, MotorType.kBrushed);
        mc4 = new CANSparkMax(4, MotorType.kBrushed);

        mc1.setInverted(true);
        mc2.setInverted(true);
        mc3.setInverted(false);
        mc4.setInverted(false);

        mc1.follow(mc2);
        mc3.follow(mc4);

        //e = mc4.getEncoder(SparkRelativeEncoder.Type.kQuadrature, 42);

        //where
        //mc1 --- mc2
        //
        //mc3 --- mc4

        mc1.setIdleMode(IdleMode.kCoast);
        mc2.setIdleMode(IdleMode.kCoast);
        mc3.setIdleMode(IdleMode.kCoast);
        mc4.setIdleMode(IdleMode.kCoast);

        //leftEncoder = mc1.getEncoder();
        //rightEncoder = mc2.getEncoder();

        d = new DifferentialDrive(mc2, mc4);
//TODO - enable some things for safety measures in differentialdrive
        d.setDeadband(.02);
        d.setExpiration(0);
        d.setMaxOutput(1);
        d.setSafetyEnabled(false);
    }

    @Override
    public void periodic() {

        if(SmartDashboard.getBoolean("Limelight Enabled", false) == true){
            UpdateLimelightTracking();
        } 

        SmartDashboard.putNumber("Current 1", mc1.getOutputCurrent());
        SmartDashboard.putNumber("Current 2", mc2.getOutputCurrent());
        SmartDashboard.putNumber("Current 3", mc3.getOutputCurrent());
        SmartDashboard.putNumber("Current 4", mc4.getOutputCurrent());

        SmartDashboard.putNumber("Gyro Yaw", gyro1.getYaw());

        //SmartDashboard.putNumber("Encoder", e.getPosition());

    }

    @Override
    public void simulationPeriodic() {

    }

    public void drive(double speed, double rotation){
        d.arcadeDrive(speed, rotation, true);
    }

    public AHRS getGyro(){
        return gyro1;
    }

    public Command ResetGyro(){
        return this.run(()->SmartDashboard.putNumber("Gyro Yaw", 0));
    }

    public Command LimelightTrigger(){
        return this.startEnd(()->SmartDashboard.putBoolean("Limelight Enabled", true),()->SmartDashboard.putBoolean("Limelight Enabled", false));
    }

    public void UpdateLimelightTracking(){
        //TODO These numbers must be tuned for your Robot!  Be careful!
        final double steerK = 0.03;                    // how hard to turn toward the target
        final double driveK = 0.26;                    // how hard to drive fwd toward the target
        final double desiredTargetArea = 13.0;        // Area of the target when the robot reaches the wall
//TODO - make sure limelight can see entire apriltag when touching wall
        final double maxSpeed = 0.7;                   // Simple speed limit so we don't drive too fast

        double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        double tid = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tis").getDouble(0);

        SmartDashboard.putNumber("Limelight ifTarget", tv);
        SmartDashboard.putNumber("Limelight X", tx);
        SmartDashboard.putNumber("Limelight Y", ty);
        SmartDashboard.putNumber("Limelight Area", ta);
        SmartDashboard.putNumber("Limelight Tag ID", tid);

        if (tv < 1.0)
        {
          limelightHasTarget = false;
          limelightSpeed = 0.0;
          limelightTurn = 0.0;
          return;
        }

        if(tid != 1 || tid != 2 || tid != 9 || tid != 10){
          limelightHasTarget = false;
          limelightSpeed = 0.0;
          limelightTurn = 0.0;
          return;
        }

        limelightHasTarget = true;

        // Start with proportional steering
        double steer_cmd = tx * steerK;
        limelightTurn = steer_cmd;

        // try to drive forward until the target area reaches our desired area
        double drive_cmd = (desiredTargetArea - ta) * driveK;

        // don't let the robot drive too fast into the goal
        if (drive_cmd > maxSpeed)
        {
          drive_cmd = maxSpeed;
        }

        double limelightDrive = drive_cmd;

//TODO - make sure this works
        drive(limelightDrive,limelightTurn);
    }

    // public void tankDrive(double left, double right){
    //     d.tankDrive(left, right);
    // }

}

