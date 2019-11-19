package ru.rzn.sbt.javaschool.lesson10.cars;

import java.util.concurrent.Phaser;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TrafficController {
    /*//Пускаем машины по одной через мост
    ReentrantLock bridge = new ReentrantLock();

    public void enterLeft() {
        bridge.lock();
    }

    public void enterRight() {
        bridge.lock();
    }

    public void leaveLeft() {
        bridge.unlock();
    }

    public void leaveRight() {
        bridge.unlock();
    }
    */

    //Разрешаем одновременное движение по мосту машинам одного цвета
    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int NONE = 2;

    private Phaser phaser = new Phaser() {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            lock.writeLock().lock();
            colorBridge = NONE;
            lock.writeLock().unlock();
            return false;
        }
    };

    private volatile int colorBridge = NONE;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void enterLeft() {
        lock.readLock().lock();
        switch (colorBridge) {
            case NONE:
                lock.readLock().unlock();
                lock.writeLock().lock();
                phaser.register();
                colorBridge = RED;
                lock.writeLock().unlock();
                break;
            case RED:
                phaser.register();
                lock.readLock().unlock();
                break;
            case BLUE:
                lock.readLock().unlock();
                phaser.awaitAdvance(phaser.getPhase());
                lock.writeLock().lock();
                phaser.register();
                colorBridge = RED;
                lock.writeLock().unlock();
                break;
        }
    }

    public void enterRight() {
        lock.readLock().lock();
        switch (colorBridge) {
            case NONE:
                lock.readLock().unlock();
                lock.writeLock().lock();
                phaser.register();
                colorBridge = BLUE;
                lock.writeLock().unlock();
                break;
            case RED:
                lock.readLock().unlock();
                phaser.awaitAdvance(phaser.getPhase());
                lock.writeLock().lock();
                phaser.register();
                colorBridge = BLUE;
                lock.writeLock().unlock();
                break;
            case BLUE:
                phaser.register();
                lock.readLock().unlock();
                break;
        }
    }

    public void leaveLeft() {
        phaser.arriveAndDeregister();
    }

    public void leaveRight() {
        phaser.arriveAndDeregister();
    }
}