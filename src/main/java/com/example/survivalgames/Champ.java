package com.example.survivalgames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import java.awt.*;


public class Champ {
    private int id;
    private int xCor;
    private int yCor;
    private int strength = 1;
    private int direction;
    private Color color;
    private final List<Integer> excludedDirection = new ArrayList<>();

    public Champ(int id, int x, int y) {
        this.id = id;
        this.xCor = x;
        this.yCor = y;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(xCor * Mechanics.UNIT_SIZE, yCor * Mechanics.UNIT_SIZE, Mechanics.UNIT_SIZE, Mechanics.UNIT_SIZE);
//        g.drawString(String.valueOf(id), xCor * Mechanics.UNIT_SIZE, yCor * Mechanics.UNIT_SIZE);
    }

    /*
    [0] - UP
    [1] - DOWN
    [2] - LEFT
    [3] - RIGHT
     */
    public void move(HashMap<String, Champ> champMap) {

        int newX;
        int newY;

        checkBorder();

        do {
            newX = xCor;
            newY = yCor;

            direction = getRandomDirection();


            switch (direction) {
                case 0 -> newY -= 1;
                case 1 -> newY += 1;
                case 2 -> newX -= 1;
                case 3 -> newX += 1;
                default -> {
                }
            }
        } while (champMap.containsKey(Mechanics.getPositionKey(newX, newY)));


        updatePosition(newX, newY, champMap);

    }

    public void fight(HashMap<String, Champ> champMap) {
        Champ opponent = getAdjacentChampion(xCor, yCor, champMap);
        if (opponent != null) {
            Champ winner = Mechanics.random.nextBoolean() ? this : opponent;
            Champ loser = winner == this ? opponent : this;
            Mechanics.removeChampion(loser);
        }
    }

    private Champ getAdjacentChampion(int x, int y, HashMap<String, Champ> champMap) {
        if (champMap.containsKey(Mechanics.getPositionKey(x, y - 1))) {
            return champMap.get(Mechanics.getPositionKey(x, y - 1));
        }
        if (champMap.containsKey(Mechanics.getPositionKey(x, y + 1))) {
            return champMap.get(Mechanics.getPositionKey(x, y + 1));
        }
        if (champMap.containsKey(Mechanics.getPositionKey(x - 1, y))) {
            return champMap.get(Mechanics.getPositionKey(x - 1, y));
        }
        if (champMap.containsKey(Mechanics.getPositionKey(x + 1, y))) {
            return champMap.get(Mechanics.getPositionKey(x + 1, y));
        }
        return null;
    }


    public void updatePosition(int newX, int newY, HashMap<String, Champ> champMap) {
        champMap.remove(Mechanics.getPositionKey(xCor, yCor));
        xCor = newX;
        yCor = newY;
        champMap.put(Mechanics.getPositionKey(xCor, yCor), this);
        excludedDirection.clear();
    }

    public int getRandomDirection() {
        do {
            direction = ThreadLocalRandom.current().nextInt(0, 4);
        } while (excludedDirection.contains(direction));

        return direction;
    }

    public void checkBorder() {
        if (yCor == 0) {
            excludedDirection.add(0);
        }
        if (yCor == Mechanics.CELLS - 1) {
            excludedDirection.add(1);
        }
        if (xCor == 0) {
            excludedDirection.add(2);
        }
        if (xCor == Mechanics.CELLS - 1) {
            excludedDirection.add(3);
        }
    }
//    public checkEnemies(){
//        if()
//    }


    public int getxCor() {
        return xCor;
    }

    public int getyCor() {
        return yCor;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
