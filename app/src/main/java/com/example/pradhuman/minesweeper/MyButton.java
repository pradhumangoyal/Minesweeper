package com.example.pradhuman.minesweeper;

import android.widget.Button;
import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by Pradhuman on 16-06-2017.
 */

public class MyButton extends ImageButton {

    private boolean isBomb;
    private boolean isFlaged;
    private boolean isRevealed;
    private int value;
    int x,y;

    public MyButton(Context context) {
        super(context);
        isBomb = false;
        isFlaged = false;
        isRevealed = false;
        value = 0;
        this.setImageResource(R.drawable.button);
    }

    public boolean getIsBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        this.isBomb = bomb;
    }

    public boolean getIsFlaged() {
        return isFlaged;
    }

    public void setFlaged(boolean flaged) {
        this.isFlaged = flaged;
    }

    public boolean getIsRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean Revealed) {
        this.isRevealed = Revealed;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}