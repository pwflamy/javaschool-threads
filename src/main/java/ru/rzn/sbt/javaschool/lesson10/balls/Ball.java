package ru.rzn.sbt.javaschool.lesson10.balls;

import java.awt.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

public class Ball implements Runnable {

    BallWorld world;

    private volatile boolean visible = false;

    private int xpos, ypos, xinc, yinc;

    private final Color col;

    private final static int BALLW = 10;
    private final static int BALLH = 10;

    public Ball(BallWorld world, int xpos, int ypos, int xinc, int yinc, Color col) {

        this.world = world;
        this.xpos = xpos;
        this.ypos = ypos;
        this.xinc = xinc;
        this.yinc = yinc;
        this.col = col;

        world.addBall(this);

        phaser.register();
    }

    @Override
    public void run() {
        this.visible = true;
        try {
            while (!Thread.interrupted()) {
                move();
            }
        } catch (InterruptedException e ){
            Thread.currentThread().interrupt();
        } finally {
            phaser.arriveAndDeregister();
        }
        world.repaint();
    }

    private final static Phaser phaser = new Phaser() {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            return false;
        }
    };

    public void move() throws InterruptedException {
        if (xpos >= world.getWidth() - BALLW || xpos <= 0) xinc = -xinc;

        if (ypos >= world.getHeight() - BALLH || ypos <= 0) yinc = -yinc;

        int lastXpos = xpos;
        int lastYpos = ypos;

        Thread.sleep(30);
        doMove();
        world.repaint();

        if ( ((lastXpos>lastYpos) && (xpos<=ypos)) || ((lastXpos<lastYpos) && (xpos>=ypos)) ) {
            phaser.arriveAndAwaitAdvance();
        }
    }

    public synchronized void doMove() {
        xpos += xinc;
        ypos += yinc;
    }

    public synchronized void draw(Graphics g) {
        if (visible) {
            g.setColor(col);
            g.fillOval(xpos, ypos, BALLW, BALLH);
        }
    }

    public void removeSelf() {
        world.removeBall(this);
    }
}
