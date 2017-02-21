package com.example.leon.tetris;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameActivity extends Activity {

    Canvas canvas;
    TetrisView tetrisView;
    Bitmap sigleSquare;

    int screenWidth;
    int screenHeight;
    int topGap;
    int rightGap;
    int downGap;
    int leftGap;

    //Block definition
    int blockSize;
    int numBlocksWide;
    int numblocksHigh;

    long lastFrameTime;
    int fps;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);

        initializeScreen();
        tetrisView = new TetrisView(this);
        setContentView(tetrisView);

    }

    class TetrisView extends SurfaceView implements Runnable{
        Thread gameThread = null;
        SurfaceHolder gameSurfaceHolder;
        volatile boolean playingTetris;
        Paint paint;

        public TetrisView(Context context) {
            super(context);
            gameSurfaceHolder = getHolder();
            paint = new Paint();
        }

        @Override
        public void run() {
            while (playingTetris) {
                updateGame();
                drawGame();
//            controlFPS();
            }
        }
        public void updateGame(){

        }
        public void drawGame() {
            if (gameSurfaceHolder.getSurface().isValid()){
                canvas = gameSurfaceHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.GRAY);//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(topGap/2);
                canvas.drawText("Score:" + score + "  Hi:" + "10", 10, topGap-6, paint);

                //draw a border - 4 lines, top right, bottom , left
                paint.setStrokeWidth(3);//4 pixel border
                canvas.drawLine(leftGap,topGap,screenWidth-rightGap,topGap,paint);
                canvas.drawLine(leftGap,topGap,leftGap,screenHeight - downGap,paint);
                canvas.drawLine(leftGap,screenHeight - downGap,screenWidth - rightGap,screenHeight - downGap,paint);
                canvas.drawLine(screenWidth - rightGap,screenHeight - downGap,screenWidth - rightGap,topGap,paint);


                gameSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        public void pause() {
            playingTetris = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
            }

        }

        public void resume() {
            playingTetris = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    public void initializeScreen(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        topGap = screenHeight/14;
        downGap = screenHeight/14;
        leftGap = screenWidth/25;
        rightGap = screenWidth/10;
        //Determine block size
        blockSize = (screenWidth - rightGap)/40;
        numblocksHigh = (screenHeight - topGap)/blockSize;
        numBlocksWide = (screenWidth - rightGap);


    }
    @Override
    protected void onStop() {
        super.onStop();

        while (true) {
            tetrisView.pause();
            break;
        }

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        tetrisView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tetrisView.pause();
    }
}
