package ru.rzn.sbt.javaschool.lesson10.cars;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficController {
    //Пускаем машины по одной через мост
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

    /*private AtomicInteger numberRed = new AtomicInteger(0);
    private AtomicInteger numberBlue = new AtomicInteger(0);

    ReentrantLock lockRed = new ReentrantLock();
    ReentrantLock lockBlue = new ReentrantLock();


    public void enterLeft() {

    }

    public void enterRight() {

    }

    public void leaveLeft() {

    }

    public void leaveRight() {

    }*/
}