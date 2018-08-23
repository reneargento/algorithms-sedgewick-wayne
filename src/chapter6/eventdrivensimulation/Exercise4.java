package chapter6.eventdrivensimulation;

import chapter2.section4.PriorityQueueResize;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento on 10/06/18.
 */
// Adding the ability to only check the adjacent cells when predicting collisions introduces the possibility of
// internal collisions. Consider the following scenario where particle A is moving upwards and particle B is moving
// downwards:
// ----------
// |  | B|  |
// ----------
// |  |  |  |
// ----------
// |  | A|  |
// ----------
// Particles A and B would not know about the existence of each other before moving to the center cell, which could
// possibly cause an internal collision. This is likely to happen especially when the cells are small.
// To address this scenario, internal collisions are allowed and the method timeToHitConsideringInternalCollisions() is
// used, in combination with the MINIMUM_TIME constant.
// The MINIMUM_TIME constant is also used to guarantee that even if a particle is very close to the next cell
// (with a distance so small that the MOVE_CELL event time would be almost equal to the current overall time,
// possibly getting the particle stuck), the particle will continue moving.
public class Exercise4 {

    private enum EventType {
        PARTICLE_COLLISION, HORIZONTAL_WALL_COLLISION, VERTICAL_WALL_COLLISION, DRAW, MOVE_CELL
    }

    public class CollisionSystem {

        private class Event implements Comparable<Event> {

            private final double time;
            private final Particle particleA;
            private final Particle particleB;
            private final int collisionsCountA;
            private final int collisionsCountB;
            private final EventType eventType;

            public Event(double time, Particle particleA, Particle particleB, EventType eventType) {
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

        private class Cell {
            private int row;
            private int column;

            Cell(int row, int column) {
                this.row = row;
                this.column = column;
            }
        }

        private PriorityQueueResize<Event> priorityQueue;
        private double time;
        private Particle[] particles;
        private HashSet<Particle>[][] area;
        private SeparateChainingHashTable<Particle, Cell> particleCellLocations;
        private HashSet<Particle> particlesWithValidHorizontalWallEvent;
        private HashSet<Particle> particlesWithValidVerticalWallEvent;
        private double rowSize;
        private double columnSize;
        private static final double MINIMUM_TIME = 0.01;

        @SuppressWarnings("unchecked")
        public CollisionSystem(Particle[] particles, int rows, int columns) {
            this.particles = particles;
            StdDraw.enableDoubleBuffering();

            if (rows < 1 || rows > 100) {
                throw new IllegalArgumentException("There must be between 1 and 100 rows in the rectangular area");
            }

            if (columns < 1 || columns > 100) {
                throw new IllegalArgumentException("There must be between 1 and 100 columns in the rectangular area");
            }

            area = new HashSet[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    area[row][column] = new HashSet<>();
                }
            }

            rowSize = 1.0 / area.length;
            columnSize = 1.0 / area[0].length;

            particleCellLocations = new SeparateChainingHashTable<>();
            particlesWithValidHorizontalWallEvent = new HashSet<>();
            particlesWithValidVerticalWallEvent = new HashSet<>();

            for (Particle particle : particles) {
                Cell cell = getParticleCell(particle.getPositionX(), particle.getPositionY());
                updateParticlePositionInArea(particle, cell);
            }
        }

        private Cell getParticleCell(double positionX, double positionY) {
            int row = (int) Math.floor(positionY / rowSize);
            int column = (int) Math.floor(positionX / columnSize);
            return new Cell(row, column);
        }

        private void updateParticlePositionInArea(Particle particle, Cell cell) {
            particleCellLocations.put(particle, cell);
            area[cell.row][cell.column].add(particle);
        }

        private void predictCollisions(Particle particle, double limit) {
            if (particle == null) {
                return;
            }

            Cell currentParticleCell = particleCellLocations.get(particle);

            int[] neighborRows = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
            int[] neighborColumns = {-1, 0, 1, -1, 0, 1, -1, 0, 1};

            for (int neighbor = 0; neighbor < neighborRows.length; neighbor++) {
                int neighborRow = currentParticleCell.row + neighborRows[neighbor];
                int neighborColumn = currentParticleCell.column + neighborColumns[neighbor];

                if (isValidCell(neighborRow, neighborColumn)) {
                    for (Particle particleInCell : area[neighborRow][neighborColumn].keys()) {
                        double deltaTime = timeToHitConsideringInternalCollisions(particle, particleInCell);

                        if (time + deltaTime <= limit) {
                            priorityQueue.insert(new Event(time + deltaTime, particle, particleInCell,
                                    EventType.PARTICLE_COLLISION));
                        }
                    }
                }
            }

            if (!particlesWithValidHorizontalWallEvent.contains(particle)) {
                double deltaTimeHorizontalWall = particle.timeToHitHorizontalWall();

                // Edge case for when the particle center + radius would be located beyond the wall
                if (deltaTimeHorizontalWall < 0) {
                    deltaTimeHorizontalWall = 0;
                }

                if (time + deltaTimeHorizontalWall <= limit) {
                    priorityQueue.insert(new Event(time + deltaTimeHorizontalWall, particle, null,
                            EventType.HORIZONTAL_WALL_COLLISION));
                    particlesWithValidHorizontalWallEvent.add(particle);
                }
            }

            if (!particlesWithValidVerticalWallEvent.contains(particle)) {
                double deltaTimeVerticalWall = particle.timeToHitVerticalWall();

                // Edge case for when the particle center + radius would be located beyond the wall
                if (deltaTimeVerticalWall < 0) {
                    deltaTimeVerticalWall = 0;
                }

                if (time + deltaTimeVerticalWall <= limit) {
                    priorityQueue.insert(new Event(time + deltaTimeVerticalWall, particle, null,
                            EventType.VERTICAL_WALL_COLLISION));
                    particlesWithValidVerticalWallEvent.add(particle);
                }
            }

            double timeToReachNextCell = timeToMoveCell(particle);
            if (time + timeToReachNextCell <= limit) {
                priorityQueue.insert(new Event(time + timeToReachNextCell, particle, null, EventType.MOVE_CELL));
            }
        }

        private double timeToMoveCell(Particle particle) {
            double positionX = particle.getPositionX();
            double positionY = particle.getPositionY();
            double velocityX = particle.getVelocityX();
            double velocityY = particle.getVelocityY();
            double radius = particle.getRadius();

            if (velocityX == 0 && velocityY == 0) {
                return Double.POSITIVE_INFINITY;
            }

            double currentSmallestTime = Double.POSITIVE_INFINITY;
            Cell currentCell = particleCellLocations.get(particle);

            // There is no need to compute the time to reach diagonal cells because it will never be smaller than
            // the time to hit horizontal or vertical cells.
            double timeToReachNextRightCell;
            double timeToReachNextLeftCell;
            double timeToReachNextTopCell;
            double timeToReachNextBottomCell;

            if (velocityX > 0) {
                int nextRightColumn;
                // Check whether the particle is at the end of the current cell
                if (positionX + radius >= (currentCell.column + 1) * columnSize) {
                    nextRightColumn = currentCell.column + 2;
                } else {
                    nextRightColumn = currentCell.column + 1;
                }

                if (nextRightColumn < area[0].length) {
                    double nextRightCellLocation = (nextRightColumn * columnSize) - radius;

                    double distance = nextRightCellLocation - positionX;
                    timeToReachNextRightCell = distance / velocityX;

                    if (timeToReachNextRightCell < MINIMUM_TIME) {
                        timeToReachNextRightCell = MINIMUM_TIME;
                    }

                    if (timeToReachNextRightCell < currentSmallestTime) {
                        currentSmallestTime = timeToReachNextRightCell;
                    }
                }
            } else if (velocityX < 0) {
                int nextLeftColumn;
                // Check whether the particle is at the end of the current cell
                if (positionX - radius <= (currentCell.column - 1) * columnSize) {
                    nextLeftColumn = currentCell.column - 2;
                } else {
                    nextLeftColumn = currentCell.column - 1;
                }

                if (nextLeftColumn >= 0) {
                    double nextLeftCellLocation = (nextLeftColumn * columnSize) + radius;

                    // Get the distance as a negative number
                    double distance = nextLeftCellLocation - positionX;
                    timeToReachNextLeftCell = distance / velocityX;

                    if (timeToReachNextLeftCell < MINIMUM_TIME) {
                        timeToReachNextLeftCell = MINIMUM_TIME;
                    }

                    if (timeToReachNextLeftCell < currentSmallestTime) {
                        currentSmallestTime = timeToReachNextLeftCell;
                    }
                }
            }

            if (velocityY > 0) {
                int nextTopRow;
                // Check whether the particle is at the end of the current cell
                if (positionY + radius >= (currentCell.row + 1) * rowSize) {
                    nextTopRow = currentCell.row + 2;
                } else {
                    nextTopRow = currentCell.row + 1;
                }

                if (nextTopRow < area.length) {
                    double nextTopCellLocation = (nextTopRow * rowSize) - radius;
                    double distance = nextTopCellLocation - positionY;
                    timeToReachNextTopCell = distance / velocityY;

                    if (timeToReachNextTopCell < MINIMUM_TIME) {
                        timeToReachNextTopCell = MINIMUM_TIME;
                    }

                    if (timeToReachNextTopCell < currentSmallestTime) {
                        currentSmallestTime = timeToReachNextTopCell;
                    }
                }
            } else if (velocityY < 0) {
                int nextBottomRow;
                // Check whether the particle is at the end of the current cell
                if (positionY - radius <= (currentCell.row - 1) * rowSize) {
                    nextBottomRow = currentCell.row - 2;
                } else {
                    nextBottomRow = currentCell.row - 1;
                }

                if (nextBottomRow >= 0) {
                    double nextBottomCellLocation = (nextBottomRow * rowSize) + radius;

                    // Get the distance as a negative number
                    double distance = nextBottomCellLocation - positionY;
                    timeToReachNextBottomCell = distance / velocityY;

                    if (timeToReachNextBottomCell < MINIMUM_TIME) {
                        timeToReachNextBottomCell = MINIMUM_TIME;
                    }

                    if (timeToReachNextBottomCell < currentSmallestTime) {
                        currentSmallestTime = timeToReachNextBottomCell;
                    }
                }
            }

            return currentSmallestTime;
        }

        private boolean isValidCell(int row, int column) {
            return row >= 0 && row < area.length && column >= 0 && column < area[0].length;
        }

        public void redraw(double limit, double hertz) {
            StdDraw.clear();

            for (int i = 0; i < particles.length; i++) {
                particles[i].draw();
            }

            StdDraw.pause(20);
            StdDraw.show();

            if (time < limit) {
                priorityQueue.insert(new Event(time + 1.0 / hertz, null, null, EventType.DRAW));
            }
        }

        public void simulate(double limit, double hertz) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

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
                    Cell cell = particleCellLocations.get(particles[i]);
                    area[cell.row][cell.column].delete(particles[i]);

                    particles[i].move(event.time - time);

                    Cell updatedCell = getParticleCell(particles[i].getPositionX(), particles[i].getPositionY());
                    updateParticlePositionInArea(particles[i], updatedCell);
                }

                // Update time
                time = event.time;

                Particle particleA = event.particleA;
                Particle particleB = event.particleB;

                switch (event.eventType) {
                    case PARTICLE_COLLISION:
                        particleA.bounceOff(particleB);
                        deleteWallCollisionEvents(particleA);
                        deleteWallCollisionEvents(particleB);
                        break;
                    case HORIZONTAL_WALL_COLLISION:
                        particleA.bounceOffHorizontalWall();
                        deleteWallCollisionEvents(particleA);
                        break;
                    case VERTICAL_WALL_COLLISION:
                        particleA.bounceOffVerticalWall();
                        deleteWallCollisionEvents(particleA);
                        break;
                    case DRAW:
                        redraw(limit, hertz);
                        break;
                    case MOVE_CELL:
                        // No actions to take in this event because the particle position was already updated
                        break;
                }

                predictCollisions(particleA, limit);
                predictCollisions(particleB, limit);
            }
        }

        private void deleteWallCollisionEvents(Particle particle) {
            particlesWithValidVerticalWallEvent.delete(particle);
            particlesWithValidHorizontalWallEvent.delete(particle);
        }

        private double timeToHitConsideringInternalCollisions(Particle particle, Particle otherParticle) {
            if (particle == otherParticle) {
                return Double.POSITIVE_INFINITY;
            }

            double deltaPositionX = otherParticle.getPositionX() - particle.getPositionX();
            double deltaPositionY = otherParticle.getPositionY() - particle.getPositionY();

            double deltaVelocityX = otherParticle.getVelocityX() - particle.getVelocityX();
            double deltaVelocityY = otherParticle.getVelocityY() - particle.getVelocityY();

            double deltaPositionByDeltaVelocity = deltaPositionX * deltaVelocityX + deltaPositionY * deltaVelocityY;
            if (deltaPositionByDeltaVelocity > 0) {
                return Double.POSITIVE_INFINITY;
            }

            double deltaVelocitySquared = deltaVelocityX * deltaVelocityX + deltaVelocityY * deltaVelocityY;
            double deltaPositionSquared = deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY;

            double distanceBetweenCenters = particle.getRadius() + otherParticle.getRadius();
            double distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters;

            // Check if particles overlap and an internal collision is occurring.
            // If it is, a collision is imminent.
            if (deltaPositionSquared < distanceBetweenCentersSquared) {
                return MINIMUM_TIME;
            }

            double distance = (deltaPositionByDeltaVelocity * deltaPositionByDeltaVelocity)
                    - deltaVelocitySquared * (deltaPositionSquared - distanceBetweenCentersSquared);

            if (distance < 0) {
                return Double.POSITIVE_INFINITY;
            }

            return -(deltaPositionByDeltaVelocity + Math.sqrt(distance)) / deltaVelocitySquared;
        }
    }

    public static void main(String[] args) {
        Exercise4 exercise4 = new Exercise4();

        int numberOfParticles = 30;
        int rows = 8;
        int columns = 12;
        Particle[] particles = new Particle[numberOfParticles];

        for (int i = 0; i < numberOfParticles; i++) {
            particles[i] = new Particle();
        }

        CollisionSystem collisionSystem = exercise4.new CollisionSystem(particles, rows, columns);
        collisionSystem.simulate(10000, 0.5);
    }

}
