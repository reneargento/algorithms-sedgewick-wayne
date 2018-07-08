package chapter6;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 05/07/18.
 */

// References:
// https://en.wikipedia.org/wiki/Ideal_gas_law
// https://www.grc.nasa.gov/WWW/k-12/airplane/volume.html
// https://courses.lumenlearning.com/boundless-chemistry/chapter/the-ideal-gas-law/
public class Exercise11 {

    // Ideal gas formula: P V = n R T
    // P = pressure
    // V = volume
    // n = number of moles
    // R = ideal gas constant
    // T = temperature
    public class ParticleWithPressure extends Particle {

        private final int DIMENSION = 2;
        private final double BOLTZMANN_CONSTANT = 1.3806488e-23;
        private double pressure;

        public double pressure() {
            return pressure;
        }

        private void addWallCollisionToPressure(boolean isHorizontal) {
            double deltaPositionX;
            double deltaPositionY;

            if (isHorizontal) {
                deltaPositionX = radius;
                deltaPositionY = 0;
            } else {
                deltaPositionX = 0;
                deltaPositionY = radius;
            }

            double deltaVelocityX = velocityX;
            double deltaVelocityY = velocityY;
            double distanceBetweenCenters = radius;

            double deltaPositionByDeltaVelocity = deltaPositionX * deltaVelocityX + deltaPositionY * deltaVelocityY;
            double magnitudeOfNormalForce = 2 * mass * deltaPositionByDeltaVelocity / (mass * distanceBetweenCenters);

            pressure += magnitudeOfNormalForce;
        }

        // V = (pi d^3) / 6
        // V = volume
        // d = diameter
        public double volume() {
            double diameter = radius * 2;
            double volumeInDm3 = (Math.PI * Math.pow(diameter, 3)) / 6;
            // Volume in meters^3
            return volumeInDm3 / 1000;
        }

        public double temperature() {
            double velocityMagnitude = Math.pow(velocityX, 2) + Math.pow(velocityY, 2);
            return mass * Math.pow(velocityMagnitude, 2) / (DIMENSION * BOLTZMANN_CONSTANT);
        }
    }

    public class CollisionSystemWithPressure extends CollisionSystem {

        private int numberOfParticles;

        private final double IDEAL_GAS_CONSTANT = 8.314;
        private final double AVOGRADOS_NUMBER = 6.022e23;

        public CollisionSystemWithPressure(ParticleInterface[] particles) {
            super(particles);
            numberOfParticles = particles.length;
        }

        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(new Event(0, null, null)); // Add redraw event

            int nextCheckpoint = 1000;

            while (!priorityQueue.isEmpty()) {
                Event event = priorityQueue.deleteTop();

                if (event.time >= nextCheckpoint) {
                    validateEquation();
                    nextCheckpoint += 1000;
                }

                if (!event.isValid()) {
                    continue;
                }

                // Update particle positions
                for (int i = 0; i < particles.length; i++) {
                    particles[i].move(event.time - time);
                }
                // Update time
                time = event.time;

                ParticleWithPressure particleA = (ParticleWithPressure) event.particleA;
                ParticleWithPressure particleB = (ParticleWithPressure) event.particleB;

                if (particleA != null && particleB != null) {
                    particleA.bounceOff(particleB);
                } else if (particleA != null && particleB == null) {
                    particleA.bounceOffVerticalWall();
                    particleA.addWallCollisionToPressure(false);
                } else if (particleA == null && particleB != null) {
                    particleB.bounceOffHorizontalWall();
                    particleB.addWallCollisionToPressure(true);
                } else if (particleA == null && particleB == null) {
                    redraw(limit, hertz);
                }

                predictCollisions(particleA, limit);
                predictCollisions(particleB, limit);
            }
        }

        // P V = n R T
        // P = (n R T) / V
        private void validateEquation() {
            double pressureMeasuredWithEquation = (numberOfMoles() * IDEAL_GAS_CONSTANT * temperature()) / volume();
            // Pressure in gases is always positive
            double pressureMeasuredWithWallCollisions = Math.abs(pressure());

            StdOut.println("Pressure measured with P V = n R T: " + pressureMeasuredWithEquation);
            StdOut.println("Pressure measured with wall collisions: " + pressureMeasuredWithWallCollisions);
            StdOut.println();
        }

        public double pressure() {
            double systemPressure = 0;

            for(ParticleInterface particle : particles) {
                ParticleWithPressure particleWithPressure = (ParticleWithPressure) particle;
                systemPressure += particleWithPressure.pressure();
            }

            return systemPressure;
        }

        public double volume() {
            double systemVolume = 0;

            for(ParticleInterface particle : particles) {
                ParticleWithPressure particleWithPressure = (ParticleWithPressure) particle;
                systemVolume += particleWithPressure.volume();
            }

            return systemVolume;
        }

        // n = m / mw
        // n = number of moles
        // m = total mass
        // mw = molecular weight
        //
        // mw = pm * AC
        // pm = mass of one particle
        // AC = Avogrado's number
        public double numberOfMoles() {
            // All particle masses are the same
            double particleMass = ((ParticleWithPressure) particles[0]).mass;
            double totalMass = particleMass * particles.length;

            double molecularWeight = particleMass * AVOGRADOS_NUMBER;
            return totalMass / molecularWeight;
        }

        public double temperature() {
            double totalTemperature = 0;

            for(ParticleInterface particle : particles) {
                ParticleWithPressure particleWithPressure = (ParticleWithPressure) particle;
                totalTemperature += particleWithPressure.temperature();
            }

            return totalTemperature / numberOfParticles;
        }
    }

    public static void main(String[] args) {
        Exercise11 exercise11 = new Exercise11();

        int numberOfParticles = 20;
        ParticleInterface[] particles = new ParticleWithPressure[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = exercise11.new ParticleWithPressure();
        }

        CollisionSystem collisionSystem = exercise11.new CollisionSystemWithPressure(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}
