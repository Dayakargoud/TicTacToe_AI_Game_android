package com.dayakar.tictactoe;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button[][] buttons=new Button[3][3];
    Button reset;
    boolean is_player1=true;

    static int MAX = 1000;
    static int MIN = -1000;
    boolean is_playing=true;
    char player = 'x', opponent = 'o';
   private int row;
   private int col;
    int COMPUTER=0;
    int HUMAN=1;
    int whoseTurn=HUMAN;
    private int bestMove;
    private TextView wishes;
    LinearLayout myLayout;
    private TextView winning_text;
    char board[][] =
            {       {'_', '_', '_' },
                    {'_', '_', '_' },
                    {'_', '_', '_' }

            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        winning_text=(TextView)findViewById(R.id.win_info);
         wishes=(TextView)findViewById(R.id.wishes_to_user);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String button_Id="button_"+i + j;
                int res_Id=getResources().getIdentifier(button_Id,"id",getPackageName());
                buttons[i][j]=findViewById(res_Id);
                buttons[i][j].setOnClickListener(this);
            }
        }
        reset=(Button)findViewById(R.id.reset_game);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rest_Game();
                winning_text.setText("");
                winning_text.setVisibility(View.INVISIBLE);
                reset.setText("Reset");
                wishes.setText(R.string.wish);
                buttons_enable(true);
            }
        });

    }
    void buttons_enable(boolean value){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                buttons[i][j].setEnabled(value);

            }
        }
    }

    @Override
    public void onClick(View v) {
        int a=-1,b=-1;
        if(whoseTurn==HUMAN){
        if(!((Button)v).getText().toString().equals("")){
            return;
        }
        if(is_playing){
            ((Button) v).setText("o");
            ((Button) v).setTextColor(getResources().getColor(R.color.yellow));
            winner(evaluate(board));
            if(((Button)v).getId()==R.id.button_00){
                a=0;
                b=0;

            }else if(((Button)v).getId()==R.id.button_01){
                a=0;
                b=1;

            }
            else if(((Button)v).getId()==R.id.button_02){
                a=0;
                b=2;

            }
            else if(((Button)v).getId()==R.id.button_10){
                a=1;
                b=0;

            }
            else if(((Button)v).getId()==R.id.button_11){
                a=1;
                b=1;
            }
            else if(((Button)v).getId()==R.id.button_12){
                a=1;
                b=2;

            }else if(((Button)v).getId()==R.id.button_20){
                a=2;
                b=0;

            }else if(((Button)v).getId()==R.id.button_21){
                a=2;
                b=1;


            }else if(((Button)v).getId()==R.id.button_22){
                a=2;
                b=2;

            }
        whoseTurn=COMPUTER;
        system_turn(a,b);

        }}

    }
    private void rest_Game(){
        whoseTurn=HUMAN;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                buttons[i][j].setText("");
                board[i][j]='_';
            }
        }}


    private void system_turn(int a,int b){


         board[a][b]=opponent;

        if(whoseTurn==COMPUTER&&isMovesLeft(board)){
            findBestMove(board);
            board[row][col]=player;

            if(buttons[row][col].getText().toString().equals("")){
                buttons[row][col].setText("x");
                buttons[row][col].setTextColor(getResources().getColor(R.color.colorPrimary));
             }
            System.out.println("\n");
            System.out.println("Computer move is "+row+" "+col);
            showBoard(board);
            winner(evaluate(board));
            //
            whoseTurn=HUMAN;
        }else if(!isMovesLeft(board)&&evaluate(board)==0){
            show_score("Its a Tie..");

        }


    }


    boolean isMovesLeft(char board[][])

    {

        for (int i = 0; i<3; i++)

            for (int j = 0; j<3; j++)

                if (board[i][j]=='_'){

                    return true;}
        return false;

    }
   public int evaluate(char b[][])

    {

        // Checking for Rows for X or O victory.

        for (int row = 0; row<3; row++)

        {

            if (b[row][0]==b[row][1] &&

                    b[row][1]==b[row][2])

            {

                if (b[row][0]==player)

                {return +10;}

                else if (b[row][0]==opponent)

                { return -10;}

            }

        }



        // Checking for Columns for X or O victory.

        for (int col = 0; col<3; col++)

        {

            if (b[0][col]==b[1][col] &&

                    b[1][col]==b[2][col])

            {

                if (b[0][col]==player) {

                    return +10;
                }


                else if (b[0][col]==opponent){

                    return -10;}

            }

        }



        // Checking for Diagonals for X or O victory.

        if (b[0][0]==b[1][1] && b[1][1]==b[2][2])

        {

            if (b[0][0]==player){

                return +10;}

            else if (b[0][0]==opponent){

                return -10;}

        }



        if (b[0][2]==b[1][1] && b[1][1]==b[2][0])

        {

            if (b[0][2]==player)
            {
                return +10;}

            else if (b[0][2]==opponent){

                return -10;}

        }



        // Else if none of them have won then return 0
        return 0;

    }
    int minimax(char board[][], int depth, boolean isMax)

    {

        int score = evaluate(board);



        // If Maximizer has won the game return his/her

        // evaluated score

        if (score == 10)

            return score-depth;



        // If Minimizer has won the game return his/her

        // evaluated score

        if (score == -10)

            return score+depth;



        // If there are no more moves and no winner then

        // it is a tie

        if (isMovesLeft(board)==false)

            return 0;



        // If this maximizer's move

        if (isMax)

        {

            int best = -1000;



            // Traverse all cells

            for (int i = 0; i<3; i++)

            {

                for (int j = 0; j<3; j++)

                {

                    // Check if cell is empty

                    if (board[i][j]=='_')

                    {

                        // Make the move

                        board[i][j] = player;



                        // Call minimax recursively and choose

                        // the maximum value

                        best = Math.max( best,

                                minimax(board, depth+1, !isMax) );



                        // Undo the move

                        board[i][j] = '_';

                    }

                }

            }

            return best;

        }



        // If this minimizer's move

        else

        {

            int best = 1000;



            // Traverse all cells

            for (int i = 0; i<3; i++)

            {

                for (int j = 0; j<3; j++)

                {

                    // Check if cell is empty

                    if (board[i][j]=='_')

                    {

                        // Make the move

                        board[i][j] = opponent;



                        // Call minimax recursively and choose

                        // the minimum value

                        best = min(best,

                                minimax(board, depth+1, !isMax));



                        // Undo the move

                        board[i][j] = '_';

                    }

                }

            }

            return best;

        }

    }
// This will return the best possible move for the player

    private int findBestMove(char board[][])

    {

        int bestVal = -1000;

        row = -1;

        col = -1;



        // Traverse all cells, evaluate minimax function for

        // all empty cells. And return the cell with optimal

        // value.

        for (int i = 0; i<3; i++)

        {

            for (int j = 0; j<3; j++)

            {

                // Check if cell is empty

                if (board[i][j]=='_')

                {

                    // Make the move

                    board[i][j] = player;



                    // compute evaluation function for this

                    // move.
                    //int moveVal= mmx(0, 0, true, values, MIN, MAX);
                    int moveVal = minimax(board, 0, false);



                    // Undo the move

                    board[i][j] = '_';



                    // If the value of the current move is

                    // more than the best value, then update

                    // best/

                    if (moveVal > bestVal)

                    {

                        row = i;

                        col = j;

                        bestVal = moveVal;

                    }

                }

            }

        }


        //  System.out.println("The value of the best Move is : "+bestVal+"\n\n");


        return bestMove;

    }
    public static void showBoard(char board[][])
    {
        System.out.println("\n\n");

        System.out.println("\t\t\t "+ board[0][0]+" | "+
                board[0][1]+" | "+board[0][2]);
        System.out.println("\t\t\t--------------\n");
        System.out.println("\t\t\t "+ board[1][0]+" | "+
                board[1][1]+" | "+board[1][2]);
        System.out.println("\t\t\t--------------\n");
        System.out.println("\t\t\t "+ board[2][0]+" | "+
                board[2][1]+" | "+board[2][2]);

        return;
    }
    private void winner(int score){
        switch (score){
            case 10:
                show_score("You lost the game");

                break;
            case -10:
                show_score("You won the game");

                break;
            case 0:

                break;

        }

    }
    void show_score(String player){
        buttons_enable(false);
        wishes.setText(R.string.game_over);

        winning_text.setText(player);
        winning_text.setVisibility(View.VISIBLE);
        reset.setText("Play again");

    }
}
