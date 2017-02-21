package com.example.leon.tetris;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    Bitmap squareBitmap;

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
    //Block matrix
    int[] blockX;
    int[] blockY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);

        initializeScreen();
        blockX = new int[4];
        blockY = new int[4];
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

            getBlock();
        }
        public void getBlock(){
            blockX[0] = 40;
            blockY[0] = 40;
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
            //blockY[0]--;
        }
        public void drawGame() {
            if (gameSurfaceHolder.getSurface().isValid()){
                canvas = gameSurfaceHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.GRAY);//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(topGap/3);
                canvas.drawText("Score:" + score, 10, topGap-20, paint);

                //Draw borders
                paint.setStrokeWidth(3);//4 pixel border
                canvas.drawLine(leftGap,topGap,screenWidth-rightGap,topGap,paint);
                canvas.drawLine(leftGap,topGap,leftGap,screenHeight - downGap,paint);
                canvas.drawLine(leftGap,screenHeight - downGap,screenWidth - rightGap,screenHeight - downGap,paint);
                canvas.drawLine(screenWidth - rightGap,screenHeight - downGap,screenWidth - rightGap,topGap,paint);

                canvas.drawBitmap(squareBitmap, blockX[0]*blockSize, (blockY[0]*blockSize), paint);


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
        rightGap = screenWidth/8;
        //Determine block size
        blockSize = (screenWidth - rightGap - leftGap)/40;
        numblocksHigh = (screenHeight - topGap)/blockSize;
        numBlocksWide = 40;

        squareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square);


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
