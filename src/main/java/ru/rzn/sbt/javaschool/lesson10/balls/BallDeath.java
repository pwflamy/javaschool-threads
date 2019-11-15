package ru.rzn.sbt.javaschool.lesson10.balls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class BallDeath implements Runnable {
    private Map<Ball, Future<?>> futures;
    private Random random;

    public BallDeath(Map<Ball, Future<?>> futures) {
        this.futures = futures;
        random = new Random();
    }

    @Override
    public void run() {
        while (futures.size() != 0) {
            int nextNumber = random.nextInt(futures.size());
            Ball nextBall = new ArrayList<Ball>(futures.keySet()).get(nextNumber);
            Future<?> future = futures.remove(nextBall);
            if (!future.isDone()) {
                future.cancel(true);
            }
            nextBall.world.removeBall(nextBall);
            try {
                Thread.sleep(1000 + random.nextInt(4000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
