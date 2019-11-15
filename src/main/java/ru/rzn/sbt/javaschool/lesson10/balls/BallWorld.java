package ru.rzn.sbt.javaschool.lesson10.balls;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BallWorld extends JPanel {

    private final int xSize = 250;
    private final int ySize = 250;

    private final static Color BGCOLOR = Color.white;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private ArrayList<Ball> balls = new ArrayList<>();

    public BallWorld() {
        setPreferredSize(new Dimension(xSize, ySize));
        setOpaque(true);
        setBackground(BGCOLOR);
    }

    public void addBall(final Ball b) {
        balls.add(b);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        lock.readLock().lock();
        for (Ball b : balls) {
            b.draw(g);
        }
        lock.readLock().unlock();
    }

    public void removeBall(Ball ball) {
        lock.writeLock().lock();
        balls.remove(ball);
        lock.writeLock().unlock();
    }
}
