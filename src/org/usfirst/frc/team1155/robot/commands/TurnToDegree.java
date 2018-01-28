package org.usfirst.frc.team1155.robot.commands;

import org.usfirst.frc.team1155.robot.Robot;
import org.usfirst.frc.team1155.robot.subsystems.DriveSubsystem.PIDMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnToDegree extends Command{

	public TurnToDegree() {
		requires(Robot.drive);
		setInterruptible(true);
		
	}
	@Override
	protected void initialize() {
		// Calibrates the turn angle
    	Robot.drive.pidMode = PIDMode.TurnDegree;
		Robot.drive.startAdjustment(Robot.drive.gyro.getAngle(), SmartDashboard.getNumber("TurnAngle", 0));
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("GyroValue", Robot.drive.gyro.getAngle());
	}

	@Override
	protected boolean isFinished() {
		return Robot.drive.getPIDController().onTarget();
	}

	@Override
	protected void end() {
		Robot.drive.endAdjustment();
		
	}

	@Override
	protected void interrupted() {
		end();
	}

}
