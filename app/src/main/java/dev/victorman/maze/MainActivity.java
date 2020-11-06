package dev.victorman.maze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int width = 30;
    int height;
    private Maze maze;
    private MazeView mazeView;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        int widthPixels = displayMetrics.widthPixels;

        final int cellSide = widthPixels / width;
        height = (int)Math.floor((double)(heightPixels / cellSide));

        maze = new Maze(width, height, 0);

        mazeView = new MazeView(getApplicationContext(), maze , cellSide);

        mazeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    maze.restart();
                    mazeView.reset();
                }
                return true;
            }
        });

        Runnable r = new Runnable() {
            long elapsedTime = 0L;
            long fps = 12L;
            long frameDuration = 1000L / fps;
            long lastTime = 0L;
            @Override
            public void run() {
                while(true) {
                    elapsedTime = System.currentTimeMillis() - lastTime;
                    if (elapsedTime > frameDuration) {
                        maze.nextStep();
                        mazeView.invalidate();
                        lastTime = System.currentTimeMillis();
                    }
                }
            }
        };

        setContentView(mazeView);

        thread = new Thread(r);
        thread.start();
    }
}