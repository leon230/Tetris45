package com.example.leon.tetris.Model;

import java.util.Random;

/**
 * Created by lukasz.homik on 2017-02-21.
 */

public class Block {
    private int blockType;
    private  int startingX;
    private  int startingY;
    private int[] blocksX;
    private int[] blocksY;

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    public int getStartingX() {
        return startingX;
    }

    public void setStartingX(int startingX) {
        this.startingX = startingX;
    }

    public int getStartingY() {
        return startingY;
    }

    public void setStartingY(int startingY) {
        this.startingY = startingY;
    }

    public int[] getBlocksX() {
        return blocksX;
    }


    public int[] getBlocksY() {
        return blocksY;
    }


    public void createBlocksArray() {
        blocksX = new int[10];
        blocksY = new int[10];

        switch (blockType){
            case 0: setStartingY(2);
                    blocksX[0] = startingX;
                    blocksY[0] = startingY;
                    blocksX[1] = blocksX[0] + 1;
                    blocksY[1] = blocksY[0];
                    blocksX[2] = blocksX[0] + 1;
                    blocksY[2] = blocksY[0] - 1;
                    blocksX[3] = blocksX[0] + 2;
                    blocksY[3] = blocksY[0];
        }

    }

}
