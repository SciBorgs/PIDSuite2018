package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.PIDMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveDistance extends Command {

    public DriveDistance() {
    	requires(Robot.drive);
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.pidMode = PIDMode.DriveDistance;
    	// TODO: Complete getEncPosition so that it returns actual distance.
    	Robot.drive.startAdjustment(Robot.drive.getEncPosition(), SmartDashboard.getNumber("DriveDistance", 0));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putNumber("EncoderValue", Robot.drive.getEncPosition());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Robot.drive.getPIDController().onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.endAdjustment();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
