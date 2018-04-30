package chapter6;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento on 27/04/18.
 */
public class CollisionSystem {

    private class Event implements Comparable<Event> {

        private final double time;
        private final ParticleInterface particleA;
        private final ParticleInterface particleB;
        private final int collisionsCountA;
        private final int collisionsCountB;

        public Event(double time, ParticleInterface particleA, ParticleInterface particleB) {
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

    private PriorityQueueResize<Event> priorityQueue;
    private double time;
    private ParticleInterface[] particles;

    public CollisionSystem(ParticleInterface[] particles) {
        this.particles = particles;
    }

    private void predictCollisions(ParticleInterface particle, double limit) {
        if (particle == null) {
            return;
        }

        for (int i = 0; i < particles.length; i++) {
            double deltaTime = particle.timeToHit(particles[i]);

            if (time + deltaTime <= limit) {
                priorityQueue.insert(new Event(time + deltaTime, particle, particles[i]));
            }
        }

        double deltaTimeVerticalWall = particle.timeToHitVerticalWall();
        if (time + deltaTimeVerticalWall <= limit) {
            priorityQueue.insert(new Event(time + deltaTimeVerticalWall, particle, null));
        }

        double deltaTimeHorizontalWall = particle.timeToHitHorizontalWall();
        if (time + deltaTimeHorizontalWall <= limit) {
            priorityQueue.insert(new Event(time + deltaTimeHorizontalWall, null, particle));
        }
    }

    public void redraw(double limit, double hertz) {
        StdDraw.clear();

        for (int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }

        StdDraw.show(20);

        if (time < limit) {
            priorityQueue.insert(new Event(time + 1.0 / hertz, null, null));
        }
    }

    public void simulate(double limit, double hertz) {
        priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

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
        }
    }

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();

        int numberOfParticles = Integer.parseInt(args[0]);
        Particle[] particles = new Particle[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = new Particle();
        }

        CollisionSystem collisionSystem = new CollisionSystem(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}
