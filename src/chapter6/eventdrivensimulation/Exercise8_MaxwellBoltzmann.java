package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

/**
 * Created by Rene Argento on 27/06/18.
 */
public class Exercise8_MaxwellBoltzmann {

    private final double BOLTZMANN_CONSTANT = 1.3806488e-23;
    private final int SHOW_HISTOGRAM_DURATION = 5000;

    public class ParticleWithTemperature extends Particle {

        ParticleWithTemperature(double positionX, double positionY, double velocityX, double velocityY, double radius,
                                double mass) {
            super(positionX, positionY, velocityX, velocityY, radius, mass);
        }

        private final int DIMENSION = 2;

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

    public class CollisionSystemWithTemperature extends CollisionSystem {

        private int numberOfParticles;

        public CollisionSystemWithTemperature(ParticleWithTemperature[] particles) {
            super(particles);
            numberOfParticles = particles.length;
        }

        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            for (int i = 0; i < particles.length; i++) {
                predictCollisions(particles[i], limit);
            }
            priorityQueue.insert(new Event(0, null, null)); // Add redraw event

            StdOut.println("Testing and showing velocities histogram for temperature: " + temperature());

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

            computeVelocitiesHistogram();
        }

        private void computeVelocitiesHistogram() {
            int buckets = 20;
            double[] histogram = new double[buckets];
            double maxValue = 0;
            double rangeOfVelocitiesPerBucket = 0.000005;

            for (ParticleInterface particle : particles) {
                ParticleWithTemperature particleWithTemperature = (ParticleWithTemperature) particle;
                double velocityMagnitude = Math.pow(particleWithTemperature.velocityX, 2)
                        + Math.pow(particleWithTemperature.velocityY, 2);

                int bucketId = (int) Math.floor(velocityMagnitude / rangeOfVelocitiesPerBucket);
                bucketId = Math.min(bucketId, histogram.length - 1);
                histogram[bucketId]++;

                if (histogram[bucketId] > maxValue) {
                    maxValue = histogram[bucketId];
                }
            }

            String[] velocityDescriptions = getVelocityDescriptions(buckets, rangeOfVelocitiesPerBucket);
            drawHistogram(histogram, maxValue, velocityDescriptions);
        }

        private String[] getVelocityDescriptions(int buckets, double rangeOfVelocitiesPerBucket) {
            String[] velocityDescriptions = new String[buckets];

            for (int i = 0; i < buckets; i++) {
                double minVelocity = rangeOfVelocitiesPerBucket * i;
                double maxVelocity;

                if (i != buckets - 1) {
                    maxVelocity = minVelocity + rangeOfVelocitiesPerBucket - 0.0000001;
                } else {
                    maxVelocity = 1;
                }

                velocityDescriptions[i] = String.format("[%.7f -%.7f] ", minVelocity, maxVelocity);
            }

            return velocityDescriptions;
        }

        private void drawHistogram(double[] histogram, double maxValue, String[] velocityDescriptions) {
            double leftMargin = 0.4;
            double maxX = histogram.length + 1;
            double maxY = maxValue + 1;
            int lineBreakIndex = 12;
            String fontName = "Serif";

            StdDraw.setCanvasSize(1300, 512);
            StdDraw.disableDoubleBuffering();
            StdDraw.setXscale(-0.5, maxX);
            StdDraw.setYscale(-1.5, maxY);

            // Labels
            StdDraw.text(histogram.length / 2, maxValue + 0.5, "Particles vs Velocities");
            StdDraw.text(leftMargin - 0.4, maxY / 2, "Particles", 90);
            StdDraw.text(histogram.length / 2, -1.2, "Velocities");

            Font particlesFont = new Font(fontName, Font.PLAIN, 14);
            StdDraw.setFont(particlesFont);

            for(int y = 0; y < maxY; y++) {
                StdDraw.text(leftMargin, y, String.valueOf(y));
            }

            Font velocitiesFont = new Font(fontName, Font.PLAIN, 10);
            StdDraw.setFont(velocitiesFont);

            for(int x = 0; x < histogram.length; x++) {
                StdDraw.text(x + 1, -0.25, velocityDescriptions[x].substring(0, lineBreakIndex));
                StdDraw.text(x + 1, -0.5, velocityDescriptions[x].substring(lineBreakIndex));
            }

            plotBars(histogram);

            try {
                Thread.sleep(SHOW_HISTOGRAM_DURATION);
            } catch (InterruptedException exception) {
                // No need to take any action if sleep is interrupted
            }
        }

        private void plotBars(double[] values) {
            for (int i = 0; i < values.length; i++) {
                StdDraw.filledRectangle(i + 1, values[i] / 2, 0.3, values[i] / 2);
            }
        }

        public double temperature() {
            double totalTemperature = 0;

            for(ParticleInterface particle : particles) {
                ParticleWithTemperature particleWithTemperature = (ParticleWithTemperature) particle;
                totalTemperature += particleWithTemperature.temperature();
            }

            return totalTemperature / numberOfParticles;
        }

        // V = SQRT(2 kb T / M)
        // SQRT(V / 2) = (vx + vy) / 2
        private void setTemperature(double temperature) {

            for (ParticleInterface particle : particles) {
                ParticleWithTemperature particleWithTemperature = (ParticleWithTemperature) particle;

                double newVelocityMagnitude =
                        Math.sqrt(2 * BOLTZMANN_CONSTANT * temperature / particleWithTemperature.getMass());
                double velocityComponent = Math.sqrt(newVelocityMagnitude / 2);
                particleWithTemperature.setVelocityX(velocityComponent);
                particleWithTemperature.setVelocityY(velocityComponent);
            }
        }
    }

    private ParticleWithTemperature getRandomParticle() {
        double positionX = StdRandom.uniform(0.0, 1.0);
        double positionY = StdRandom.uniform(0.0, 1.0);

        double velocityX = StdRandom.uniform(-0.005, 0.005);
        double velocityY = StdRandom.uniform(-0.005, 0.005);

        double radius = 0.005;
        double mass = 0.5;

        return new ParticleWithTemperature(positionX, positionY, velocityX, velocityY, radius, mass);
    }

    private void doExperiment(int numberOfParticles, int simulationTime, double hertz) {
        double baseLineTemperature = doBaselineTest(numberOfParticles, simulationTime, hertz);

        double[] testTemperatures = {
                baseLineTemperature / 4,
                baseLineTemperature / 2,
                baseLineTemperature * 2,
                baseLineTemperature * 4
        };

        for (int t = 0; t < testTemperatures.length; t++) {
            ParticleWithTemperature[] newParticles = new ParticleWithTemperature[numberOfParticles];
            resetCanvas();

            for (int i = 0; i < numberOfParticles; i++) {
                newParticles[i] = getRandomParticle();
            }

            CollisionSystemWithTemperature collisionSystemWithTemperature =
                    new CollisionSystemWithTemperature(newParticles);
            collisionSystemWithTemperature.setTemperature(testTemperatures[t]);

            collisionSystemWithTemperature.simulate(simulationTime, hertz);
        }
    }

    private double doBaselineTest(int numberOfParticles, int simulationTime, double hertz) {
        ParticleWithTemperature[] particles = new ParticleWithTemperature[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = getRandomParticle();
        }

        CollisionSystemWithTemperature collisionSystem = new CollisionSystemWithTemperature(particles);
        collisionSystem.simulate(simulationTime, hertz);

        return collisionSystem.temperature();
    }

    private void resetCanvas() {
        StdDraw.clear();
        StdDraw.setCanvasSize();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
    }

    public static void main(String[] args) {
        int numberOfParticles = 30;
        int simulationTime = 10000;
        double hertz = 0.5;

        new Exercise8_MaxwellBoltzmann().doExperiment(numberOfParticles, simulationTime, hertz);
    }

}

