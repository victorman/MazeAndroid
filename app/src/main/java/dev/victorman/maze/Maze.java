package dev.victorman.maze;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

public class Maze {

    Random random;
    long seed;
    byte[][] map;
    Stack<Point> paths;
    public List<Point> newlyCarved;
    int width;
    int height;
    private Point start;

    public Maze(int width, int height, long seed) {

        if(width % 2 != 0 || height % 2 != 0)
            throw new IllegalArgumentException("width and height must be even");

        this.width = width;
        this.height = height;

        if(seed == 0L)
            this.seed = System.currentTimeMillis();
        else
            this.seed = seed;

        random = new Random(seed);

        map = new byte[height][width];

        for(int i = 0; i<map.length; i++)
            Arrays.fill(map[i],(byte)0);

        paths = new Stack<Point>();
        newlyCarved = new ArrayList<Point>();
    }

    public void restart() {
        start = null;
        paths.empty();

        for(int i = 0; i<map.length; i++)
            Arrays.fill(map[i],(byte)0);
    }


    public void determineStart() {
        start = new Point((Math.abs(random.nextInt()) % (width - 2))+1, 1);
        carve(new Point(start.x, 0));
        carve(start);
    }

    public  void determineFinish() {
        int r = Math.abs(random.nextInt()) % (width - 2)+1;
        carve(new Point(r, height-1));
        carve(new Point(r, height-2));
    }

    public void carve(Point p) {
        map[p.y][p.x] = 1;
        newlyCarved.add(p);
    }

    public boolean nextStep() {
        if (start == null) {
            determineFinish();
            determineStart();
            paths.push(start);
        } else {
            boolean traversed = false;
            while(!traversed && !paths.isEmpty()) {

                //peek the stack
                Point p = paths.peek();
                //traverse next by picking a direction, determinining validity, pushing onto the stack
                int rand = Math.abs(random.nextInt());
                int dir = Math.abs(random.nextInt()) % 2;
                for (int i = 0; i < 4; i++) {
                    rand += Math.pow(-1, dir);
                    switch (rand % 4) {
                        case 0:
                            if (isValid(up(p))) {
                                traversed = true;
                                carve(p, up(p));
                                paths.push(up(p));
                            }
                            break;
                        case 1:
                            if (isValid(right(p))) {
                                traversed = true;
                                carve(p, right(p));
                                paths.push(right(p));
                            }
                            break;
                        case 2:
                            if (isValid(down(p))) {
                                traversed = true;
                                carve(p, down(p));
                                paths.push(down(p));
                            }
                            break;
                        case 3:
                            if (isValid(left(p))) {
                                traversed = true;
                                carve(p, left(p));
                                paths.push(left(p));
                            }
                            break;
                    }
                    if(traversed)
                        break;
                }
                // if no valid traverses exist then pop
                if (!traversed) {
                    paths.pop();
                }
            }
        }

        return !paths.isEmpty();
    }

    private boolean isValid(Point p) {
        if(p.x < 1 || p.x >= width-1) {
            return false;
        }
        if(p.y < 1 || p.y >= height-1) {
            return false;
        }
        if(map[p.y][p.x] == 1) {
            return false;
        }
        return true;
    }


    private Point up(Point p) {
        return new Point(p.x, p.y+2);
    }
    private Point down(Point p) {
        return new Point(p.x, p.y-2);
    }
    private Point left(Point p) {
        return new Point(p.x-2, p.y);
    }
    private Point right(Point p) {
        return new Point(p.x+2, p.y);
    }

    public void carve(Point from, Point to) {
        //interim point
        Point interim = new Point(from.x + (to.x - from.x)/2, from.y + (to.y - from.y)/2);
        carve(interim);
        carve(to);
    }
}
