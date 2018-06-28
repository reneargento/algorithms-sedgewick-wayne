package chapter6;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/06/18.
 */
// Particle.bounceOff(ParticleInterface) method (based on https://algs4.cs.princeton.edu/61event/) does not compute the
// exact resulting velocities after a particle collision. There is a round-off error.
// Due to that, the temperature (which is based on the velocities) is not constant.
// However, if there were no round-off error the temperature would be constant.
public class Exercise7 {

    public class ParticleWithTemperature extends Particle {

        private final int DIMENSION = 2;
        private final double BOLTZMANN_CONSTANT = 1.3806488e-23;

        public double temperature() {
            double velocityMagnitude = Math.pow(velocityX, 2) + Math.pow(velocityY, 2);
            return getMass() * Math.pow(velocityMagnitude, 2) / (DIMENSION * BOLTZMANN_CONSTANT);
        }
    }

    public class CollisionSystemWithTemperature extends CollisionSystem {

        private int numberOfParticles;
        private double currentTemperature;
        private boolean isTemperatureConstant = true;

        public CollisionSystemWithTemperature(ParticleInterface[] particles) {
            super(particles);
            numberOfParticles = particles.length;
        }

        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(new Event(0, null, null)); // Add redraw event

            int nextCheckpoint = 0;

            while (!priorityQueue.isEmpty()) {
                Event event = priorityQueue.deleteTop();

                if (event.time >= nextCheckpoint) {
                    checkNewTemperature(nextCheckpoint);
                    StdOut.println("Temperature: " + currentTemperature);
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

                ParticleInterface particleA = event.particleA;
                ParticleInterface particleB = event.particleB;

                if (particleA != null && particleB != null) {
                    particleA.bounceOff(particleB);
                } else if (particleA != null && particleB == null) {
                    particleA.bounceOffVerticalWall();
                } else if (particleA == null && particleB != null) {
                    particleB.bounceOffHorizontalWall();
                } else if (particleA == null && particleB == null) {
                    redraw(limit, hertz);
                }

                predictCollisions(particleA, limit);
                predictCollisions(particleB, limit);
            }

            StdOut.println();

            if (isTemperatureConstant) {
                StdOut.println("Temperature is constant!");
            } else {
                StdOut.println("Temperature is not constant.");
            }
        }

        private void checkNewTemperature(int checkpoint) {
            double newTemperature = temperature();

            if (checkpoint != 0) {
                double differenceInTemperature = newTemperature - currentTemperature;

                if (differenceInTemperature != 0) {
                    isTemperatureConstant = false;
                }
            }

            currentTemperature = newTemperature;
        }

        public double temperature() {
            double totalTemperature = 0;

            for(ParticleInterface particle : particles) {
                ParticleWithTemperature particleWithTemperature = (ParticleWithTemperature) particle;
                totalTemperature += particleWithTemperature.temperature();
            }

            return totalTemperature / numberOfParticles;
        }

    }

    public static void main(String[] args) {
        Exercise7 exercise7 = new Exercise7();

        int numberOfParticles = 10;
        ParticleInterface[] particles = new ParticleWithTemperature[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = exercise7.new ParticleWithTemperature();
        }

        CollisionSystem collisionSystem = exercise7.new CollisionSystemWithTemperature(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}
