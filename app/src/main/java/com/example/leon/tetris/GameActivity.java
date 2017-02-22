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
    Bitmap playgroundBitmap;

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
    int gameSpeed = 500;
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
            blockX[0] = numBlocksWide/2;
            blockY[0] = 10;
        }


        @Override
        public void run() {
            while (playingTetris) {
                updateGame();
                drawGame();
            controlFPS();
            }
        }
        public void updateGame(){
//            System.out.println(blockY[0]);
            blockY[0]++;
        }
        public void drawGame() {
            if (gameSurfaceHolder.getSurface().isValid()){
                canvas = gameSurfaceHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.GRAY);//the background
                paint.setColor(Color.argb(255, 255, 255, 100));
                paint.setTextSize(topGap/3);
                canvas.drawText("Score:" + score, 10, topGap-20, paint);

                //Draw borders
                paint.setStrokeWidth(3);//4 pixel border
                canvas.drawLine(leftGap,topGap,screenWidth-rightGap,topGap,paint);
                canvas.drawLine(leftGap,topGap,leftGap,numblocksHigh*blockSize + topGap,paint);
                canvas.drawLine(leftGap,screenHeight - downGap - topGap,screenWidth - rightGap,screenHeight - downGap - topGap,paint);
                canvas.drawLine(screenWidth - rightGap,screenHeight - downGap - topGap,screenWidth - rightGap,topGap,paint);

                canvas.drawBitmap(squareBitmap, blockX[0]*blockSize, (blockY[0]*blockSize), paint);
                for (int j = topGap/blockSize; j < 37; j++){
                for (int i = 1 + leftGap/blockSize; i <= 20; i++){
//
                        canvas.drawBitmap(playgroundBitmap, i*blockSize, j*blockSize, paint);
//                        canvas.drawBitmap(playgroundBitmap, 2*blockSize, 1*blockSize, paint);
//                        canvas.drawBitmap(playgroundBitmap, 3*blockSize, 1*blockSize, paint);
                    }
//
                }


                gameSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = gameSpeed - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {

                try {
                    gameThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    //Print an error message to the consoleLog.e("error", "failed to load sound files);
                }

            }

            lastFrameTime = System.currentTimeMillis();
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


        //Determine block size
//        blockSize = (screenWidth - rightGap - leftGap)/40;
        blockSize = 45;
        topGap = 2*blockSize;
        downGap = blockSize;
        leftGap = blockSize;
        rightGap = blockSize*2;
        System.out.println("leftGap: " + leftGap + " blockSize " + blockSize + "topGap " + topGap);
        numblocksHigh = (screenHeight - topGap - downGap);
        numBlocksWide = 20;

        squareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square);
        squareBitmap = Bitmap.createScaledBitmap(squareBitmap, blockSize, blockSize, false);
        playgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playground);
        playgroundBitmap = Bitmap.createScaledBitmap(playgroundBitmap, blockSize, blockSize, false);


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
