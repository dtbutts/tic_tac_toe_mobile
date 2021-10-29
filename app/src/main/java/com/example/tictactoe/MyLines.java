package com.example.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.GnssAntennaInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyLines extends View {

    //constants
    private static final int E = 0;
    private static final int X = 1;
    private static final int O = 2;

    //styles
    private final int boardColor;
    private final int xColor;
    private final int oColor;

    //using for calculations
    public int turn = X;
    boolean done = false;
    int numClicks = 0;

    //pieces
    private int[][] pieces;

    //paint object
    private Paint painter = new Paint();

    //sizes
    private int cellSize = getWidth()/3;

    //top message
    public TextView message;

    //required constructor
    public MyLines(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        //initialize pieces
        pieces = new int[3][3];
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                pieces[i][j] = 0;
            }
        }

        //get styles
        TypedArray styles = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyLines, 0, 0);
        try{
            boardColor = styles.getInteger(R.styleable.MyLines_boardColor, 0);
            xColor = styles.getInteger(R.styleable.MyLines_xColor, 0);
            oColor = styles.getInteger(R.styleable.MyLines_oColor, 0);
        }finally{
            styles.recycle();
        }
    }


    //logic of the drawing
    @Override
    protected void onDraw(Canvas canvas){
        painter.setStyle(Paint.Style.STROKE);
        painter.setAntiAlias(true);

        drawBoard(canvas);
        drawPieces(canvas);
    }

    //makes the board a perfect square
    @Override
    public void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dimensions = getMeasuredWidth();
        cellSize = dimensions/3;
        setMeasuredDimension(dimensions, dimensions);
    }

    //draws the lines of the board
    private void drawBoard(Canvas canvas){
        painter.setColor(boardColor);
        painter.setStrokeWidth(20);

        //column lines
        for(int i = 1; i<3; i++){
           canvas.drawLine(cellSize*i, 0, cellSize*i, canvas.getWidth(), painter);
        }

        //row lines
        for(int k = 1; k<3; k++){
            canvas.drawLine(0, cellSize*k, canvas.getWidth(), cellSize*k, painter);
        }
    }

    //draws and x
    public void putX(Canvas canvas, int row, int col){
        painter.setColor(xColor);

        //draw lines of x
        canvas.drawLine((float)((col+1)*cellSize - cellSize * 0.2),
                        (float) (row*cellSize + cellSize * 0.2),
                        (float)(col*cellSize + cellSize * 0.2),
                        (float) ((row+1)*cellSize - cellSize * 0.2),
                        painter
        );

        canvas.drawLine((float)(col*cellSize + cellSize*0.2),
                        (float)(row*cellSize + cellSize*0.2),
                        (float)((col+1)*cellSize - cellSize*0.2),
                        (float)((row+1)*cellSize - cellSize*0.2),
                        painter
        );
    }

    //draws an o
    public void putO(Canvas canvas, int row, int col){
        painter.setColor(oColor);

        //draw circle
        canvas.drawOval((float)(col*cellSize + cellSize*0.2),
                        (float)(row*cellSize + cellSize*0.2),
                        (float)( (col*cellSize + cellSize) - cellSize*0.2),
                        (float)((row*cellSize + cellSize) - cellSize*0.2),
                        painter
        );
    }

    //see if tapped
    @Override
    public boolean onTouchEvent(MotionEvent event){

        //stop if game is won
        if(done){
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            int row = (int) Math.ceil(y/cellSize);
            int col = (int) Math.ceil(x/cellSize);

            //put into pieces
            //see if empty
            if(pieces[row - 1][col - 1] == E){
                numClicks++;

                //if it is xs turn
                if(turn == X){
                  pieces[row - 1][col - 1] = X;
                  if(testIfWon()){
                      message.setText("Player X Won!");
                      done = true;
                  }else {
                      if(numClicks == 9){       //cats game
                          done = true;
                          message.setText("Tie: cat's game");
                      }
                      else {
                          turn = O;
                          message.setText("Player O's turn");
                      }
                  }

                }else {
                    //is os turn
                    pieces[row - 1][col - 1] = O;
                    if(testIfWon()){
                        message.setText("Player O Won!");
                        done = true;
                    }else {
                        if(numClicks == 9){       //cats game
                            done = true;
                            message.setText("Tie: cat's game");
                        }else {
                            turn = X;
                            message.setText("Player X's turn");
                        }
                    }
                }
            }

            //redraw
            invalidate();   //runs onDraw() again

            return true;
        }

        return false;
    }

    //draws the current xs and os
    public void drawPieces(Canvas canvas){
        //see if it is marked
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                if (getPieces()[i][j] != E){
                    //see if its xs or os
                    if(getPieces()[i][j] == X){
                        putX(canvas, i, j);
                    }
                    else{
                        putO(canvas, i, j);
                    }
                }
            }
        }
    }

    //gets the xs and os
    public int[][] getPieces(){
        return pieces;
    }

    //for changing message in main activity
    public void updatableMessage(TextView message){
        this.message = message;
    }


    //GAME LOGIC HERE
    private boolean testIfWon() {
        //check for horizontals
        for (int i = 0; i < 3; i++) {
            if (pieces[i][0] == pieces[i][1] && pieces[i][0] == pieces[i][2] && pieces[i][0] !=0) {
                return true;
            }
        }
        //check for verticals
        for (int i = 0; i < 3; i++) {
            if (pieces[0][i] == pieces[1][i] && pieces[0][i] == pieces[2][i] && pieces[0][i] !=0) {
                return true;
            }
        }

        //check diagonals
        if(pieces[0][0] == pieces[1][1] && pieces[0][0] == pieces[2][2] && pieces[0][0] !=0){
            return true;
        }
        if(pieces[0][2] == pieces[1][1] && pieces[2][0] == pieces[0][2] && pieces[0][2] !=0){
            return true;
        }

        return false;
    }




}
