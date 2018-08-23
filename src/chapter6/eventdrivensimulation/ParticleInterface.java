package chapter6.eventdrivensimulation;

/**
 * Created by Rene Argento on 28/04/18.
 */
public interface ParticleInterface {

    void draw();

    void move(double time);

    int count();

    double timeToHit(ParticleInterface otherParticle);

    double timeToHitHorizontalWall();

    double timeToHitVerticalWall();

    void bounceOff(ParticleInterface otherParticle);

    void bounceOffHorizontalWall();

    void bounceOffVerticalWall();

}
