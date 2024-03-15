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

public class Feeder extends SubsystemBase{

        CANSparkMax feedMotor;

        //RelativeEncoder feedEncoder;
        //RelativeEncoder shootEncoder;

    public Feeder(){
        feedMotor = new CANSparkMax(8, MotorType.kBrushed);

        feedMotor.setIdleMode(IdleMode.kBrake);

        feedMotor.setInverted(true);

        // feedEncoder = feedMotor.getEncoder();
        // shootEncoder = shootMotor.getEncoder();
    }

    @Override
    public void periodic(){

    }

    public void runFeedMotor(double speed){
        feedMotor.set(speed);
    }

    // public void getShootVelocity(){
    //     // shootEncoder.getVelocity();
    // }


    public Command RunFeedMotor(double speed){
        return this.startEnd(()->runFeedMotor(speed),()->runFeedMotor(0));
    }
    
}