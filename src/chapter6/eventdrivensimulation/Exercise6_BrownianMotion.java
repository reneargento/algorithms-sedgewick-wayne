package chapter6.eventdrivensimulation;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by Rene Argento on 25/06/18.
 */
// For better visibility of the phenomenon 10,000 particles are used instead of millions.
// If an invalid state of overlapping particles occur during initialization, rerun the program until the random
// positioning of the particles do not overlap.
public class Exercise6_BrownianMotion {

    private static double MIN_DIMENSION_SIZE = 0;
    private static double MAX_DIMENSION_SIZE = 90;

    public class ParticleWithColor extends Particle {

        private Color color;

        ParticleWithColor(boolean isSmall) {
            positionX = StdRandom.uniform(MIN_DIMENSION_SIZE, MAX_DIMENSION_SIZE);
            positionY = StdRandom.uniform(MIN_DIMENSION_SIZE, MAX_DIMENSION_SIZE);

            // In Brownian motion particles move fast
            velocityX = StdRandom.uniform(-0.05, 0.05);
            velocityY = StdRandom.uniform(-0.05, 0.05);

            if (isSmall) {
                radius = 0.0000001;
                color = Color.black;
            } else {
                radius = 0.2;
                color = Color.red;
            }

            mass = 0.5;
        }

        public void draw() {
            StdDraw.setPenColor(color);
            StdDraw.filledCircle(positionX, positionY, radius);
        }

        public double timeToHitHorizontalWall() {
            double distance;

            if (velocityY < 0) {
                // Get the distance as a negative number
                distance = radius - positionY;
            } else if (velocityY > 0) {
                distance = MAX_DIMENSION_SIZE - radius - positionY;
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
                distance = MAX_DIMENSION_SIZE - radius - positionX;
            } else {
                return Double.POSITIVE_INFINITY;
            }

            return distance / velocityX;
        }
    }

    public static void main(String[] args) {
        Exercise6_BrownianMotion brownianMotion = new Exercise6_BrownianMotion();
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setScale(MIN_DIMENSION_SIZE, MAX_DIMENSION_SIZE);
        StdDraw.enableDoubleBuffering();

        int numberOfSmallParticles = 10000;
        int numberOfBigParticles = 5;
        int totalNumberOfParticles = numberOfBigParticles + numberOfSmallParticles;
        ParticleInterface[] particles = new Particle[totalNumberOfParticles];

        int particleIndex;

        for (particleIndex = 0; particleIndex < numberOfBigParticles; particleIndex++) {
            particles[particleIndex] = brownianMotion.new ParticleWithColor(false);
        }

        while (particleIndex < totalNumberOfParticles) {
            particles[particleIndex] = brownianMotion.new ParticleWithColor(true);
            particleIndex++;
        }

        CollisionSystem collisionSystem = new CollisionSystem(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}