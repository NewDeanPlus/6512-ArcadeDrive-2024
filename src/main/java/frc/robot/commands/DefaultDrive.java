package frc.robot.commands;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

import java.util.function.DoubleSupplier;

public class DefaultDrive extends Command {

    DriveTrain m_driveTrain;
    Joystick j;
    double speed;
    double rot;
    boolean ifTeleop;

    public DefaultDrive(DriveTrain subsystem) {
        m_driveTrain = subsystem;
        addRequirements(m_driveTrain);
        ifTeleop = true;
    }

        public DefaultDrive(DriveTrain subsystem, double speed, double turn) {
        m_driveTrain = subsystem;
        addRequirements(m_driveTrain);
        ifTeleop = false;
        this.speed = speed;
        this.rot = turn;
    }


    @Override
    public void initialize() {
    }

    @Override
    public void execute() {

        if(ifTeleop == true){
//Teleop Drive
        j = RobotContainer.getInstance().getJoystick();

        SmartDashboard.putNumber("Speed", j.getY());
        SmartDashboard.putNumber("Twist", j.getTwist());

        m_driveTrain.drive(
            j.getY(),
            -j.getTwist() * .8
        );

        }else{
//Auto Drive
            m_driveTrain.drive(
                speed,
                rot
            );
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
