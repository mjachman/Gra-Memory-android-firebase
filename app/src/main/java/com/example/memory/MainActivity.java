package com.example.memory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private CardView card0, card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13, card14, card15, card16, card17, card18, card19, card20, card21, card22, card23;
    private TextView player1TV, player2TV, p1PointsTV, p2PointsTV;
    int moves = 0;

    private String playerUniqueId = "0";

    private boolean pair;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://memory-d96f4-default-rtdb.firebaseio.com/");

    private boolean opponentFound = false;

    private String opponentUniqueId = "0";

    private String status = "matching";

    private String playerTurn = "";

    private String connectionId = "";

    ValueEventListener turnsEventListener, wonEventListener;


    Integer[] cardsArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    private int[] images = new int[24];
    private int cardNumber = 1;
    private int firstCard;
    private int secondCard;
    private int clickedFirst;
    private int clickedSecond;
    private int p1Points = 0;
    private int p2Points = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card0 = findViewById(R.id.card0);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
        card10 = findViewById(R.id.card10);
        card11 = findViewById(R.id.card11);
        card12 = findViewById(R.id.card12);
        card13 = findViewById(R.id.card13);
        card14 = findViewById(R.id.card14);
        card15 = findViewById(R.id.card15);
        card16 = findViewById(R.id.card16);
        card17 = findViewById(R.id.card17);
        card18 = findViewById(R.id.card18);
        card19 = findViewById(R.id.card19);
        card20 = findViewById(R.id.card20);
        card21 = findViewById(R.id.card21);
        card22 = findViewById(R.id.card22);
        card23 = findViewById(R.id.card23);

        card0.setTag("0");
        card1.setTag("1");
        card2.setTag("2");
        card3.setTag("3");
        card4.setTag("4");
        card5.setTag("5");
        card6.setTag("6");
        card7.setTag("7");
        card8.setTag("8");
        card9.setTag("9");
        card10.setTag("10");
        card11.setTag("11");
        card12.setTag("12");
        card13.setTag("13");
        card14.setTag("14");
        card15.setTag("15");
        card16.setTag("16");
        card17.setTag("17");
        card18.setTag("18");
        card19.setTag("19");
        card20.setTag("20");
        card21.setTag("21");
        card22.setTag("22");
        card23.setTag("23");

        images[0] = R.drawable.im1;
        images[1] = R.drawable.im2;
        images[2] = R.drawable.im3;
        images[3] = R.drawable.im4;
        images[4] = R.drawable.im5;
        images[5] = R.drawable.im6;
        images[6] = R.drawable.im7;
        images[7] = R.drawable.im8;
        images[8] = R.drawable.im9;
        images[9] = R.drawable.im10;
        images[10] = R.drawable.im11;
        images[11] = R.drawable.im12;
        images[12] = R.drawable.im1;
        images[13] = R.drawable.im2;
        images[14] = R.drawable.im3;
        images[15] = R.drawable.im4;
        images[16] = R.drawable.im5;
        images[17] = R.drawable.im6;
        images[18] = R.drawable.im7;
        images[19] = R.drawable.im8;
        images[20] = R.drawable.im9;
        images[21] = R.drawable.im10;
        images[22] = R.drawable.im11;
        images[23] = R.drawable.im12;


        player1TV = findViewById(R.id.tv_p1);
        player2TV = findViewById(R.id.tv_p2);
        p1PointsTV = findViewById(R.id.p1_points);
        p2PointsTV = findViewById(R.id.p2_points);


        final String getPlayerName = getIntent().getStringExtra("playerName");


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Oczekiwanie na przecinika");
        progressDialog.show();


        playerUniqueId = String.valueOf(System.currentTimeMillis());

        player1TV.setText(getPlayerName);


        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!opponentFound) {

                    if (snapshot.hasChildren()) {


                        for (DataSnapshot connections : snapshot.getChildren()) {

                            String conId = connections.getKey();



                            int getPlayersCount = (int) connections.getChildrenCount();

                            if (status.equals("waiting")) {


                                if (getPlayersCount == 2) {

                                    playerTurn = playerUniqueId;
                                    applyPlayerTurn();


                                    boolean playerFound = false;


                                    for (DataSnapshot players : connections.getChildren()) {

                                        String getPlayerUniqueId = players.getKey();


                                        if (getPlayerUniqueId.equals(playerUniqueId)) {
                                            playerFound = true;
                                        } else if (playerFound) {


                                            String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                            opponentUniqueId = players.getKey();


                                            player2TV.setText(getOpponentPlayerName);


                                            connectionId = conId;


                                            opponentFound = true;


                                            databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);

                                            databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);


                                            Collections.shuffle(Arrays.asList(cardsArray),new Random(Long.parseLong(conId)));



                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();

                                            }


                                            databaseReference.child("connections").removeEventListener(this);

                                        }
                                    }
                                }
                            } else {


                                if (getPlayersCount == 1) {


                                    connections.child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);


                                    for (DataSnapshot players : connections.getChildren()) {

                                        String getOpponentName = players.child("player_name").getValue(String.class);
                                        opponentUniqueId = players.getKey();


                                        playerTurn = opponentUniqueId;
                                        applyPlayerTurn();


                                        player2TV.setText(getOpponentName);


                                        connectionId = conId;
                                        opponentFound = true;


                                        databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);

                                        databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);
                                        Collections.shuffle(Arrays.asList(cardsArray),new Random(Long.parseLong(conId)));

                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        databaseReference.child("connections").removeEventListener(this);

                                        break;
                                    }
                                }
                            }
                        }


                        if (!opponentFound && !status.equals("waiting")) {

                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());

                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);

                            status = "waiting";
                        }
                    } else {


                        String connectionUniqueId = String.valueOf(System.currentTimeMillis());

                        snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);

                        status = "waiting";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turnsEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (ds.child("previous_card").getValue(Integer.class) != null && ds.child("pair?").getValue(Boolean.class) != null && ds.child("card_number").getValue(Integer.class) != null) {
                        if (ds.child("player_turn").getValue(String.class) != null) {

                            playerTurn = ds.child("player_turn").getValue(String.class);
                            applyPlayerTurn();
                        }
                        if (ds.child(opponentUniqueId).getValue(Integer.class) != null) {
                            p2Points = ds.child(opponentUniqueId).getValue(Integer.class);
                            p2PointsTV.setText("" + p2Points);

                        }

                        int id0 = -1;

                        if (ds.child("previous_card").getValue(Integer.class) != null) {
                            int getPreviousCard = ds.child("previous_card").getValue(Integer.class);
                            id0 = getResources().getIdentifier("card" + getPreviousCard, "id", getPackageName());
                        }
                        final int getBoxPosition = ds.child("current_card").getValue(Integer.class);

                        int id = getResources().getIdentifier("card" + getBoxPosition, "id", getPackageName());


                        if (ds.child("pair?").getValue(Boolean.class)) {

                            int finalId1 = id0;

                            disappear(findViewById(id));
                            disappear(findViewById(finalId1));


                        } else if (ds.child("card_number").getValue(Integer.class) == 2 && !ds.child("pair?").getValue(Boolean.class)) {
                            Handler handler = new Handler();
                            int finalId = id0;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    animate(findViewById(id), R.drawable.ic_back1);
                                    animate(findViewById(finalId), R.drawable.ic_back1);

                                }
                            }, 1000);

                        } else {
                            animate(findViewById(id), images[cardsArray[getBoxPosition]]);
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.hasChild("player_id")) {

                    String getWinPlayerId = snapshot.child("player_id").getValue(String.class);

                    final WinDialog winDialog;

                    if (getWinPlayerId.equals(playerUniqueId)) {


                        winDialog = new WinDialog(MainActivity.this, "Wygrałeś");
                    } else if (getWinPlayerId.equals(opponentUniqueId)) {

                        winDialog = new WinDialog(MainActivity.this, "Przegrałeś");
                    } else {
                        winDialog = new WinDialog(MainActivity.this, "Remis");
                    }

                    winDialog.setCancelable(false);
                    winDialog.show();

                    databaseReference.child("turns").child(connectionId).removeEventListener(turnsEventListener);
                    databaseReference.child("won").child(connectionId).removeEventListener(wonEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

    }


    public void onClick(View view) {
        int theCard = Integer.parseInt((String) view.getTag());
        CardView card = (CardView) view;
        selectCards(card, theCard, playerTurn);
    }

    private void applyPlayerTurn() {

        if (playerTurn.equals(playerUniqueId)) {
            disableEnableControls(true, findViewById(R.id.mainGrid));
            player1TV.setBackgroundColor(Color.GREEN);
            player2TV.setBackgroundColor(Color.GRAY);

        } else {
            disableEnableControls(false, findViewById(R.id.mainGrid));
            player1TV.setBackgroundColor(Color.GRAY);
            player2TV.setBackgroundColor(Color.GREEN);
        }
    }

    private void selectCards(CardView card, int theCard, String SelectedByPlayer) {
        moves++;
        pair = false;
        animate(card, images[cardsArray[theCard]]);

        databaseReference.child("turns").child(connectionId).child("move").child("pair?").setValue(pair);
        databaseReference.child("turns").child(connectionId).child("move").child("current_card").setValue(theCard);

        databaseReference.child("turns").child(connectionId).child("move").child("card_number").setValue(cardNumber);


        playerMove(card, theCard, SelectedByPlayer);
    }

    private void playerMove(CardView card, int theCard, String SelectedByPlayer) {


        if (cardNumber == 1) {

            firstCard = cardsArray[theCard];
            databaseReference.child("turns").child(connectionId).child("move").child("previous_card").setValue(theCard);

            card.setEnabled(false);
            if (firstCard >= 12) {
                firstCard = firstCard - 12;

            }

            cardNumber = 2;
            clickedFirst = theCard;

        } else if (cardNumber == 2) {
            secondCard = cardsArray[theCard];
            if (secondCard >= 12) {
                secondCard = secondCard - 12;

            }
            System.out.println("Karta2: " + secondCard);
            cardNumber = 1;
            clickedSecond = theCard;

            databaseReference.child("turns").child(connectionId).child("move").child("pair?").setValue(pair);

            databaseReference.child("turns").child(connectionId).child("move").child("card_number").setValue(cardNumber);
            databaseReference.child("turns").child(connectionId).child("move").child("current_card").setValue(theCard);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 1000);
        }
    }

    private void calculate() {
        pair = false;
        int id = getResources().getIdentifier("card" + clickedFirst, "id", getPackageName());
        int id2 = getResources().getIdentifier("card" + clickedSecond, "id", getPackageName());
        if (firstCard == secondCard) {

            findViewById(id2).setEnabled(false);

            if (playerTurn.equals(playerUniqueId)) {
                p1Points++;

                p1PointsTV.setText("" + p1Points);
            } else {
                p2Points++;

                p2PointsTV.setText("" + p2Points);
            }


            disappear(findViewById(id));
            disappear(findViewById(id2));
            pair = true;

            databaseReference.child("turns").child(connectionId).child("move").child("pair?").setValue(pair);
            databaseReference.child("turns").child(connectionId).child("move").child(playerUniqueId).setValue(p1Points);

        } else {
            //playerChange=true;
            //databaseReference.child("turns").child(connectionId).child("move").child("player_change").setValue(playerChange);
            findViewById(id).setEnabled(true);
            findViewById(id2).setEnabled(true);

            if (playerTurn.equals(playerUniqueId)) {

                playerTurn = opponentUniqueId;
                //applyPlayerTurn();

            } else {
                playerTurn = playerUniqueId;

            }
            databaseReference.child("turns").child(connectionId).child("move").child("player_turn").setValue(playerTurn);
        }
        checkEnd();


    }

    private void disappear(CardView card) {
        card.animate()
                .translationY(card.getHeight())
                .alpha(0.0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card.setVisibility(View.INVISIBLE);
                    }
                });

    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }

    private void checkEnd() {
        if (p1Points + p2Points == 12) {
            if (p1Points > p2Points) {
                databaseReference.child("won").child(connectionId).child("player_id").setValue(playerUniqueId);
            } else if (p1Points < p2Points) {
                databaseReference.child("won").child(connectionId).child("player_id").setValue(opponentUniqueId);
            } else {
                databaseReference.child("won").child(connectionId).child("player_id").setValue("0");
            }


        }


    }

    private void animate(CardView card, int img) {
        card.animate().withLayer()
                .rotationY(90)
                .setDuration(300)
                .withEndAction(
                        new Runnable() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void run() {
                                //card.setCardBackgroundColor(color);
                                card.getChildAt(0).setBackgroundResource(img);
                                // second quarter turn
                                card.setRotationY(-90);
                                card.animate().withLayer()
                                        .rotationY(0)
                                        .setDuration(300)
                                        .start();
                            }
                        }
                ).start();
    }
}