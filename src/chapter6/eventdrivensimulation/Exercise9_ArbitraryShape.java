package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by Rene Argento on 01/07/18.
 */
public class Exercise9_ArbitraryShape {

    private static final double BOUNDARY_WALL_LEFT = 0.4;
    private static final double BOUNDARY_WALL_RIGHT = 0.6;
    private static final double BOUNDARY_WALL_END_UP = 0.45;     // Range covered: [0 - 0.45]
    private static final double BOUNDARY_WALL_START_DOWN = 0.55; // Range covered: [0.55 - 1]

    private static final Color LEFT_VESSEL_COLOR = Color.BLUE;
    private static final Color RIGHT_VESSEL_COLOR = Color.GREEN;

    private enum EventType {
        PARTICLE_COLLISION, HORIZONTAL_WALL_COLLISION, VERTICAL_WALL_COLLISION, VERTICAL_BOUNDARY_WALL_COLLISION,
        HORIZONTAL_BOUNDARY_WALL_COLLISION, DRAW
    }

    private class ParticleArbitraryShape extends Particle {

        private Color color;

        ParticleArbitraryShape(double positionX, double positionY, double velocityX, double velocityY, double radius,
                               double mass, Color color) {
            super(positionX, positionY, velocityX, velocityY, radius, mass);
            this.color = color;
        }

        public double timeToHitHorizontalBoundaryWall() {
            double distance;

            if (velocityY < 0 && positionY - radius >= BOUNDARY_WALL_END_UP) {
                // Get the distance as a negative number
                distance = radius - (positionY - BOUNDARY_WALL_END_UP);
            } else if (velocityY > 0 && positionY + radius <= BOUNDARY_WALL_START_DOWN) {
                distance = BOUNDARY_WALL_START_DOWN - radius - positionY;
            } else {
                return Double.POSITIVE_INFINITY;
            }

            return distance / velocityY;
        }

        public double timeToHitVerticalBoundaryWall() {
            double distance;

            if (velocityX < 0 && positionX - radius >= BOUNDARY_WALL_RIGHT) {
                // Get the distance as a negative number
                distance = radius - (positionX - BOUNDARY_WALL_RIGHT);
            } else if (velocityX > 0 && positionX + radius <= BOUNDARY_WALL_LEFT) {
                distance = BOUNDARY_WALL_LEFT - radius - positionX;
            } else {
                return Double.POSITIVE_INFINITY;
            }

            return distance / velocityX;
        }

        public void bounceOffHorizontalBoundaryWall() {
            bounceOffHorizontalWall();
        }

        public void bounceOffVerticalBoundaryWall() {
            bounceOffVerticalWall();
        }
    }

    private class CollisionSystemArbitraryShape {

        private class Event implements Comparable<Event> {

            private final double time;
            private final ParticleArbitraryShape particleA;
            private final ParticleArbitraryShape particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;
            private final EventType eventType;

            public Event(double time, ParticleArbitraryShape particleA, ParticleArbitraryShape particleB,
                         EventType eventType) {
                this.time = time;
                this.particleA = particleA;
                this.particleB = particleB;
                this.eventType = eventType;

                if (particleA != null) {
                    collisionsCountA = particleA.count();
                } else {
                    collisionsCountA = -1;
                }

                if (particleB != null) {
                    collisionsCountB = particleB.count();
                } else {
                    collisionsCountB = -1;
                }
            }

            public int compareTo(Event otherEvent) {
                if (this.time < otherEvent.time) {
                    return -1;
                } else if (this.time > otherEvent.time) {
                    return +1;
                } else {
                    return 0;
                }
            }

            public boolean isValid() {
                if (particleA != null && particleA.count() != collisionsCountA) {
                    return false;
                }

                if (particleB != null && particleB.count() != collisionsCountB) {
                    return false;
                }

                return true;
            }
        }

        private PriorityQueueResize<Event> priorityQueue;
        private double time;
        private ParticleArbitraryShape[] particles;

        public CollisionSystemArbitraryShape(ParticleArbitraryShape[] particles) {
            this.particles = particles;
            drawPipe();
        }

        // Pipe coordinates
        // X: 0.4 to 0.6
        // Y: upper: 0 to 0.45
        //    lower: 0.55 to 1
        private void drawPipe() {
            StdDraw.setPenColor(Color.BLACK);

            double centerX = 0.5;
            double halfWidth = 0.1;

            double halHeight = 0.225;

            // Lower pipe
            double lowerY = 0.775;
            StdDraw.filledRectangle(centerX, lowerY, halfWidth, halHeight);

            // Upper pipe
            double upperY = 0.225;
            StdDraw.filledRectangle(centerX, upperY, halfWidth, halHeight);
        }

        public void predictCollisions(ParticleArbitraryShape particle, double limit) {
            if (particle == null) {
                return;
            }

            for (int i = 0; i < particles.length; i++) {
                double deltaTime = particle.timeToHit(particles[i]);

                if (time + deltaTime <= limit) {
                    priorityQueue.insert(new Event(time + deltaTime, particle, particles[i],
                            EventType.PARTICLE_COLLISION));
                }
            }

            double deltaTimeVerticalWall = particle.timeToHitVerticalWall();
            insertWallCollisionEventIfValid(deltaTimeVerticalWall, limit, particle, EventType.VERTICAL_WALL_COLLISION);

            double deltaTimeHorizontalWall = particle.timeToHitHorizontalWall();
            insertWallCollisionEventIfValid(deltaTimeHorizontalWall, limit, particle, EventType.HORIZONTAL_WALL_COLLISION);

            double deltaTimeVerticalBoundaryWall = particle.timeToHitVerticalBoundaryWall();
            insertWallCollisionEventIfValid(deltaTimeVerticalBoundaryWall, limit, particle,
                    EventType.VERTICAL_BOUNDARY_WALL_COLLISION);

            double deltaTimeHorizontalBoundaryWall = particle.timeToHitHorizontalBoundaryWall();
            insertWallCollisionEventIfValid(deltaTimeHorizontalBoundaryWall, limit, particle,
                    EventType.HORIZONTAL_BOUNDARY_WALL_COLLISION);
        }

        private void insertWallCollisionEventIfValid(double deltaTime, double limit, ParticleArbitraryShape particle,
                                                     EventType eventType) {
            if (time + deltaTime <= limit) {
                priorityQueue.insert(new Event(time + deltaTime, particle, null, eventType));
            }
        }

        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(new Event(0, null, null, EventType.DRAW)); // Add redraw event

            int nextCheckpoint = 0;

            while (!priorityQueue.isEmpty()) {
                Event event = priorityQueue.deleteTop();

                if (event.time >= nextCheckpoint) {
                    computeFractionOfParticlesInEachVessel(event.time);
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

                ParticleArbitraryShape particleA = event.particleA;
                ParticleArbitraryShape particleB = event.particleB;

                boolean ignoreEvent = false;

                switch (event.eventType) {
                    case PARTICLE_COLLISION:
                        particleA.bounceOff(particleB);
                        break;
                    case VERTICAL_WALL_COLLISION:
                        particleA.bounceOffVerticalWall();
                        break;
                    case HORIZONTAL_WALL_COLLISION:
                        particleA.bounceOffHorizontalWall();
                        break;
                    case VERTICAL_BOUNDARY_WALL_COLLISION:
                        if (particleA.positionY < BOUNDARY_WALL_END_UP + particleA.radius
                                || particleA.positionY > BOUNDARY_WALL_START_DOWN - particleA.radius) {
                            particleA.bounceOffVerticalBoundaryWall();
                        } else {
                            ignoreEvent = true;
                        }
                        break;
                    case HORIZONTAL_BOUNDARY_WALL_COLLISION:
                        if (BOUNDARY_WALL_LEFT - particleA.radius < particleA.positionX
                            && particleA.positionX < BOUNDARY_WALL_RIGHT + particleA.radius) {
                            particleA.bounceOffHorizontalBoundaryWall();
                        } else {
                            ignoreEvent = true;
                        }
                        break;
                    case DRAW:
                        redraw(limit, hertz);
                        break;
                }

                if (!ignoreEvent) {
                    predictCollisions(particleA, limit);
                    predictCollisions(particleB, limit);
                }
            }
        }

        public void redraw(double limit, double hertz) {
            StdDraw.clear();
            drawPipe();

            for (int i = 0; i < particles.length; i++) {
                StdDraw.setPenColor(particles[i].color);
                particles[i].draw();
            }

            StdDraw.pause(20);
            StdDraw.show();

            if (time < limit) {
                priorityQueue.insert(new Event(time + 1.0 / hertz, null, null, EventType.DRAW));
            }
        }

        public void computeFractionOfParticlesInEachVessel(double time) {
            // Particles currently in the pipe are not in any vessel
            int particlesOfType1InLeftVessel = 0;
            int particlesOfType1InRightVessel = 0;

            int particlesOfType2InLeftVessel = 0;
            int particlesOfType2InRightVessel = 0;

            int totalNumberOfParticlesInLeftVessel = 0;
            int totalNumberOfParticlesInRightVessel = 0;

            for (ParticleArbitraryShape particle : particles) {
                if (particle.positionX <= BOUNDARY_WALL_LEFT) {
                    totalNumberOfParticlesInLeftVessel++;

                    if (particle.color == LEFT_VESSEL_COLOR) {
                        particlesOfType1InLeftVessel++;
                    } else {
                        particlesOfType2InLeftVessel++;
                    }
                } else if (particle.positionX >= BOUNDARY_WALL_RIGHT) {
                    totalNumberOfParticlesInRightVessel++;

                    if (particle.color == LEFT_VESSEL_COLOR) {
                        particlesOfType1InRightVessel++;
                    } else {
                        particlesOfType2InRightVessel++;
                    }
                }
            }

            StdOut.println("Time " + (int) time);
            StdOut.println("LEFT VESSEL: " + totalNumberOfParticlesInLeftVessel + " particles");
            StdOut.println("Type 1: " + particlesOfType1InLeftVessel + "/" + totalNumberOfParticlesInLeftVessel +
            " Type 2: " + particlesOfType2InLeftVessel + "/" + totalNumberOfParticlesInLeftVessel);

            StdOut.println("RIGHT VESSEL: " + totalNumberOfParticlesInRightVessel + " particles");
            StdOut.println("Type 1: " + particlesOfType1InRightVessel + "/" + totalNumberOfParticlesInRightVessel +
                    " Type 2: " + particlesOfType2InRightVessel + "/" + totalNumberOfParticlesInRightVessel);
            StdOut.println();
        }
    }

    private ParticleArbitraryShape generateParticleInLeftVessel() {
        double positionX = StdRandom.uniform(0.0, BOUNDARY_WALL_LEFT);
        double positionY = StdRandom.uniform(0.0, 1.0);

        double velocityX = 0.005;
        double velocityY = -0.005;

        double radius = 0.005;
        double mass = 0.5;

        return new ParticleArbitraryShape(positionX, positionY, velocityX, velocityY, radius, mass, LEFT_VESSEL_COLOR);
    }

    private ParticleArbitraryShape generateParticleInRightVessel() {
        double positionX = StdRandom.uniform(BOUNDARY_WALL_RIGHT, 1.0);
        double positionY = StdRandom.uniform(0.0, 1.0);

        double velocityX = 0.01;
        double velocityY = 0.01;

        double radius = 0.015;
        double mass = 1;

        return new ParticleArbitraryShape(positionX, positionY, velocityX, velocityY, radius, mass, RIGHT_VESSEL_COLOR);
    }

    public static void main(String[] args) {
        Exercise9_ArbitraryShape arbitraryShape = new Exercise9_ArbitraryShape();
        StdDraw.enableDoubleBuffering();

        int numberOfParticles = 20;
        ParticleArbitraryShape[] particles = new ParticleArbitraryShape[numberOfParticles];

        for (int i = 0; i < numberOfParticles / 2; i++) {
            particles[i] = arbitraryShape.generateParticleInLeftVessel();
        }

        for (int i = numberOfParticles / 2; i < numberOfParticles; i++) {
            particles[i] = arbitraryShape.generateParticleInRightVessel();
        }

        CollisionSystemArbitraryShape collisionSystem = arbitraryShape.new CollisionSystemArbitraryShape(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}