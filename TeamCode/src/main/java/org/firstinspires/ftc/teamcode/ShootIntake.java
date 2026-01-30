package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class ShootIntake {
    private DcMotorEx shooter1, shooter2;
    private Servo shooterAngle;
    private Servo shooterTrigger;
    private CRServo shooterFeeder1;
    private CRServo shooterFeeder2;
    private DcMotorEx intake;

    // Launch Sequence Variables
    private ElapsedTime launchTimer = new ElapsedTime();
    private ElapsedTime loadingTimer = new ElapsedTime();
    private boolean waitingForLaunch = false;
    private boolean launching = false;
    private boolean resetingShot = false;
    private boolean loading = false;
    private double shooterVelocityErrorTolerance = 20;
    public boolean launchSequenceRunning = false;

    // Launch Zone Variables
    private double[] launchZonePositions = {0, 0.5, 1};
    private int launchZoneIndex = 0;
    private String launchZone = "Close Zone";

    // Intake Variables
    public boolean intaking = false;

    // Shooter PID Variables
    private double lastVelocity = 0;
    public double velocityError = 0;
    private double commandedVelocity = 0;
    private double feedFoward = 0;
    private double kP = 0.017;
    private double kV = 0.0006;
    private double kS = 0.18;
    private double maxAccel = 3000;
    private double maxDecel = 1500;
    private ElapsedTime shooterPIDTimer = new ElapsedTime();
    private double dt = 0;



    public double currentShooterAngle = 0;



    public ShootIntake(HardwareMap hardwareMap) {
        shooterAngle = hardwareMap.get(Servo.class, "shooter angle");
        shooterTrigger = hardwareMap.get(Servo.class, "shooter trigger");
        shooterFeeder1 = hardwareMap.get(CRServo.class, "shooter feeder 1");
        shooterFeeder2 = hardwareMap.get(CRServo.class, "shooter feeder 2");

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter 1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter 2");

        shooter1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.FORWARD);
    }


    // Set functions
    public void setShooterPower(double power) {
        shooter1.setPower(power);
        shooter2.setPower(power);
    }

    public void runIntake() {
        intake.setPower(1);
        setShooterFeederPower(1);
    }

    public void stopIntake() {
        intake.setPower(0);
        setShooterFeederPower(0);
    }
    public void shooterTrigger() {
        shooterTrigger.setPosition(0.65);
    }

    public void shooterTriggerReset() {
        shooterTrigger.setPosition(1);
    }

    public void setShooterFeederPower(double power) {
        shooterFeeder1.setPower(power);
        shooterFeeder2.setPower(-power);
    }

    public void setShooterAnglePosition(double position) {
        shooterAngle.setPosition(position);
    }

    // Launch Sequence Functions
    public void launchSequence(double shooterVelocityError) {
        if (waitingForLaunch && Math.abs(shooterVelocityError) <= shooterVelocityErrorTolerance) {
            shooterTrigger();
            launchTimer.reset();
            waitingForLaunch = false;
            launching = true;
        }
        if (launchTimer.seconds() >= 0.3 && launching) {
            shooterTriggerReset();
            resetingShot = true;
            launching = false;
            loadingTimer.reset();
        }

        if (loadingTimer.seconds() >= 0.3 && resetingShot) {
            runIntake();
            loading = true;
            resetingShot = false;
            loadingTimer.reset();
        }

        if (loadingTimer.seconds() >= 0.4 && loading) {
            stopIntake();
            waitingForLaunch = true;
            loading = false;
        }
    }

    public void initiateLaunchSequence() {
        //setShooterAnglePosition(launchZonePositions[launchZoneIndex]);
        waitingForLaunch = true;
    }

    public void endLaunchSequence() {
        if (launching) {
            shooterTriggerReset();
            launching = false;
        }
        if (resetingShot) {
            resetingShot = false;
        }
        if (loadingTimer.seconds() >= 0.4 && loading) {
            stopIntake();
            loading = false;
        }
    }

    // Shooter PID
    public double getShooterPower(double targetVelocity, double currentVelocity) {
        // Loop time
        dt = shooterPIDTimer.seconds();
        dt = Range.clip(dt, 0.001, 0.05); // 1ms–50ms

        // Acceleration rate clamp
        double delta = targetVelocity - commandedVelocity;
        double maxDelta = (delta > 0 ? maxAccel : maxDecel) * dt;
        delta = Range.clip(delta, -maxDelta, maxDelta);
        commandedVelocity += delta;

        shooterPIDTimer.reset();

        // Error Calculation
        velocityError = commandedVelocity - currentVelocity;

        // Feedfoward
        feedFoward = kV * commandedVelocity;
        feedFoward += kS * Math.signum(commandedVelocity);
        /*
        if (Math.abs(commandedVelocity) > 50) {
            feedFoward += kS * Math.signum(commandedVelocity);
        }
        */

        double output = (kP * velocityError) + (feedFoward);

        output = Range.clip(output, -1, 1);
        return output;
    }

    public void resetShooterPID() {
        lastVelocity = 0.0;
        commandedVelocity = 0.0;
    }

    // Launch Zone Setters
    public void setCloseLaunchZone() {
        launchZoneIndex = 0;
    }

    public void setMidLaunchZone() {
        launchZoneIndex = 1;
    }

    public void setFarLaunchZone() {
        launchZoneIndex = 2;
    }

    public String getCurrentLaunchZone() {
        if (launchZoneIndex == 0) {
            launchZone = "Close Zone";
        }
        if (launchZoneIndex == 1) {
            launchZone = "Mid Zone";
        }
        if (launchZoneIndex == 2) {
            launchZone = "Far Zone";
        }
        return  launchZone;
    }

    // Return Shooter Current Velocity
    public double getShooterVelocity() {
        return shooter1.getVelocity();
    }

    // Return Shooter Velocity Error
    public double getShooterVelocityError(double target) {
        return target - shooter1.getVelocity();
    }

    public void shooterAnglePositionUp(double change) {
        currentShooterAngle += change;
        currentShooterAngle = Range.clip(currentShooterAngle, 0, 1);
        setShooterAnglePosition(currentShooterAngle);
    }
    public void shooterAnglePositionDown(double change) {
        currentShooterAngle -= change;
        currentShooterAngle = Range.clip(currentShooterAngle, 0, 1);
        setShooterAnglePosition(currentShooterAngle);
    }

}
