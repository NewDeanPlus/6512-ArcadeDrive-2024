package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase{

        CANSparkMax climbMotor;
        RelativeEncoder climbEncoder;
        double b;

    public Climber(){
        climbMotor = new CANSparkMax(5, MotorType.kBrushless);

        climbMotor.setIdleMode(IdleMode.kCoast);
        // climbMotor.setSoftLimit(SoftLimitDirection.kReverse, 160);
        //climbMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
        climbEncoder = climbMotor.getEncoder();
        climbEncoder.setPosition(0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("climbEncoder", climbEncoder.getPosition());
        // b = SmartDashboard.getNumber("climbEncoder", 0);
        // b = climbEncoder.getPosition();
        // SmartDashboard.putNumber("Yes", b);
        if(climbEncoder.getPosition() >= 230){ //205, 220
            //279 max limit, use 260
            climbStop();
            SmartDashboard.putBoolean("ifWinchEnabled", false);
        }
               

    }

    public void climb(double speed){
        climbMotor.set(speed);
    }

        public void climbStop(){
            climbMotor.set(0);
        }

    public Command MoveWinch(double speed){
        //b = climbEncoder.getPosition();
        // if(b <= 160){
             return this.startEnd(()->climb(speed),()->climbStop());
        //  }else{
        //     return this.run(()->climbStop());
        //  }
        // if(b >= 160) {
        //     return this.startEnd(()->climb(speed),()->climbStop());
        // }

        
    }
    
}
