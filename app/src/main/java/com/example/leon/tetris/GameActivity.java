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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.leon.tetris.Model.Block;

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
    int[] [] backgroundArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);

        initializeScreen();
        blockX = new int[10];
        blockY = new int[10];
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

            Block block  = new Block();
            block.setStartingX(numBlocksWide/2);
            block.setBlockType(0);
            block.createBlocksArray();
            blockX = block.getBlocksX();
            blockY = block.getBlocksY();
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
            for (int i = 0; i < blockY.length; i++) {
                if (blockY[i] != 0){
                    blockY[i]++;
                }
            }
            for (int i = 0; i < blockY.length; i++){
                if (blockY[i] == numblocksHigh ||  backgroundArray[blockY[i]][blockX[i]] == 1){
                    updatebackgroundArray(blockX,blockY);
                    getBlock();
                }
            }
        }
        public void drawGame() {
            if (gameSurfaceHolder.getSurface().isValid()){
                canvas = gameSurfaceHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.GRAY);//the background
//                paint.setColor(Color.argb(255, 255, 255, 0));
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(topGap/3);
                canvas.drawText("Score:" + score, 10, topGap-20, paint);

                //Draw borders
                paint.setStrokeWidth(3);//4 pixel border
                //canvas.drawRect(leftGap,topGap,screenWidth - rightGap, numblocksHigh * blockSize, paint);



                //System.out.print("------------------------------------------------------------");
                for (int j = topGap/blockSize; j < (numblocksHigh); j++){
                    for (int i = Math.round(leftGap/blockSize); i < (screenWidth-rightGap)/blockSize; i++){
                        canvas.drawBitmap(playgroundBitmap, i*blockSize, j*blockSize, paint);
                    }
                }
//                System.out.println("blocksHigh : " + numblocksHigh + "screenHeight: " + screenHeight + " top: " + topGap + " down: " + downGap + "blocksize: " + blockSize);
//                System.out.print(numblocksHigh + "  2");
                //Draw figure
                for (int i = 0; i < blockY.length; i++) {
                    if (blockY[i] != 0) {
                        canvas.drawBitmap(squareBitmap, blockX[i] * blockSize, (blockY[i] * blockSize), paint);
                    }
                }

                //Draw background blocks
                for (int i = 0; i < numblocksHigh; i++) {
                    //System.out.print("\n");
                    for (int j = 0; j < numBlocksWide; j++) {
                        //System.out.print(backgroundArray[i][j]);
                        if (backgroundArray[i][j] == 1){

                            canvas.drawBitmap(squareBitmap,j*blockSize , i*blockSize, paint);
                        }
                    }
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

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    if (motionEvent.getX() >= screenWidth / 2) {

                        //turn right
                        for (int i = 0; i < blockX.length; i++) {
                            if (blockX[i] != 0 && blockX[i] < numBlocksWide){
                                blockX[i]++;
                            }
                        }
                    } else {
                        //turn left
                        for (int i = 0; i < blockX.length; i++) {
                            if (blockX[i] != 0 && blockX[i] > 0){
                                blockX[i]--;
                            }
                        }
                    }
            }
            return true;
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

        numBlocksWide = 10;
        leftGap= 100;
        rightGap = 100;
        blockSize = (screenWidth - leftGap - rightGap)/numBlocksWide;
        topGap = 2*blockSize;
        downGap = blockSize/2;

        numblocksHigh = (screenHeight - topGap - downGap)/blockSize;
//        System.out.println("leftGap: " + leftGap + " blockSize " + blockSize + "topGap " + topGap);


        squareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square);
        squareBitmap = Bitmap.createScaledBitmap(squareBitmap, blockSize, blockSize, false);
        playgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.playground);
        playgroundBitmap = Bitmap.createScaledBitmap(playgroundBitmap, blockSize, blockSize, false);

        backgroundArray = new int[numblocksHigh][numBlocksWide + 1];
        for (int i = 0; i < numblocksHigh; i++) {
            for (int j = 0; j < numBlocksWide; j++) {
                backgroundArray[i][j] = 0;
            }
        }
//        backgroundArray[2][2] = 1;

    }
    public void updatebackgroundArray(int[] coordX, int[] coordY){
        for (int i = 0; i < coordX.length; i++){
            if (coordX[i] != 0 && coordY[i] != 0){
                backgroundArray[coordY[i]-1][coordX[i]] = 1;
            }
        }
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
