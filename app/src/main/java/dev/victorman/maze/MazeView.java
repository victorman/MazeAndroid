package dev.victorman.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class MazeView extends View {
    private final Maze maze;
    private final float side;
    private Path mazePath;
    RectF newRect;
    RectF blackRect;
    private Paint yellowPaint;
    private Paint blackPaint;

    public MazeView(Context context, Maze maze, float cellSide) {
        super(context);
        this.maze = maze;
        this.side = cellSide;
        newRect = new RectF(0f,0f,0f,0f);
        yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void reset() {
        mazePath.rewind();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mazePath == null) {
            mazePath = new Path();
            mazePath.rewind();
        }

        if(blackRect == null) {
            blackRect = new RectF(0, 0, maze.width * side, maze.height * side);
        }

        while(!maze.newlyCarved.isEmpty()) {
            Point p = maze.newlyCarved.remove(0);
            newRect.set(p.x * side,
                    p.y * side,
                    (p.x + 1) * side,
                    (p.y + 1) * side);
//            Log.i("draw", newRect.toString());
            canvas.drawRect(newRect, yellowPaint);
            mazePath.addRect(newRect, Path.Direction.CCW);
        }
        canvas.clipOutPath(mazePath);
        canvas.drawRect(blackRect, blackPaint);
    }
}
