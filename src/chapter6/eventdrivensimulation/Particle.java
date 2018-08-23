package chapter6.eventdrivensimulation;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 27/04/18.
 */
public class Particle implements ParticleInterface {
    double positionX;
    double positionY;

    double velocityX;
    double velocityY;

    double radius;
    double mass;

    private int numberOfCollisions;

    Particle() {
        positionX = StdRandom.uniform(0.0, 1.0);
        positionY = StdRandom.uniform(0.0, 1.0);

        velocityX = StdRandom.uniform(-0.005, 0.005);
        velocityY = StdRandom.uniform(-0.005, 0.005);

        radius = 0.01;
        mass = 0.5;
    }

    Particle(double positionX, double positionY, double velocityX, double velocityY, double radius, double mass) {
        this.positionX = positionX;
        this.positionY = positionY;

        this.velocityX = velocityX;
        this.velocityY = velocityY;

        this.radius = radius;
        this.mass = mass;
    }

    public void draw() {
        StdDraw.filledCircle(positionX, positionY, radius);
    }

    public void move(double time) {
        positionX = positionX + (velocityX * time);
        positionY = positionY + (velocityY * time);
    }

    public int count() {
        return numberOfCollisions;
    }

    // Based on the explanation at https://algs4.cs.princeton.edu/61event/
    public double timeToHit(ParticleInterface otherParticleInterface) {
        Particle otherParticle = (Particle) otherParticleInterface;

        if (this == otherParticle) {
            return Double.POSITIVE_INFINITY;
        }

        double deltaPositionX = otherParticle.positionX - positionX;
        double deltaPositionY = otherParticle.positionY - positionY;

        double deltaVelocityX = otherParticle.velocityX - velocityX;
        double deltaVelocityY = otherParticle.velocityY - velocityY;

        double deltaPositionByDeltaVelocity = deltaPositionX * deltaVelocityX + deltaPositionY * deltaVelocityY;
        if (deltaPositionByDeltaVelocity > 0) {
            return Double.POSITIVE_INFINITY;
        }

        double deltaVelocitySquared = deltaVelocityX * deltaVelocityX + deltaVelocityY * deltaVelocityY;
        double deltaPositionSquared = deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY;

        double distanceBetweenCenters = radius + otherParticle.radius;
        double distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters;

        // Check if particles overlap
        if (deltaPositionSquared < distanceBetweenCentersSquared) {
            throw new IllegalStateException("Invalid state: overlapping particles. No two objects can occupy the same space " +
                    "at the same time.");
        }

        double distance = (deltaPositionByDeltaVelocity * deltaPositionByDeltaVelocity)
                - deltaVelocitySquared * (deltaPositionSquared - distanceBetweenCentersSquared);

        if (distance < 0) {
            return Double.POSITIVE_INFINITY;
        }

        return -(deltaPositionByDeltaVelocity + Math.sqrt(distance)) / deltaVelocitySquared;
    }

    public double timeToHitHorizontalWall() {
        double distance;

        if (velocityY < 0) {
            // Get the distance as a negative number
            distance = radius - positionY;
        } else if (velocityY > 0) {
            distance = 1 - radius - positionY;
        } else {
            return Double.POSITIVE_INFINITY;
        }

        return distance / velocityY;
    }

    public double timeToHitVerticalWall() {
        double distance;

        if (velocityX < 0) {
            // Get the distance as a negative number
            distance = radius - positionX;
        } else if (velocityX > 0) {
            distance = 1 - radius - positionX;
        } else {
            return Double.POSITIVE_INFINITY;
        }

        return distance / velocityX;
    }

    // Based on the explanation at https://algs4.cs.princeton.edu/61event/
    public void bounceOff(ParticleInterface otherParticleInterface) {
        Particle otherParticle = (Particle) otherParticleInterface;

        double deltaPositionX = otherParticle.positionX - positionX;
        double deltaPositionY = otherParticle.positionY - positionY;

        double deltaVelocityX = otherParticle.velocityX - velocityX;
        double deltaVelocityY = otherParticle.velocityY - velocityY;

        double deltaPositionByDeltaVelocity = deltaPositionX * deltaVelocityX + deltaPositionY * deltaVelocityY;

        double distanceBetweenCenters = radius + otherParticle.radius;

        // Compute normal force
        double magnitudeOfNormalForce = 2 * mass * otherParticle.mass * deltaPositionByDeltaVelocity
                / ((mass + otherParticle.mass) * distanceBetweenCenters);

        double normalForceInXDirection = magnitudeOfNormalForce * deltaPositionX / distanceBetweenCenters;
        double normalForceInYDirection = magnitudeOfNormalForce * deltaPositionY / distanceBetweenCenters;

        // Update velocities according to the normal force
        velocityX += normalForceInXDirection / mass;
        velocityY += normalForceInYDirection / mass;
        otherParticle.velocityX -= normalForceInXDirection / otherParticle.mass;
        otherParticle.velocityY -= normalForceInYDirection / otherParticle.mass;

        // Update collision counts
        numberOfCollisions++;
        otherParticle.numberOfCollisions++;
    }

    public void bounceOffHorizontalWall() {
        velocityY = -velocityY;
        numberOfCollisions++;
    }

    public void bounceOffVerticalWall() {
        velocityX = -velocityX;
        numberOfCollisions++;
    }

    // Kinetic energy = 1/2 * mass * velocity^2
    public double kineticEnergy() {
        return 0.5 * mass * (velocityX * velocityX + velocityY * velocityY);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

}