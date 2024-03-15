package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Claw extends SubsystemBase{

       CANSparkMax clawMotor;

    public Claw(){

        clawMotor = new CANSparkMax(7, MotorType.kBrushed);

        clawMotor.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public void periodic(){

    }

    public void moveClaw(double speed){
        clawMotor.set(speed);
    }

    public Command MoveClaw(double speed){
        return this.startEnd(()->moveClaw(speed),()->moveClaw(0));
    }
    
}