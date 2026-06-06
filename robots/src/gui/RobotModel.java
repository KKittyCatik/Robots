package gui;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class RobotModel
{
    private volatile double robotPositionX = 100;
    private volatile double robotPositionY = 100;
    private volatile double robotDirection = 0;

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;

    public static final double MAX_VELOCITY = 0.1;
    public static final double MAX_ANGULAR_VELOCITY = 0.001;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        support.removePropertyChangeListener(l);
    }

    public double getRobotPositionX() { return robotPositionX; }
    public double getRobotPositionY() { return robotPositionY; }
    public double getRobotDirection() { return robotDirection; }
    public int getTargetPositionX() { return targetPositionX; }
    public int getTargetPositionY() { return targetPositionY; }

    public void setTargetPosition(Point p)
    {
        targetPositionX = p.x;
        targetPositionY = p.y;
    }

    public void update(int fieldWidth, int fieldHeight)
    {
        double distance = distance(
            targetPositionX, targetPositionY,
            robotPositionX, robotPositionY);
        if (distance < 0.5)
            return;

        double angleToTarget = angleTo(
            robotPositionX, robotPositionY,
            targetPositionX, targetPositionY);

        double angleDiff = angleToTarget - robotDirection;
        while (angleDiff >  Math.PI) angleDiff -= 2 * Math.PI;
        while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;

        double angularVelocity = 0;
        if (angleDiff > 0) angularVelocity =  MAX_ANGULAR_VELOCITY;
        if (angleDiff < 0) angularVelocity = -MAX_ANGULAR_VELOCITY;

        moveRobot(MAX_VELOCITY, angularVelocity, 10, fieldWidth, fieldHeight);
    }

    private void moveRobot(double velocity, double angularVelocity,
                           double duration, int fieldWidth, int fieldHeight)
    {
        velocity = applyLimits(velocity, 0, MAX_VELOCITY);
        angularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);

        double newX = robotPositionX + velocity / angularVelocity *
            (Math.sin(robotDirection + angularVelocity * duration) - Math.sin(robotDirection));
        if (!Double.isFinite(newX))
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);

        double newY = robotPositionY - velocity / angularVelocity *
            (Math.cos(robotDirection + angularVelocity * duration) - Math.cos(robotDirection));
        if (!Double.isFinite(newY))
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);

        newX = applyLimits(newX, 0, fieldWidth);
        newY = applyLimits(newY, 0, fieldHeight);

        robotPositionX = newX;
        robotPositionY = newY;
        robotDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);

        support.firePropertyChange("position", null, this);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2, dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        return asNormalizedRadians(Math.atan2(toY - fromY, toX - fromX));
    }

    private static double applyLimits(double value, double min, double max)
    {
        return Math.max(min, Math.min(max, value));
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle <  0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
}