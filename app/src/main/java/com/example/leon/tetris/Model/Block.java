package com.example.leon.tetris.Model;

import java.util.Random;

/**
 * Created by lukasz.homik on 2017-02-21.
 */

public class Block {
    private int blockType;
    private int[][] blocksArray;

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    public int[][] getBlocksArray() {
        switch (blockType){
            case 0: return blocksArray;
        }

        return blocksArray;
    }

    public void setBlocksArray(int[][] blocksArray) {
        this.blocksArray = blocksArray;
    }
}
