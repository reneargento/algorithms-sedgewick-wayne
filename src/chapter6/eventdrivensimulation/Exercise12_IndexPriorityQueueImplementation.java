package chapter6.eventdrivensimulation;

import chapter2.section4.IndexMinPriorityQueue;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento on 10/07/18.
 */
public class Exercise12_IndexPriorityQueueImplementation {

    public class ParticleWithId extends Particle {
        private int id;

        ParticleWithId(int id) {
            super();
            this.id = id;
        }

        ParticleWithId(int id, double positionX, double positionY, double velocityX, double velocityY, double radius,
                       double mass) {
            super(positionX, positionY, velocityX, velocityY, radius, mass);
            this.id = id;
        }
    }

    public class CollisionSystemIndexPriorityQueue {

        private class Event implements Comparable<Event> {

            private final double time;
            private final ParticleWithId particleA;
            private final ParticleWithId particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;

            public Event(double time, ParticleWithId particleA, ParticleWithId particleB) {
                this.time = time;
                this.particleA = particleA;
                this.particleB = particleB;

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

        private IndexMinPriorityQueue<Event> priorityQueue;
        private double time;
        private ParticleWithId[] particles;
        private final int DRAW_EVENT_ID;

        public CollisionSystemIndexPriorityQueue(ParticleWithId[] particles) {
            StdDraw.enableDoubleBuffering();
            this.particles = particles;
            DRAW_EVENT_ID = particles.length;
        }

        private void predictCollisions(ParticleWithId particle, double limit) {
            if (particle == null) {
                return;
            }

            int particleId = particle.id;

            if (priorityQueue.contains(particleId)) {
                priorityQueue.delete(particleId);
            }

            double currentSmallestEventTime = Double.POSITIVE_INFINITY;

            for (int i = 0; i < particles.length; i++) {
                double deltaTime = particle.timeToHit(particles[i]);
                double eventTime = time + deltaTime;

                if (eventTime <= limit && eventTime <= currentSmallestEventTime) {
                    if (priorityQueue.contains(particleId)) {
                        priorityQueue.delete(particleId);
                    }

                    priorityQueue.insert(particleId, new Event(eventTime, particle, particles[i]));
                    currentSmallestEventTime = eventTime;
                }
            }

            double deltaTimeVerticalWall = particle.timeToHitVerticalWall();
            double deltaTimeVerticalWallEventTime = time + deltaTimeVerticalWall;

            if (deltaTimeVerticalWallEventTime <= limit && deltaTimeVerticalWallEventTime <= currentSmallestEventTime) {
                if (priorityQueue.contains(particleId)) {
                    priorityQueue.delete(particleId);
                }

                priorityQueue.insert(particleId, new Event(deltaTimeVerticalWallEventTime, particle, null));
                currentSmallestEventTime = deltaTimeVerticalWallEventTime;
            }

            double deltaTimeHorizontalWall = particle.timeToHitHorizontalWall();
            double deltaTimeHorizontalWallEventTime = time + deltaTimeHorizontalWall;

            if (deltaTimeHorizontalWallEventTime <= limit  && deltaTimeHorizontalWallEventTime <= currentSmallestEventTime) {
                if (priorityQueue.contains(particleId)) {
                    priorityQueue.delete(particleId);
                }

                priorityQueue.insert(particleId, new Event(deltaTimeHorizontalWallEventTime, null, particle));
            }
        }

        public void redraw(double limit, double hertz) {
            StdDraw.clear();

            for (int i = 0; i < particles.length; i++) {
                particles[i].draw();
            }

            StdDraw.pause(20);
            StdDraw.show();

            if (time < limit) {
                priorityQueue.insert(DRAW_EVENT_ID, new Event(time + 1.0 / hertz, null, null));
            }
        }

        public void simulate(double limit, double hertz) {
            // Add 1 extra space for the draw event
            priorityQueue = new IndexMinPriorityQueue<>(particles.length + 1);

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(DRAW_EVENT_ID, new Event(0, null, null)); // Add redraw event

            while (!priorityQueue.isEmpty()) {
                Event event = priorityQueue.minKey();
                priorityQueue.deleteMin();

                if (!event.isValid()) {
                    predictCollisions(event.particleA, limit);
                    predictCollisions(event.particleB, limit);
                    continue;
                }

                // Update particle positions
                for (int i = 0; i < particles.length; i++) {
                    particles[i].move(event.time - time);
                }
                // Update time
                time = event.time;

                ParticleWithId particleA = event.particleA;
                ParticleWithId particleB = event.particleB;

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
        }
    }

    public static void main(String[] args) {
        Exercise12_IndexPriorityQueueImplementation indexPriorityQueueImplementation =
                new Exercise12_IndexPriorityQueueImplementation();

        int numberOfParticles = 20;
        ParticleWithId[] particles = new ParticleWithId[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = indexPriorityQueueImplementation.new ParticleWithId(i);
        }

        CollisionSystemIndexPriorityQueue collisionSystemIndexPriorityQueue =
                indexPriorityQueueImplementation.new CollisionSystemIndexPriorityQueue(particles);
        collisionSystemIndexPriorityQueue.simulate(10000, 0.5);
    }

}

