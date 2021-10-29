package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public MyLines gameBoard;
    public Button reset;
    public TextView message;

    //constants
    private static final int E = 0;
    private static final int X = 1;
    private static final int O = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("DTB", "TESTING");

        setContentView(R.layout.activity_main);

        gameBoard = findViewById(R.id.theBoard);
        reset = findViewById(R.id.resetButton);
        message = findViewById(R.id.playerTurn);

        gameBoard.updatableMessage(message);    //give MyLines class ability to change message

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set all of the gameBoard pieces to 0
                for(int i = 0; i< 3; i++){
                    for(int j = 0; j< 3; j++){
                        gameBoard.getPieces()[i][j] = 0;
                        gameBoard.turn = X;
                        gameBoard.invalidate();
                        message.setText("Player X's turn");
                        gameBoard.done = false;
                        gameBoard.numClicks = 0;

                    }
                }
            }
        });



    }

}