package org.usfirst.frc.team1155.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 *
 */
public class DriveSubsystem extends PIDSubsystem {

	public TalonSRX frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;
	public Gyro gyro;

	public static enum PIDMode {
		TurnDegree, DriveStraight, DriveDistance;
	}

	public PIDMode pidMode;

	// Initialize your subsystem here
	public DriveSubsystem() {
		super("Drive", 0.1, 0, 0.1);
		pidMode = PIDMode.TurnDegree;

		frontLeftMotor = new TalonSRX(3);
		frontRightMotor = new TalonSRX(1);
		backLeftMotor = new TalonSRX(4);
		backRightMotor = new TalonSRX(2);
		backRightMotor.set(ControlMode.Follower, frontRightMotor.getDeviceID());
		backLeftMotor.set(ControlMode.Follower, frontLeftMotor.getDeviceID());

		getPIDController().setContinuous(false);

		gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
		gyro.reset();
	}

	protected void initDefaultCommand() {

	}

	protected double returnPIDInput() {
		switch (pidMode) {
		case TurnDegree:
		case DriveStraight:
			return gyro.getAngle();
		case DriveDistance:
			return getEncPosition();
		default:
			return 0;
		}
	}

	protected void usePIDOutput(double output) {
		switch (pidMode) {
		// For reference, a CW gyro correction is positive by default
		// TODO: Check if the robot goes in the right direction.
		// If it doesn't, flip the negations on the outputs.
		case TurnDegree:
			output *= 0.5;
			setSpeed(output, -output);
			break;
		case DriveStraight:
			output *= 0.1;
			correctSpeed(output);
			break;
		case DriveDistance:
			output *= -0.5;
			setSpeed(output, output);
		default:
			setSpeed(0, 0);
			break;
		}
	}

	public void setSpeed(double leftSpeed, double rightSpeed) {
		frontLeftMotor.set(ControlMode.PercentOutput, leftSpeed);
		frontRightMotor.set(ControlMode.PercentOutput, rightSpeed);
	}

	public void correctSpeed(double offset) {
		double rightOutput = frontRightMotor.getMotorOutputPercent();
		double leftOutput = frontLeftMotor.getMotorOutputPercent();
		frontLeftMotor.set(ControlMode.PercentOutput, leftOutput + ((rightOutput >= 0) ? offset : -offset));

	}

	public void startAdjustment(double current, double setPoint) {
		switch (pidMode) {
		case TurnDegree:
			getPIDController().setPercentTolerance(5.0);
			setPoint %= 360;
			setSetpoint((int) (((current - setPoint >= 0 ? 180 : -180) + current - setPoint) / 360) * 360 + setPoint);
			break;
		case DriveStraight:
			getPIDController().setPercentTolerance(0.5);
			setSetpoint(setPoint);
			break;
		default:
			setSetpoint(setPoint);
			break;
		}
		enable();
	}
	
	public void endAdjustment() {
		getPIDController().disable();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public double getEncPosition() {
		// TODO: Find Gear Ratio and use to convert sensor position into actual distance.
		return frontLeftMotor.getSensorCollection().getQuadraturePosition();
	}
	
}
