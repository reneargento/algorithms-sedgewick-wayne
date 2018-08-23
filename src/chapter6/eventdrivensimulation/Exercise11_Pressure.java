package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 05/07/18.
 */

// References:
// https://en.wikipedia.org/wiki/Ideal_gas_law
// https://www.mathopenref.com/spherevolume.html
// https://courses.lumenlearning.com/boundless-chemistry/chapter/the-ideal-gas-law/
// https://physics.stackexchange.com/questions/221789/why-the-round-trip-instead-collision-time
public class Exercise11_Pressure {

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
            if (isHorizontal) {
                pressure += Math.abs(2 * mass * velocityY);
            } else {
                pressure += Math.abs(2 * mass * velocityX);
            }
        }

        // V = 4/3 (pi r^3)
        // V = volume
        // r = radius
        public double volume() {
            double volumeInCm3 = 4.0/3.0 * Math.PI * Math.pow(radius, 3);
            // Volume in dm^3
            return volumeInCm3 / 1000;
        }

        public double temperature() {
            double velocityMagnitude = Math.pow(velocityX, 2) + Math.pow(velocityY, 2);
            return mass * Math.pow(velocityMagnitude, 2) / (DIMENSION * BOLTZMANN_CONSTANT);
        }
    }

    public class CollisionSystemWithPressure extends CollisionSystem {

        private int numberOfParticles;

        private final double IDEAL_GAS_CONSTANT = 0.082057;
        private final double AVOGADROS_NUMBER = 6.022e23;

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
                    validateEquation(time);
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
        private void validateEquation(double time) {
            double pressureMeasuredWithEquation = (numberOfMoles() * IDEAL_GAS_CONSTANT * temperature()) / volume();
            double pressureMeasuredWithWallCollisions = pressure(time);

            StdOut.println("Pressure measured with P V = n R T: " + pressureMeasuredWithEquation);
            StdOut.println("Pressure measured with wall collisions: " + pressureMeasuredWithWallCollisions);
            StdOut.println();
        }

        public double pressure(double time) {
            double systemPressure = 0;

            for(ParticleInterface particle : particles) {
                ParticleWithPressure particleWithPressure = (ParticleWithPressure) particle;
                systemPressure += particleWithPressure.pressure();
            }

            return systemPressure / time;
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
        // AC = Avogadro's number
        public double numberOfMoles() {
            // All particle masses are the same
            double particleMass = ((ParticleWithPressure) particles[0]).mass;
            double totalMass = particleMass * particles.length;

            double molecularWeight = particleMass * AVOGADROS_NUMBER;
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
        Exercise11_Pressure pressure = new Exercise11_Pressure();

        int numberOfParticles = 20;
        ParticleInterface[] particles = new ParticleWithPressure[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = pressure.new ParticleWithPressure();
        }

        CollisionSystem collisionSystem = pressure.new CollisionSystemWithPressure(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}
