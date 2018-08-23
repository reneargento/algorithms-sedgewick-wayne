package chapter6.eventdrivensimulation;

import chapter2.section4.IndexMinPriorityQueue;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 12/07/18.
 */
public class Exercise13_PriorityQueuePerformance {

    private final double BOLTZMANN_CONSTANT = 1.3806488e-23;

    public class ParticleWithPressureAndId extends Particle {

        private static final int DIMENSION = 2;

        private int id;
        private double pressure;

        ParticleWithPressureAndId(int id, double positionX, double positionY, double velocityX, double velocityY,
                                  double radius, double mass) {
            super(positionX, positionY, velocityX, velocityY, radius, mass);
            this.id = id;
        }

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

        public void setVelocityX(double velocityX) {
            this.velocityX = velocityX;
        }

        public void setVelocityY(double velocityY) {
            this.velocityY = velocityY;
        }
    }

    public class CollisionSystemWithPressure {

        private class Event implements Comparable<Event> {

            private final double time;
            private final ParticleWithPressureAndId particleA;
            private final ParticleWithPressureAndId particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;

            public Event(double time, ParticleWithPressureAndId particleA, ParticleWithPressureAndId particleB) {
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
        private IndexMinPriorityQueue<Event> indexMinPriorityQueue;
        private double time;

        private ParticleWithPressureAndId[] particles;
        private final int DRAW_EVENT_ID;

        private int numberOfParticles;
        private boolean useIndexPriorityQueue;

        private final double IDEAL_GAS_CONSTANT = 0.082057;
        private final double AVOGADROS_NUMBER = 6.022e23;

        private double totalTimeSpentOnInsert = 0;
        private double totalTimeSpentOnDeleteMin = 0;
        private double totalTimeSpentOnIsEmpty = 0;

        // Index min priority queue specific metrics
        private double totalTimeSpentOnContains = 0;
        private double totalTimeSpentOnMin = 0;
        private double totalTimeSpentOnDelete = 0;

        public CollisionSystemWithPressure(ParticleWithPressureAndId[] particles, boolean useIndexPriorityQueue) {
            StdDraw.enableDoubleBuffering();
            this.particles = particles;
            numberOfParticles = particles.length;

            DRAW_EVENT_ID = particles.length;
            this.useIndexPriorityQueue = useIndexPriorityQueue;
        }

        private void predictCollisions(ParticleWithPressureAndId particle, double limit) {
            if (particle == null) {
                return;
            }

            int particleId = particle.id;
            double currentSmallestEventTime = Double.POSITIVE_INFINITY;

            if (useIndexPriorityQueue) {
                Stopwatch timer = new Stopwatch();
                boolean containsParticleId = indexMinPriorityQueue.contains(particleId);
                totalTimeSpentOnContains += timer.elapsedTime();

                if (containsParticleId) {
                    timer = new Stopwatch();
                    indexMinPriorityQueue.delete(particleId);
                    totalTimeSpentOnDelete += timer.elapsedTime();
                }
            }

            for (int i = 0; i < particles.length; i++) {
                double deltaTime = particle.timeToHit(particles[i]);
                double eventTime = time + deltaTime;

                if (eventTime <= limit
                        && (!useIndexPriorityQueue || eventTime <= currentSmallestEventTime)) {
                    if (!useIndexPriorityQueue) {
                        Stopwatch timer = new Stopwatch();
                        priorityQueue.insert(new Event(eventTime, particle, particles[i]));
                        totalTimeSpentOnInsert += timer.elapsedTime();
                    } else {
                        Stopwatch timer = new Stopwatch();
                        boolean containsParticleId = indexMinPriorityQueue.contains(particleId);
                        totalTimeSpentOnContains += timer.elapsedTime();

                        if (containsParticleId) {
                            timer = new Stopwatch();
                            indexMinPriorityQueue.delete(particleId);
                            totalTimeSpentOnDelete += timer.elapsedTime();
                        }

                        timer = new Stopwatch();
                        indexMinPriorityQueue.insert(particleId, new Event(eventTime, particle, particles[i]));
                        totalTimeSpentOnInsert += timer.elapsedTime();

                        currentSmallestEventTime = eventTime;
                    }
                }
            }

            double deltaTimeVerticalWall = particle.timeToHitVerticalWall();
            double verticalWallEventTime = time + deltaTimeVerticalWall;

            if (verticalWallEventTime <= limit
                    && (!useIndexPriorityQueue || verticalWallEventTime <= currentSmallestEventTime)) {
                if (!useIndexPriorityQueue) {
                    Stopwatch timer = new Stopwatch();
                    priorityQueue.insert(new Event(verticalWallEventTime, particle, null));
                    totalTimeSpentOnInsert += timer.elapsedTime();
                } else {
                    Stopwatch timer = new Stopwatch();
                    boolean containsParticleId = indexMinPriorityQueue.contains(particleId);
                    totalTimeSpentOnContains += timer.elapsedTime();

                    if (containsParticleId) {
                        timer = new Stopwatch();
                        indexMinPriorityQueue.delete(particleId);
                        totalTimeSpentOnDelete += timer.elapsedTime();
                    }

                    timer = new Stopwatch();
                    indexMinPriorityQueue.insert(particleId, new Event(verticalWallEventTime, particle, null));
                    totalTimeSpentOnInsert += timer.elapsedTime();

                    currentSmallestEventTime = verticalWallEventTime;
                }
            }

            double deltaTimeHorizontalWall = particle.timeToHitHorizontalWall();
            double horizontalWallEventTime = time + deltaTimeHorizontalWall;

            if (horizontalWallEventTime <= limit
                    && (!useIndexPriorityQueue || horizontalWallEventTime <= currentSmallestEventTime)) {
                if (!useIndexPriorityQueue) {
                    Stopwatch timer = new Stopwatch();
                    priorityQueue.insert(new Event(horizontalWallEventTime, null, particle));
                    totalTimeSpentOnInsert += timer.elapsedTime();
                } else {
                    Stopwatch timer = new Stopwatch();
                    boolean containsParticleId = indexMinPriorityQueue.contains(particleId);
                    totalTimeSpentOnContains += timer.elapsedTime();

                    if (containsParticleId) {
                        timer = new Stopwatch();
                        indexMinPriorityQueue.delete(particleId);
                        totalTimeSpentOnDelete += timer.elapsedTime();
                    }

                    timer = new Stopwatch();
                    indexMinPriorityQueue.insert(particleId, new Event(horizontalWallEventTime, null, particle));
                    totalTimeSpentOnInsert += timer.elapsedTime();
                }
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
                if (!useIndexPriorityQueue) {
                    Stopwatch timer = new Stopwatch();
                    priorityQueue.insert(new Event(time + 1.0 / hertz, null, null));
                    totalTimeSpentOnInsert += timer.elapsedTime();
                } else {
                    Stopwatch timer = new Stopwatch();
                    indexMinPriorityQueue.insert(DRAW_EVENT_ID, new Event(time + 1.0 / hertz, null, null));
                    totalTimeSpentOnInsert += timer.elapsedTime();
                }
            }
        }

        public void simulate(double limit, double hertz) {
            if (!useIndexPriorityQueue) {
                priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

                Stopwatch timer = new Stopwatch();
                priorityQueue.insert(new Event(0, null, null));
                totalTimeSpentOnInsert += timer.elapsedTime();
            } else {
                // Add 1 extra space for the draw event
                indexMinPriorityQueue = new IndexMinPriorityQueue<>(particles.length + 1);

                Stopwatch timer = new Stopwatch();
                indexMinPriorityQueue.insert(DRAW_EVENT_ID, new Event(0, null, null));
                totalTimeSpentOnInsert += timer.elapsedTime();
            }

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }

            StdOut.println("Testing with temperature: " + temperature());

            int nextCheckpoint = 1000;

            while (!isPriorityQueueEmpty()) {
                Event event;

                if (!useIndexPriorityQueue) {
                    Stopwatch timer = new Stopwatch();
                    event = priorityQueue.deleteTop();
                    totalTimeSpentOnDeleteMin += timer.elapsedTime();
                } else {
                    Stopwatch timer = new Stopwatch();
                    event = indexMinPriorityQueue.minKey();
                    totalTimeSpentOnMin += timer.elapsedTime();

                    timer = new Stopwatch();
                    indexMinPriorityQueue.deleteMin();
                    totalTimeSpentOnDeleteMin += timer.elapsedTime();
                }

                // ValidateEquation method is only used to simulate a Pressure test, but its results are not used
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

                ParticleWithPressureAndId particleA = event.particleA;
                ParticleWithPressureAndId particleB = event.particleB;

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

            identifyBottleneck();
        }

        private boolean isPriorityQueueEmpty() {
            boolean isEmpty;

            if (!useIndexPriorityQueue) {
                Stopwatch timer = new Stopwatch();
                isEmpty = priorityQueue.isEmpty();
                totalTimeSpentOnIsEmpty += timer.elapsedTime();
            } else {
                Stopwatch timer = new Stopwatch();
                isEmpty = indexMinPriorityQueue.isEmpty();
                totalTimeSpentOnIsEmpty += timer.elapsedTime();
            }

            return isEmpty;
        }

        private void validateEquation(double time) {
            double pressureMeasuredWithEquation = pressure();
            double pressureMeasuredWithWallCollisions = pressureWithWallCollisions(time);
            boolean unusedTest = pressureMeasuredWithEquation == pressureMeasuredWithWallCollisions;
        }

        // Ideal gas formula: P V = n R T
        // P = pressure
        // V = volume
        // n = number of moles
        // R = ideal gas constant
        // T = temperature
        //
        // P V = n R T
        // P = (n R T) / V
        public double pressure() {
            return (numberOfMoles() * IDEAL_GAS_CONSTANT * temperature()) / volume();
        }

        public double pressureWithWallCollisions(double time) {
            double systemPressure = 0;

            for(ParticleWithPressureAndId particle : particles) {
                systemPressure += particle.pressure();
            }

            return systemPressure / time;
        }

        public double volume() {
            double systemVolume = 0;

            for(ParticleWithPressureAndId particle : particles) {
                systemVolume += particle.volume();
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
            double particleMass = particles[0].mass;
            double totalMass = particleMass * particles.length;

            double molecularWeight = particleMass * AVOGADROS_NUMBER;
            return totalMass / molecularWeight;
        }

        public double temperature() {
            double totalTemperature = 0;

            for(ParticleWithPressureAndId particle : particles) {
                totalTemperature += particle.temperature();
            }

            return totalTemperature / numberOfParticles;
        }

        // V = SQRT(2 kb T / M)
        // SQRT(V / 2) = (vx + vy) / 2
        private void setTemperature(double temperature) {

            for (ParticleWithPressureAndId particle : particles) {
                double newVelocityMagnitude =
                        Math.sqrt(2 * BOLTZMANN_CONSTANT * temperature / particle.getMass());
                double velocityComponent = Math.sqrt(newVelocityMagnitude / 2);
                particle.setVelocityX(velocityComponent);
                particle.setVelocityY(velocityComponent);
            }
        }

        private void identifyBottleneck() {
            double highestTimeSpent = 0;
            String computationalBottleneck = "";

            StdOut.printf("Total time spent on insert operations: %.5f\n", totalTimeSpentOnInsert);
            if (totalTimeSpentOnInsert > highestTimeSpent) {
                highestTimeSpent = totalTimeSpentOnInsert;
                computationalBottleneck = "insert";
            }

            StdOut.printf("Total time spent on deleteMin operations: %.5f\n", totalTimeSpentOnDeleteMin);
            if (totalTimeSpentOnDeleteMin > highestTimeSpent) {
                highestTimeSpent = totalTimeSpentOnDeleteMin;
                computationalBottleneck = "deleteMin";
            }

            StdOut.printf("Total time spent on isEmpty operations: %.5f\n", totalTimeSpentOnIsEmpty);
            if (totalTimeSpentOnIsEmpty > highestTimeSpent) {
                highestTimeSpent = totalTimeSpentOnIsEmpty;
                computationalBottleneck = "isEmpty";
            }

            if (useIndexPriorityQueue) {
                StdOut.printf("Total time spent on contains operations: %.5f\n", totalTimeSpentOnContains);
                if (totalTimeSpentOnContains > highestTimeSpent) {
                    highestTimeSpent = totalTimeSpentOnContains;
                    computationalBottleneck = "contains";
                }

                StdOut.printf("Total time spent on min operations: %.5f\n", totalTimeSpentOnMin);
                if (totalTimeSpentOnMin > highestTimeSpent) {
                    highestTimeSpent = totalTimeSpentOnMin;
                    computationalBottleneck = "min";
                }

                StdOut.printf("Total time spent on delete operations: %.5f\n", totalTimeSpentOnDelete);
                if (totalTimeSpentOnDelete > highestTimeSpent) {
                    computationalBottleneck = "delete";
                }
            }

            StdOut.println("Computational bottleneck: " + computationalBottleneck + " operations");
        }
    }

    private ParticleWithPressureAndId getRandomParticle(int id) {
        double positionX = StdRandom.uniform(0.0, 1.0);
        double positionY = StdRandom.uniform(0.0, 1.0);

        double velocityX = StdRandom.uniform(-0.005, 0.005);
        double velocityY = StdRandom.uniform(-0.005, 0.005);

        double radius = 0.0025;
        double mass = 0.5;

        return new ParticleWithPressureAndId(id, positionX, positionY, velocityX, velocityY, radius, mass);
    }

    private void doExperiment(int numberOfParticles, int simulationTime, double hertz) {
        StdOut.println("**** Standard priority queue tests ****");
        StdOut.println();

        double baseLineTemperature = doBaselineTest(numberOfParticles, simulationTime, hertz);

        double[] testTemperatures = {
                baseLineTemperature * 100,
                baseLineTemperature * 10000,
                baseLineTemperature * 1000000,
                baseLineTemperature * 100000000
        };

        for (int t = 0; t < testTemperatures.length; t++) {
            simulate(numberOfParticles, simulationTime, hertz, testTemperatures[t], false);
        }

        StdOut.println();
        StdOut.println("**** Index priority queue tests ****");

        // Also test the high temperatures with the index priority-queue
        for (int t = 2; t < testTemperatures.length; t++) {
            simulate(numberOfParticles, simulationTime, hertz, testTemperatures[t], true);
        }
    }

    private double doBaselineTest(int numberOfParticles, int simulationTime, double hertz) {
        ParticleWithPressureAndId[] particles = new ParticleWithPressureAndId[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = getRandomParticle(i);
        }

        CollisionSystemWithPressure collisionSystem = new CollisionSystemWithPressure(particles, false);
        collisionSystem.simulate(simulationTime, hertz);

        return collisionSystem.temperature();
    }

    private void simulate(int numberOfParticles, int simulationTime, double hertz, double testTemperature,
                          boolean useIndexPriorityQueue) {
        StdDraw.clear();
        StdOut.println();

        ParticleWithPressureAndId[] particles = new ParticleWithPressureAndId[numberOfParticles];
        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = getRandomParticle(i);
        }

        CollisionSystemWithPressure collisionSystemWithPressure =
                new CollisionSystemWithPressure(particles, useIndexPriorityQueue);
        collisionSystemWithPressure.setTemperature(testTemperature);

        collisionSystemWithPressure.simulate(simulationTime, hertz);
    }

    public static void main(String[] args) {
        int numberOfParticles = 30;
        int simulationTime = 10000;
        double hertz = 0.5;

        new Exercise13_PriorityQueuePerformance().doExperiment(numberOfParticles, simulationTime, hertz);
    }

}