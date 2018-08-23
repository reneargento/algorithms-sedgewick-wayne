package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import util.MathUtil;

/**
 * Created by Rene Argento on 22/06/18.
 */

// Thermodynamic entropy formula:
// dS = dQ / T
// Where
// dS is the change in entropy
// dQ is the change in heat (can be computed by checking the overall change in kinetic energy of the system)
// T is the system temperature

// Boltzmann entropy formula:
// S = kb ln(W)
// Where
// S is the system entropy
// kb is the Boltzmann constant
// W is the number of ways that atoms/molecules/particles of a thermodynamic system can be arranged

// To compute W:
// Divide the region into rectangular cells.
// At any point in time: check how many particles are in which cells.
// This gives a distribution such as: (n1, n2, n3, n4, n5, ..., nl) = (1, 0, 0, 2, 0, ..., 0)
// Where one particle is in cell 1, no particles are in cells 2 and 3, two particles are in cell 4 and so on.
// W is equal to the number of arrangements compatible with that distribution.
// W = N! / n1! * n2! * n3! ... nl!
// where N is the number of particles.

// Classical results related to entropy that are proven in this exercise:
    // Related to thermodynamic entropy:
// 1- The Second Law of Thermodynamics
// In any thermodynamic process, the total entropy either increases or remains constant, but never decreases.
// 2- The Third Law of Thermodynamics
// As the temperature of any system approaches zero, its entropy approaches a minimum value.

    // Related to Boltzmann entropy:
// 3- A system tends to approach the equilibrium, evolving from states of lower toward states of higher probability.

// References:
// https://en.wikipedia.org/wiki/Entropy_(classical_thermodynamics)
// https://en.wikipedia.org/wiki/Boltzmann%27s_entropy_formula
// http://faculty.poly.edu/~jbain/physinfocomp/lectures/03.BoltzGibbsShannon.pdf
// https://www.quora.com/What-is-entropy-in-thermodynamics
public class Exercise5 {

    private final double BOLTZMANN_CONSTANT = 1.3806488e-23;

    public class ParticleWithTemperature extends Particle {

        private final int DIMENSION = 2;

        ParticleWithTemperature(double positionX, double positionY, double velocityX, double velocityY, double radius,
                                double mass) {
            super(positionX, positionY, velocityX, velocityY, radius, mass);
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

    public class CollisionSystem {

        private class Event implements Comparable<Event> {

            private final double time;
            private final ParticleWithTemperature particleA;
            private final ParticleWithTemperature particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;

            public Event(double time, ParticleWithTemperature particleA, ParticleWithTemperature particleB) {
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
        private ParticleWithTemperature[] particles;
        private int numberOfParticles;

        // Since we are dealing with elastic collisions, there is no change in kinetic energy after each collision.
        // However, when computing the resulting kinetic energy there may be a roundoff error due to floating point
        // division.
        private double KINETIC_ENERGY_ROUNDOFF_ERROR = 1e-16;
        private boolean thermodynamicEntropyNeverDecreases = true;
        private boolean whenTemperatureDecreasesEntropyDecreases = true;

        private double rowSize;
        private double columnSize;
        private final static int GRID_DIMENSION = 4;

        public CollisionSystem(ParticleWithTemperature[] particles) {
            if (particles.length > 10) {
                throw new IllegalArgumentException("For the Boltzmann entropy computation a small number of particles " +
                        "is needed. Create the system with no more than 10 particles");
            }

            StdDraw.enableDoubleBuffering();
            this.particles = particles;
            numberOfParticles = particles.length;

            rowSize = 1.0 / GRID_DIMENSION;
            columnSize = 1.0 / GRID_DIMENSION;
        }

        private void predictCollisions(ParticleWithTemperature particle, double limit) {
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

            double initialBoltzmannEntropy = boltzmannEntropy();

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

                ParticleWithTemperature particleA = event.particleA;
                ParticleWithTemperature particleB = event.particleB;

                double initialKineticEnergy = 0;

                if (particleA != null) {
                    initialKineticEnergy += particleA.kineticEnergy();
                }
                if (particleB != null) {
                    initialKineticEnergy += particleB.kineticEnergy();
                }

                if (particleA != null && particleB != null) {
                    particleA.bounceOff(particleB);
                } else if (particleA != null && particleB == null) {
                    particleA.bounceOffVerticalWall();
                } else if (particleA == null && particleB != null) {
                    particleB.bounceOffHorizontalWall();
                } else if (particleA == null && particleB == null) {
                    redraw(limit, hertz);
                }

                double finalKineticEnergy = 0;
                if (particleA != null) {
                    finalKineticEnergy += particleA.kineticEnergy();
                }
                if (particleB != null) {
                    finalKineticEnergy += particleB.kineticEnergy();
                }
                double changeInThermodynamicEntropy =
                        computeChangeInThermodynamicEntropy(initialKineticEnergy, finalKineticEnergy);

                if (changeInThermodynamicEntropy < 0) {
                    thermodynamicEntropyNeverDecreases = false;
                }

                predictCollisions(particleA, limit);
                predictCollisions(particleB, limit);
            }

            checkThirdLawOfThermodynamics();

            printResults(initialBoltzmannEntropy);
        }

        private void checkThirdLawOfThermodynamics() {
            double finalTemperature = temperature();

            double[] newTemperatures = {
                    finalTemperature / 2,
                    finalTemperature / 4,
                    finalTemperature / 8,
                    finalTemperature / 16,
            };

            for (int i = 0; i < newTemperatures.length; i++) {
                double initialKineticEnergy = getSystemKineticEnergy();

                setTemperature(newTemperatures[i]);

                double finalKineticEnergy = getSystemKineticEnergy();
                double changeInThermodynamicEntropy =
                        computeChangeInThermodynamicEntropy(initialKineticEnergy, finalKineticEnergy);

                if (changeInThermodynamicEntropy >= 0) {
                    whenTemperatureDecreasesEntropyDecreases = false;
                }
            }
        }

        private void printResults(double initialBoltzmannEntropy) {
            // Classical result 1
            if (thermodynamicEntropyNeverDecreases) {
                StdOut.println("Second Law of Thermodynamics: confirmed! " +
                        "The thermodynamic entropy either increases or remains constant, but never decreases.");
            } else {
                throw new IllegalStateException("Something is wrong. The thermodynamic entropy decreased.");
            }

            // Classical result 2
            StdOut.println();
            if (whenTemperatureDecreasesEntropyDecreases) {
                StdOut.println("Third Law of Thermodynamics: confirmed! " +
                        "As the temperature approaches zero, the entropy approaches a minimum value.");
            } else {
                throw new IllegalStateException("Something is wrong. The thermodynamic entropy increased when " +
                        "temperature decreased.");
            }

            // Classical result 3
            double finalBoltzmannEntropy = boltzmannEntropy();

            StdOut.println();
            if (finalBoltzmannEntropy > initialBoltzmannEntropy) {
                StdOut.println("A system tends to approach the equilibrium: confirmed!");
            } else {
                StdOut.println("The system did not approach the equilibrium. Unlikely but not impossible.");
            }
            StdOut.println("Initial Boltzmann entropy: " + initialBoltzmannEntropy +
                    "\nFinal Boltzmann entropy: " + finalBoltzmannEntropy);
        }

        private int getParticleCellId(ParticleWithTemperature particle) {
            int row = (int) Math.floor(particle.getPositionY() / rowSize);
            int column = (int) Math.floor(particle.getPositionX() / columnSize);

            return (row * GRID_DIMENSION) + column;
        }

        public double temperature() {
            double totalTemperature = 0;

            for(ParticleWithTemperature particle : particles) {
                totalTemperature += particle.temperature();
            }

            return totalTemperature / numberOfParticles;
        }

        // V = SQRT(2 kb T / M)
        // SQRT(V / 2) = (vx + vy) / 2
        private void setTemperature(double temperature) {

            for (ParticleWithTemperature particle : particles) {
                double newVelocityMagnitude = Math.sqrt(2 * BOLTZMANN_CONSTANT * temperature / particle.getMass());
                double velocityComponent = Math.sqrt(newVelocityMagnitude / 2);
                particle.setVelocityX(velocityComponent);
                particle.setVelocityY(velocityComponent);
            }

        }

        private double getSystemKineticEnergy() {
            double systemKineticEnergy = 0;

            for (ParticleWithTemperature particle : particles) {
                systemKineticEnergy += particle.kineticEnergy();
            }

            return systemKineticEnergy;
        }

        // dS = dQ / T
        private double computeChangeInThermodynamicEntropy(double initialKineticEnergy, double finalKineticEnergy) {
            double heatProduced = finalKineticEnergy - initialKineticEnergy;

            if (-KINETIC_ENERGY_ROUNDOFF_ERROR <= heatProduced && heatProduced <= KINETIC_ENERGY_ROUNDOFF_ERROR) {
                heatProduced = 0;
            }

            return heatProduced / temperature();
        }

        // S = kb ln(W)
        private double boltzmannEntropy() {
            int[] particlesPerCell = new int[GRID_DIMENSION * GRID_DIMENSION];

            for (ParticleWithTemperature particle : particles) {
                int cellId = getParticleCellId(particle);
                cellId = Math.max(cellId, 0);
                cellId = Math.min(cellId, particlesPerCell.length - 1);

                particlesPerCell[cellId]++;
            }

            double possibleArrangementsPerCell = 1;

            for (int particlesInCell : particlesPerCell) {
                possibleArrangementsPerCell *= MathUtil.factorial(particlesInCell);
            }

            double possibleStates = ((double) MathUtil.factorial(numberOfParticles)) / possibleArrangementsPerCell;
            return BOLTZMANN_CONSTANT * Math.log(possibleStates);
        }
    }

    public static void main(String[] args) {
        Exercise5 exercise5 = new Exercise5();

        // Initial state: minimum Boltzmann entropy when all particles are in the same cell
        ParticleWithTemperature particle1 = exercise5.new ParticleWithTemperature(0.52, 0.52,
                0.002, 0.003, 0.01, 0.5);
        ParticleWithTemperature particle2 = exercise5.new ParticleWithTemperature(0.5, 0.52,
                -0.001, 0.002, 0.01, 0.5);
        ParticleWithTemperature particle3 = exercise5.new ParticleWithTemperature(0.54, 0.52,
                0.002, 0.002, 0.01, 0.5);
        ParticleWithTemperature particle4 = exercise5.new ParticleWithTemperature(0.52, 0.5,
                0, -0.002, 0.01, 0.5);
        ParticleWithTemperature particle5 = exercise5.new ParticleWithTemperature(0.5, 0.5,
                -0.004, -0.003, 0.01, 0.5);
        ParticleWithTemperature particle6 = exercise5.new ParticleWithTemperature(0.54, 0.5,
                0.001, -0.002, 0.01, 0.5);

        ParticleWithTemperature[] particles = {
                particle1,
                particle2,
                particle3,
                particle4,
                particle5,
                particle6
        };

        CollisionSystem collisionSystem = exercise5.new CollisionSystem(particles);
        collisionSystem.simulate(10000, 0.5);
    }

}
