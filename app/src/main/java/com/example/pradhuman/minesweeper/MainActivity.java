package com.example.pradhuman.minesweeper;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static int row = 9;
    public static int col = 9;
    public static int No_OF_MINES = 10;
    MyButton[][] buttons;
    public int flag_c = No_OF_MINES;
    LinearLayout mainLayout;
    LinearLayout rowLayouts[];
    boolean gameOver = false;
    public boolean first_click = true;
    int mine_left = No_OF_MINES;
    int revleaed_total;
    TextView flag_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("CreateGrid","Went into function");
        Intent i = getIntent();
        String name  = i.getStringExtra("username");
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        revleaed_total = No_OF_MINES;
        flag_count = (TextView)findViewById(R.id.flagCount);
        ImageButton imageButton = (ImageButton)findViewById(R.id.ima);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBoard();
            }
        });
        createGrid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.newGame)
            resetBoard();
        else if(id==R.id.easy)
        {
            row = 9;
            col = 9;
            mine_left = No_OF_MINES = 10;
            flag_count.setText( String.format("%.03d",No_OF_MINES));
            flag_c = mine_left;
            Log.i("OMIFFFFFFFFF","before");
            createGrid();
            return true;
        }else if(id==R.id.medium){
            row = 16;
            col = 10;
            mine_left = No_OF_MINES = 30;
            flag_c = mine_left;
            flag_count.setText(String.format("%.03d",No_OF_MINES));
            createGrid();
            return true;
        }else if(id==R.id.hard){
            row = 20;
            col = 20;
            mine_left = No_OF_MINES = 50;
            flag_c = mine_left;
            flag_count.setText(String.format("%.03d",No_OF_MINES));
            createGrid();
            return true;
        }

        return true;
    }
    public void resetBoard(){
        gameOver = false;

        mine_left = No_OF_MINES;
        flag_c = No_OF_MINES;
        flag_count.setText(String.format("%.03d",No_OF_MINES));
        first_click = true;
        for(int i = 0;i<row;i++){
            for(int j=0;j<col;j++){
                buttons[i][j].setFlaged(false);
                buttons[i][j].setRevealed(false);
                buttons[i][j].setValue(0);
                buttons[i][j].setBomb(false);
                buttons[i][j].setImageResource(R.drawable.button);
            }
        }
        setbombs();
    }

    public void createGrid(){
        Log.i("CreateGrid","Went into function");
        flag_c = No_OF_MINES;
        flag_count.setText(String.format("%.03d",No_OF_MINES));
        revleaed_total = No_OF_MINES;
        mainLayout.removeAllViews();
        buttons = new MyButton[row][col];
        rowLayouts =  new LinearLayout[row];
        for(int i = 0; i < row; i++){
            rowLayouts[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f);
            params.setMargins(0,1,0,1);
            rowLayouts[i].setLayoutParams(params);
            rowLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rowLayouts[i]);
        }
        Log.i("CreateGrid","Set up row layout");
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                buttons[i][j] = new MyButton(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].x = i;
                buttons[i][j].y = j;
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                buttons[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                buttons[i][j].setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle));
                rowLayouts[i].addView(buttons[i][j]);
            }
        }
        Log.i("CreateGrid","Set up col Layout");
        setbombs();
        Log.i("CreateGrid","Set up bomb");
       /*
       #Print Grid
       for(int i =0 ; i< row; i++)
        {
            String print = " |";
            for(int j = 0;j<col;j++){
                print+= buttons[i][j].getValue()+" |";
            }
            Log.i("ROw",print+ " \n");
        }*/

    }

    @Override
    public boolean onLongClick(View view) {

        MyButton button = (MyButton)view;
        if(gameOver||button.getIsRevealed())
            return true;
        if(button.getIsFlaged()){
            button.setImageResource(R.drawable.button);
            if(button.getIsBomb()){
                revleaed_total--;
            }
            flag_c++;
            //String.format("%.03d",flag_c);
            flag_count.setText( String.format("%.03d",flag_c));
            button.setFlaged(false);
            return true;
        }
        if(revleaed_total!=0&&flag_c<=0)
            return true;
        button.setFlaged(true);
        flag_c--;
        flag_count.setText( String.format("%.03d",flag_c));
        button.setImageResource(R.drawable.flag);
        if(button.getIsBomb()){
            revleaed_total--;
           // flag_count.setText(revleaed_total+"");
        }
        if(revleaed_total==0&&flag_c==0) {
            Toast.makeText(this, "Game Won!!", Toast.LENGTH_SHORT).show();
            gameOver = true;
        }
        return true;
    }
    public void setbombs(){
        ImageButton imageButton = (ImageButton)findViewById(R.id.ima);
        imageButton.setImageResource(R.drawable.sm);
        int x,y;
        revleaed_total = No_OF_MINES;
        mine_left = No_OF_MINES;
        Random random = new Random();
        while (mine_left>0){
            x = random.nextInt(row);
            y = random.nextInt(col);

            if(!buttons[x][y].getIsBomb()){
                buttons[x][y].setValue(-1);
                buttons[x][y].setBomb(true);
                for(int i=x-1;i<=x+1;i++){
                    if(i<0||i>=row)
                        continue;
                    for(int j = y-1; j <= y+1 ;j++){
                        if(j<0||j>=col)
                            continue;
                        if(buttons[i][j].getIsBomb())
                            continue;
                        if(i==x&&j==y)
                            continue;
                        buttons[i][j].setValue(buttons[i][j].getValue()+1);
                    }
                }
                Log.i("Bomb Positions"," " + x+" " + y +" " + buttons[x][y].getValue());
                mine_left--;

            }
        }
    }
    @Override
    public void onClick(View v) {


        if(gameOver)
            return;
        MyButton button = (MyButton)v;
        if(button.getIsRevealed())
            return;
       if(button.getIsFlaged())
           return;
        //*****************************************************************************************

        //Implements First Click No Bomb
        Log.i("Mine_Initial", "ENTERED");
        if(first_click&&button.getIsBomb())
        {
            Log.i("Mine_Initial", "ENTERED");
            mine_left = 1;
            button.setValue(0);
            button.setBomb(false);
            for(int i=button.x-1;i<=button.x+1;i++){
                if(i<0||i>=row)
                    continue;
                for(int j = button.y-1; j <= button.y+1 ;j++){
                    if(j<0||j>=col)
                        continue;
                    if(buttons[i][j].getIsBomb())
                        continue;
                    if(i==button.x&&j==button.y)
                        continue;
                    buttons[i][j].setValue(buttons[i][j].getValue()+1);
                }
            }
            int  new_row=0;
            int new_col=0;
            boolean flag = false;
            while (mine_left>0)
            {
                for(new_row=0;new_row<row;new_row++)
                {
                    for(new_col=0;new_col<col;new_col++)
                    {
                        if(new_row==button.x&&new_col==button.y)
                            continue;
                        if(!buttons[new_row][new_col].getIsBomb()){
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        break;
                }
                if(!buttons[new_row][new_col].getIsBomb()){
                    buttons[new_row][new_col].setValue(-1);
                    buttons[new_row][new_col].setBomb(true);
                    for(int i=new_row-1;i<=new_row+1;i++){
                        if(i<0||i>=row)
                            continue;
                        for(int j = new_col-1; j <= new_col+1 ;j++){
                            if(j<0||j>=col)
                                continue;
                            if(buttons[i][j].getIsBomb()||buttons[i][j].getIsFlaged())
                                continue;
                            if(i==new_row&&j==new_col)
                                continue;
                            buttons[i][j].setValue(buttons[i][j].getValue()+1);
                        }
                    }
                    mine_left--;
                }
            }
            Log.i("Mine_Initial", "LEFT");
        }
        //******************************************************************************************
        //End of If
        Log.i("Mine_Initial", "EXIT");
        if(first_click) {
            first_click = false;

        }
        if(button.getIsBomb()){
            gameOver = true;
            ImageButton imageButton = (ImageButton)findViewById(R.id.ima);
            imageButton.setImageResource(R.drawable.dead);
            Toast.makeText(this,"Game Over!! You Lost",Toast.LENGTH_SHORT).show();
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(buttons[i][j].getIsBomb())
                    {
                        buttons[i][j].setImageResource(R.drawable.bomb_normal);
                    }
                    else {
                        switch (buttons[i][j].getValue()) {
                            case 0:
                                buttons[i][j].setImageResource(R.drawable.number_0);
                                break;
                            case 1:
                                buttons[i][j].setImageResource(R.drawable.number_1);
                                break;
                            case 2:
                                buttons[i][j].setImageResource(R.drawable.number_2);
                                break;
                            case 3:
                                buttons[i][j].setImageResource(R.drawable.number_3);
                                break;
                            case 4:
                                buttons[i][j].setImageResource(R.drawable.number_4);
                                break;
                            case 5:
                                buttons[i][j].setImageResource(R.drawable.number_5);
                                break;
                            case 6:
                                buttons[i][j].setImageResource(R.drawable.number_6);
                                break;
                            case 7:
                                buttons[i][j].setImageResource(R.drawable.number_7);
                                break;
                            case 8:
                                buttons[i][j].setImageResource(R.drawable.number_8);
                                break;
                        }
                    }
                    if(button.x ==i &&button.y==j)
                        button.setImageResource(R.drawable.bomb_exploded);

                }
            }

        }else{
            Log.d("YES NO Bomb","YO!!");
            if(button.getIsFlaged())
                return;
            if(button.getValue()==0) {


                LinkedList<Integer> row_ = new LinkedList<>();
                LinkedList<Integer> col_ = new LinkedList<>();
                row_.addLast(button.x);
                col_.addLast(button.y);

                while (!row_.isEmpty()) {
                    if(buttons[row_.getFirst()][col_.getFirst()].getIsFlaged())
                    {
                        row_.removeFirst();
                        col_.removeFirst();
                        continue;
                    }

                    buttons[row_.getFirst()][col_.getFirst()].setRevealed(true);
                    int _row = row_.getFirst();
                    int _col = col_.getFirst();
                    Log.d("NO, Bomb",_row+" "+ _col + " |");
                    buttons[_row][_col].setImageResource(R.drawable.number_0);
                    for (int i = _row - 1; i <= _row + 1; i++) {
                        if (i < 0 || i >= row)
                            continue;
                        for (int j = _col - 1; j <= _col + 1; j++) {
                            if (j < 0 || j >= col)
                                continue;
                            if (i == _row && j == _col)
                                continue;
                            if (!buttons[i][j].getIsBomb()) {
                                if (buttons[i][j].getValue() == 0&&!buttons[i][j].getIsRevealed()) {
                                    row_.addLast(i);
                                    col_.addLast(j);
                                } else {

                                    buttons[i][j].setRevealed(true);
                                    switch (buttons[i][j].getValue()){
                                        case 0:
                                            buttons[i][j].setImageResource(R.drawable.number_0);
                                            break;
                                        case 1:
                                            buttons[i][j].setImageResource(R.drawable.number_1);
                                            break;
                                        case 2:
                                            buttons[i][j].setImageResource(R.drawable.number_2);
                                            break;
                                        case 3:
                                            buttons[i][j].setImageResource(R.drawable.number_3);
                                            break;
                                        case 4:
                                            buttons[i][j].setImageResource(R.drawable.number_4);
                                            break;
                                        case 5:
                                            buttons[i][j].setImageResource(R.drawable.number_5);
                                            break;
                                        case 6:
                                            buttons[i][j].setImageResource(R.drawable.number_6);
                                            break;
                                        case 7:
                                            buttons[i][j].setImageResource(R.drawable.number_7);
                                            break;
                                        case 8:
                                            buttons[i][j].setImageResource(R.drawable.number_8);
                                            break;
                                    }
                                }

                            }
                        }
                    }
                    row_.removeFirst();
                    col_.removeFirst();
                }
            }
            else
            {

                button.setRevealed(true);
                switch (button.getValue()){
                    case 0:
                        button.setImageResource(R.drawable.number_0);
                        break;
                    case 1:
                        button.setImageResource(R.drawable.number_1);
                        break;
                    case 2:
                        button.setImageResource(R.drawable.number_2);
                        break;
                    case 3:
                        button.setImageResource(R.drawable.number_3);
                        break;
                    case 4:
                        button.setImageResource(R.drawable.number_4);
                        break;
                    case 5:
                        button.setImageResource(R.drawable.number_5);
                        break;
                    case 6:
                        button.setImageResource(R.drawable.number_6);
                        break;
                    case 7:
                        button.setImageResource(R.drawable.number_7);
                        break;
                    case 8:
                        button.setImageResource(R.drawable.number_8);
                        break;
                }
            }
        }
        int notRevel= row*col;
        for(int x=0;x<row;x++)
        {
            for(int y=0;y<col;y++){
                if(buttons[x][y].getIsRevealed())
                    notRevel--;
            }
        }
        if(notRevel == No_OF_MINES)
        {
            Toast.makeText(this, "Game Won!!", Toast.LENGTH_SHORT).show();
            gameOver = true;
        }

    }


}
