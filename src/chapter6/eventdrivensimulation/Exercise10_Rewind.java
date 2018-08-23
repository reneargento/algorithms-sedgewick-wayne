package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 05/07/18.
 */
public class Exercise10_Rewind {

    // Used to compute the roundoff error between the final and original states of the system
    private class State {
        private double positionX;
        private double positionY;

        State(double positionX, double positionY) {
            this.positionX = positionX;
            this.positionY = positionY;
        }
    }

    private class CollisionSystemRewind extends CollisionSystem {

        public CollisionSystemRewind(ParticleInterface[] particles) {
            super(particles);
        }

        @Override
        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);
            State[] originalState = getCurrentState();
            boolean isDoingRewind = false;

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(new Event(0, null, null)); // Add redraw event

            while (!priorityQueue.isEmpty()) {
                Event event = priorityQueue.deleteTop();

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

                if (priorityQueue.isEmpty()) {
                    if (!isDoingRewind) {
                        isDoingRewind = true;
                        beginRewind(limit);
                    } else {
                        break;
                    }
                }
            }

            computeRoundOffError(originalState);
        }

        private void beginRewind(double limit) {
            negateAllVelocities(particles);
            time = 0;

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }

            priorityQueue.insert(new Event(0, null, null)); // Add redraw event
        }

        private void negateAllVelocities(ParticleInterface[] particles) {

            for (ParticleInterface particleInterface : particles) {
                Particle particle = (Particle) particleInterface;
                particle.velocityX = -particle.velocityX;
                particle.velocityY = -particle.velocityY;
            }

        }

        private State[] getCurrentState() {
            State[] particleStates = new State[particles.length];

            for (int i = 0; i < particles.length; i++) {
                Particle particle = (Particle) particles[i];
                particleStates[i] = new State(particle.positionX, particle.positionY);
            }

            return particleStates;
        }

        private void computeRoundOffError(State[] originalState) {
            State[] finalState = getCurrentState();

            double sumOfSquareErrors = 0;

            for (int i = 0; i < particles.length; i++) {
                double errorX = Math.pow(originalState[i].positionX - finalState[i].positionX, 2);
                double errorY = Math.pow(originalState[i].positionY - finalState[i].positionY, 2);
                sumOfSquareErrors += errorX + errorY;
            }

            double meanSquareError = sumOfSquareErrors / particles.length;
            StdOut.println("Roundoff error: " + meanSquareError);
        }
    }

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();

        int numberOfParticles = 10;
        double limit = 10000;
        double hertz = 0.5;
        ParticleInterface[] particles = new Particle[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = new Particle();
        }

        CollisionSystem collisionSystem = new Exercise10_Rewind().new CollisionSystemRewind(particles);
        collisionSystem.simulate(limit, hertz);
    }

}
