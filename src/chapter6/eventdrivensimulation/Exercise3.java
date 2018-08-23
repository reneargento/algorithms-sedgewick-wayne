package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdRandom;
import util.StdDraw3D;

// To run this class, make sure to import the following libraries to the project (they are in the libs/java3d folder):
// jogl-all.jar
// gluegen-rt.jar
// j3dcore-jar
// j3dutils.jar
// vecmath.jar

// Native libraries are also needed. If you are using a Mac, import the libraries:
// libgluegen-rt.jnilib
// libjogl_desktop.jnilib
// libnativewindow_awt.jnilib
// libnativewindow_macosx.jnilib
// libnewt.jnilib

// If you are using any other operating system, import the libraries mentioned in
// https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL

// Finally, set the VM Options parameter to point java.library.path to the location of your libraries.
// -Djava.library.path=./libs/java3d

// After running the code, you can click and drag the screen to view the different dimensions.

/**
 * Created by Rene Argento on 28/05/18.
 */
public class Exercise3 {

    public class Particle3D {
        private double positionX;
        private double positionY;
        private double positionZ;

        private double velocityX;
        private double velocityY;
        private double velocityZ;

        private double radius;
        private double mass;

        private int numberOfCollisions;

        Particle3D() {
            positionX = StdRandom.uniform(0.0, 1.0);
            positionY = StdRandom.uniform(0.0, 1.0);
            positionZ = StdRandom.uniform(0.0, 1.0);

            velocityX = StdRandom.uniform(-0.005, 0.005);
            velocityY = StdRandom.uniform(-0.005, 0.005);
            velocityZ = StdRandom.uniform(-0.005, 0.005);

            radius = 0.01;
            mass = 0.5;
        }

        Particle3D(double positionX, double positionY, double positionZ, double velocityX, double velocityY,
                   double velocityZ, double radius, double mass) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.positionZ = positionZ;

            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.velocityZ = velocityZ;

            this.radius = radius;
            this.mass = mass;
        }

        public void draw() {
            StdDraw3D.drawSphere(positionX, positionY, positionZ, radius);
        }

        public void move(double time) {
            positionX = positionX + (velocityX * time);
            positionY = positionY + (velocityY * time);
            positionZ = positionZ + (velocityZ * time);
        }

        public int count() {
            return numberOfCollisions;
        }

        // Based on the explanation at https://algs4.cs.princeton.edu/61event/
        public double timeToHit(Particle3D otherParticle) {

            if (this == otherParticle) {
                return Double.POSITIVE_INFINITY;
            }

            double deltaPositionX = otherParticle.positionX - positionX;
            double deltaPositionY = otherParticle.positionY - positionY;
            double deltaPositionZ = otherParticle.positionZ - positionZ;

            double deltaVelocityX = otherParticle.velocityX - velocityX;
            double deltaVelocityY = otherParticle.velocityY - velocityY;
            double deltaVelocityZ = otherParticle.velocityZ - velocityZ;

            double deltaPositionByDeltaVelocity = deltaPositionX * deltaVelocityX + deltaPositionY * deltaVelocityY
                    + deltaPositionZ * deltaVelocityZ;
            if (deltaPositionByDeltaVelocity > 0) {
                return Double.POSITIVE_INFINITY;
            }

            double deltaVelocitySquared = deltaVelocityX * deltaVelocityX + deltaVelocityY * deltaVelocityY
                    + deltaVelocityZ * deltaVelocityZ;
            double deltaPositionSquared = deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY
                    + deltaPositionZ * deltaPositionZ;

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

        public double timeToHitThirdDimensionWall() {
            double distance;

            if (velocityZ < 0) {
                // Get the distance as a negative number
                distance = radius - positionZ;
            } else if (velocityZ > 0) {
                distance = 1 - radius - positionZ;
            } else {
                return Double.POSITIVE_INFINITY;
            }

            return distance / velocityZ;
        }

        // Based on the explanation at https://algs4.cs.princeton.edu/61event/
        public void bounceOff(Particle3D otherParticle) {
            double deltaPositionX = otherParticle.positionX - positionX;
            double deltaPositionY = otherParticle.positionY - positionY;
            double deltaPositionZ = otherParticle.positionZ - positionZ;

            double deltaVelocityX = otherParticle.velocityX - velocityX;
            double deltaVelocityY = otherParticle.velocityY - velocityY;
            double deltaVelocityZ = otherParticle.velocityZ - velocityZ;

            double deltaPositionByDeltaVelocity = deltaPositionX * deltaVelocityX + deltaPositionY * deltaVelocityY
                    + deltaPositionZ * deltaVelocityZ;

            double distanceBetweenCenters = radius + otherParticle.radius;

            // Compute normal force
            double magnitudeOfNormalForce = 2 * mass * otherParticle.mass * deltaPositionByDeltaVelocity
                    / ((mass + otherParticle.mass) * distanceBetweenCenters);

            double normalForceInXDirection = magnitudeOfNormalForce * deltaPositionX / distanceBetweenCenters;
            double normalForceInYDirection = magnitudeOfNormalForce * deltaPositionY / distanceBetweenCenters;
            double normalForceInZDirection = magnitudeOfNormalForce * deltaPositionZ / distanceBetweenCenters;

            // Update velocities according to the normal force
            velocityX += normalForceInXDirection / mass;
            velocityY += normalForceInYDirection / mass;
            velocityZ += normalForceInZDirection / mass;
            otherParticle.velocityX -= normalForceInXDirection / otherParticle.mass;
            otherParticle.velocityY -= normalForceInYDirection / otherParticle.mass;
            otherParticle.velocityZ -= normalForceInZDirection / otherParticle.mass;

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

        public void bounceOffThirdDimensionWall() {
            velocityZ = -velocityZ;
            numberOfCollisions++;
        }

        // Kinetic energy = 1/2 * mass * velocity^2
        public double kineticEnergy() {
            return 0.5 * mass * (velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        }
    }

    private enum EventType {
        PARTICLE_COLLISION, HORIZONTAL_WALL_COLLISION, VERTICAL_WALL_COLLISION, THIRD_DIMENSION_WALL_COLLISION, DRAW
    }

    public class CollisionSystem3D {

        private class Event implements Comparable<Event> {

            private final double time;
            private final Particle3D particleA;
            private final Particle3D particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;
            private final EventType eventType;

            public Event(double time, Particle3D particleA, Particle3D particleB, EventType eventType) {
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
        private Particle3D[] particles;

        public CollisionSystem3D(Particle3D[] particles) {
            this.particles = particles;
        }

        private void predictCollisions(Particle3D particle3D, double limit) {
            if (particle3D == null) {
                return;
            }

            for (int i = 0; i < particles.length; i++) {
                double deltaTime = particle3D.timeToHit(particles[i]);

                if (time + deltaTime <= limit) {
                    priorityQueue.insert(new Event(time + deltaTime, particle3D, particles[i],
                            EventType.PARTICLE_COLLISION));
                }
            }

            double deltaTimeVerticalWall = particle3D.timeToHitVerticalWall();
            if (time + deltaTimeVerticalWall <= limit) {
                priorityQueue.insert(new Event(time + deltaTimeVerticalWall, particle3D, null,
                        EventType.VERTICAL_WALL_COLLISION));
            }

            double deltaTimeHorizontalWall = particle3D.timeToHitHorizontalWall();
            if (time + deltaTimeHorizontalWall <= limit) {
                priorityQueue.insert(new Event(time + deltaTimeHorizontalWall, particle3D, null,
                        EventType.HORIZONTAL_WALL_COLLISION));
            }

            double deltaTimeThirdDimensionWall = particle3D.timeToHitThirdDimensionWall();
            if (time + deltaTimeThirdDimensionWall <= limit) {
                priorityQueue.insert(new Event(time + deltaTimeThirdDimensionWall, particle3D, null,
                        EventType.THIRD_DIMENSION_WALL_COLLISION));
            }
        }

        public void redraw(double limit, double hertz) {
            StdDraw3D.clear();

            for (int i = 0; i < particles.length; i++) {
                particles[i].draw();
            }

            StdDraw3D.pause(20);
            StdDraw3D.show();

            if (time < limit) {
                priorityQueue.insert(new Event(time + 1.0 / hertz, null, null, EventType.DRAW));
            }
        }

        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);
            // Using a range of 0.15 to 0.85 to better visualize all 3 dimensions
            StdDraw3D.setScale(0.15, 0.85);

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(new Event(0, null, null, EventType.DRAW)); // Add redraw event

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

                Particle3D particleA = event.particleA;
                Particle3D particleB = event.particleB;

                switch (event.eventType) {
                    case PARTICLE_COLLISION:
                        particleA.bounceOff(particleB);
                        break;
                    case HORIZONTAL_WALL_COLLISION:
                        particleA.bounceOffHorizontalWall();
                        break;
                    case VERTICAL_WALL_COLLISION:
                        particleA.bounceOffVerticalWall();
                        break;
                    case THIRD_DIMENSION_WALL_COLLISION:
                        particleA.bounceOffThirdDimensionWall();
                        break;
                    case DRAW:
                        redraw(limit, hertz);
                        break;
                }

                predictCollisions(particleA, limit);
                predictCollisions(particleB, limit);
            }
        }
    }

    public static void main(String[] args) {
        Exercise3 exercise3 = new Exercise3();

        int numberOfParticles = 10;
        Particle3D[] particles = new Particle3D[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = exercise3.new Particle3D();
        }

        CollisionSystem3D collisionSystem3D = exercise3.new CollisionSystem3D(particles);
        collisionSystem3D.simulate(10000, 0.5);
    }

}
