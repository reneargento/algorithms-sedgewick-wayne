package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import util.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 28/04/18.
 */
// Based on https://physics.stackexchange.com/questions/296767/multiple-colliding-balls
// and https://physics.stackexchange.com/questions/173596/is-this-solveable-simultaneous-elastic-collision-of-4-objects-in-xy-plane
public class Exercise2 {

    public class Collision {
        private ParticleMultiCollisions particle1;
        private ParticleMultiCollisions particle2;

        Collision(ParticleMultiCollisions particle1, ParticleMultiCollisions particle2) {
            this.particle1 = particle1;
            this.particle2 = particle2;
        }
    }

    public class ParticleMultiCollisions {

        private int id;

        private double positionX;
        private double positionY;

        private double velocityX;
        private double velocityY;

        private double radius;
        private double mass;

        private int numberOfCollisions;

        ParticleMultiCollisions(int id) {
            StdDraw.enableDoubleBuffering();

            this.id = id;
            positionX = StdRandom.uniform(0.0, 1.0);
            positionY = StdRandom.uniform(0.0, 1.0);

            velocityX = StdRandom.uniform(-0.005, 0.005);
            velocityY = StdRandom.uniform(-0.005, 0.005);

            radius = 0.01;
            mass = 0.5;
        }

        ParticleMultiCollisions(int id, double positionX, double positionY, double velocityX, double velocityY,
                                double radius, double mass) {
            StdDraw.enableDoubleBuffering();

            this.id = id;
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
        public double timeToHit(ParticleMultiCollisions otherParticle) {

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

        public void bounceOffHorizontalWall(double[][] velocities) {
            velocityY = -velocityY;

            int velocityIdY = id * 2 + 1;
            velocities[velocityIdY][0] = -velocities[velocityIdY][0];

            numberOfCollisions++;
        }

        public void bounceOffVerticalWall(double[][] velocities) {
            velocityX = -velocityX;

            int velocityIdX = id * 2;
            velocities[velocityIdX][0] = -velocities[velocityIdX][0];

            numberOfCollisions++;
        }

        // Kinetic energy = 1/2 * mass * velocity^2
        public double kineticEnergy() {
            return 0.5 * mass * (velocityX * velocityX + velocityY * velocityY);
        }
    }

    public class CollisionSystem {

        private class Event implements Comparable<Event> {

            private final double time;
            private final ParticleMultiCollisions particleA;
            private final ParticleMultiCollisions particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;

            public Event(double time, ParticleMultiCollisions particleA, ParticleMultiCollisions particleB) {
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
        private ParticleMultiCollisions[] particles;
        private SeparateChainingHashTable<Integer, ParticleMultiCollisions> particlesById;

        private double[][] velocities;
        private double[][] massesMatrixInverse;

        public CollisionSystem(ParticleMultiCollisions[] particles) {
            this.particles = particles;
            particlesById = new SeparateChainingHashTable<>();

            // Create a vector with all velocities and a matrix with all masses to compute multiple-particle collisions later
            velocities = new double[particles.length * 2][1];
            double[][] masses = new double[particles.length * 2][particles.length * 2];

            for (int particle = 0; particle < particles.length; particle++) {
                int particleId = particles[particle].id;

                int index1 = particleId * 2;
                int index2 = index1 + 1;

                // Compute velocities
                velocities[index1][0] = particles[particle].velocityX;
                velocities[index2][0] = particles[particle].velocityY;

                // Compute masses
                masses[index1][index1] = particles[particle].mass;
                masses[index2][index2] = particles[particle].mass;

                // Add quick access to particle by its id
                if (particlesById.contains(particleId)) {
                    throw new IllegalArgumentException("Particle IDs should be unique");
                }
                particlesById.put(particleId, particles[particle]);
            }

            massesMatrixInverse = MatrixUtil.inverse(masses);
        }

        private void predictCollisions(ParticleMultiCollisions particle, double limit) {
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

        private void solveCollisionBetweenParticles(List<Collision> collisions) {

            // 1- Create a matrix of impacts with the normal contact vectors.
            // It uses 2 lines per particle (one for the x dimension contact and another for the y dimension contact)
            // and one column per collision.
            double[][] normalContactVectors = new double[particles.length * 2][collisions.size()];

            int collisionId = 0;

            for (Collision collision : collisions) {
                ParticleMultiCollisions particle1 = collision.particle1;
                ParticleMultiCollisions particle2 = collision.particle2;

                double deltaPositionX = particle2.positionX - particle1.positionX;
                double deltaPositionY = particle2.positionY - particle1.positionY;

                double norm = Math.sqrt(deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY);

                int particle1IdX = particle1.id * 2;
                int particle1IdY = particle1IdX + 1;
                int particle2IdX = particle2.id * 2;
                int particle2IdY = particle2IdX + 1;

                normalContactVectors[particle1IdX][collisionId] = -(deltaPositionX / norm);
                normalContactVectors[particle1IdY][collisionId] = -(deltaPositionY / norm);

                normalContactVectors[particle2IdX][collisionId] = deltaPositionX / norm;
                normalContactVectors[particle2IdY][collisionId] = deltaPositionY / norm;

                collisionId++;
            }

            // 2- Calculate the relative speed of each contact pair
            double[][] normalContactVectorsTransposed = MatrixUtil.transpose(normalContactVectors);
            double[][] relativeSpeed = MatrixUtil.multiply(normalContactVectorsTransposed, velocities);

            // 3- Compute the impulses
            double[][] normalContactVectorsByMassesInverse =
                    MatrixUtil.multiplyByConstant(
                            MatrixUtil.inverse(
                                    MatrixUtil.multiply(
                                            MatrixUtil.multiply(normalContactVectorsTransposed, massesMatrixInverse),
                                            normalContactVectors)), -1);

            double[][] impulses = MatrixUtil.multiply(
                    MatrixUtil.multiplyByConstant(normalContactVectorsByMassesInverse, 2),
                    relativeSpeed);

            // 4- Compute the changes in velocity
            double[][] changesInVelocity = MatrixUtil.multiply(
                    MatrixUtil.multiply(massesMatrixInverse, normalContactVectors),
                    impulses);

            // 5- Update velocities
            for (int particle = 0; particle < particles.length; particle++) {
                int particleIdX = particle * 2;
                int particleIdY = particleIdX + 1;

                ParticleMultiCollisions currentParticle = particlesById.get(particle);
                currentParticle.velocityX += changesInVelocity[particleIdX][0];
                currentParticle.velocityY += changesInVelocity[particleIdY][0];

                velocities[particleIdX][0] += changesInVelocity[particleIdX][0];
                velocities[particleIdY][0] += changesInVelocity[particleIdY][0];
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

                List<Collision> collisions = new ArrayList<>();
                SeparateChainingHashTable<Integer, HashSet<Integer>> pairsColliding = new SeparateChainingHashTable<>();
                HashSet<Integer> uniqueParticlesColliding = new HashSet<>();

                while (true) {
                    if (!event.isValid()) {
                        if (!priorityQueue.isEmpty() && priorityQueue.peek().time == time) {
                            event = priorityQueue.deleteTop();
                            continue;
                        } else {
                            break;
                        }
                    }

                    ParticleMultiCollisions particleA = event.particleA;
                    ParticleMultiCollisions particleB = event.particleB;

                    if (particleA != null && particleB != null) {
                        // If the collision of particle A -> particle B is already mapped, the collision
                        // particle B -> particle A will also be handled. So there is no need to handle it twice.
                        if (pairsColliding.contains(particleB.id)
                                && pairsColliding.get(particleB.id).contains(particleA.id)) {

                            if (!priorityQueue.isEmpty() && priorityQueue.peek().time == time) {
                                event = priorityQueue.deleteTop();
                                continue;
                            } else {
                                break;
                            }
                        }

                        if (!pairsColliding.contains(particleA.id)) {
                            pairsColliding.put(particleA.id, new HashSet<>());
                        }
                        pairsColliding.get(particleA.id).add(particleB.id);

                        collisions.add(new Collision(particleA, particleB));
                        uniqueParticlesColliding.add(particleA.id);
                        uniqueParticlesColliding.add(particleB.id);
                    } else if (particleA != null && particleB == null) {
                        particleA.bounceOffVerticalWall(velocities);
                        predictCollisions(particleA, limit);
                    } else if (particleA == null && particleB != null) {
                        particleB.bounceOffHorizontalWall(velocities);
                        predictCollisions(particleB, limit);
                    } else if (particleA == null && particleB == null) {
                        redraw(limit, hertz);
                    }

                    if (!priorityQueue.isEmpty() && priorityQueue.peek().time == time) {
                        event = priorityQueue.deleteTop();
                    } else {
                        break;
                    }
                }

                if (!collisions.isEmpty()) {
                    solveCollisionBetweenParticles(collisions);

                    for (Integer particleId : uniqueParticlesColliding.keys()) {
                        particlesById.get(particleId).numberOfCollisions++;
                    }

                    for (Integer particleId : uniqueParticlesColliding.keys()) {
                        predictCollisions(particlesById.get(particleId), limit);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Exercise2 exercise2 = new Exercise2();

        // Game of billiards simulation
        ParticleMultiCollisions[] particles = new ParticleMultiCollisions[4];
        particles[0] = exercise2.new ParticleMultiCollisions(0, 0.47, 0.8,
                0, 0, 0.029, 1);
        particles[1] = exercise2.new ParticleMultiCollisions(1, 0.531, 0.8,
                0, 0, 0.029, 1);
        particles[2] = exercise2.new ParticleMultiCollisions(2, 0.5, 0.7480384757729337,
                0, 0, 0.029, 1);
        particles[3] = exercise2.new ParticleMultiCollisions(3, 0.5, 0.1,
                0, 0.02, 0.029, 1);

        CollisionSystem collisionSystem = exercise2.new CollisionSystem(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}
