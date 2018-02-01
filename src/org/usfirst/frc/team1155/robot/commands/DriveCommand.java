
package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.OI;
import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.PIDMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 */
public class DriveCommand extends Command {
	
	private Joystick movementStick;
    public DriveCommand(Joystick stick) {
        requires(Robot.drive);
        movementStick = stick;
        setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.pidMode = PIDMode.DriveStraight;
    	if(OI.driveStraightButton.get())
    		Robot.drive.startAdjustment(0, Robot.drive.gyro.getAngle());
    	else
    		Robot.drive.endAdjustment();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putNumber("GyroValue", Robot.drive.gyro.getAngle());
    	Robot.drive.setSpeed(-movementStick.getY(), -movementStick.getY());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.setSpeed(0, 0);
    	Robot.drive.endAdjustment();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
    
}
