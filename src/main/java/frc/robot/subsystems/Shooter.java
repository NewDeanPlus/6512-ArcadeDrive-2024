package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Shooter extends SubsystemBase{

        CANSparkMax shootMotor;

        //RelativeEncoder feedEncoder;
        //RelativeEncoder shootEncoder;

    public Shooter(){
        shootMotor = new CANSparkMax(6, MotorType.kBrushed);

        shootMotor.setIdleMode(IdleMode.kCoast);

        shootMotor.setInverted(true);

        // feedEncoder = feedMotor.getEncoder();
        // shootEncoder = shootMotor.getEncoder();
    }

    @Override
    public void periodic(){

    }


    public void runShootMotor(double speed){
        shootMotor.set(speed);
    }

    // public void getShootVelocity(){
    //     // shootEncoder.getVelocity();
    // }

    public Command PrepareShooter(double speed){
        return this.startEnd(()->runShootMotor(speed),()->runShootMotor(0));
    }

    
}