package frc.robot.commands;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

import java.util.function.DoubleSupplier;

import com.kauailabs.navx.frc.AHRS;

public class Rotate extends SequentialCommandGroup {

    DriveTrain m_driveTrain;
    AHRS gyro;
    double degrees;

    public Rotate(DriveTrain subsystem, double degrees, AHRS gyro) {
        m_driveTrain = subsystem;
        addRequirements(m_driveTrain);
        this.gyro = gyro;
        this.degrees = degrees;

        addCommands(
            m_driveTrain.ResetGyro().raceWith(new WaitCommand(.01)),
            new DefaultDrive(m_driveTrain, 0, degrees * .7)
        );
    }


    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
