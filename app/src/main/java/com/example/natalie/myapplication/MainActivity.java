package com.example.natalie.myapplication;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int ADD_CARD_REQUEST_CODE = 100;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer1)).setText(allFlashcards.get(0).getAnswer());
        }
        //click on flashcard question card to see answers
        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer1).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcard_answer2).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcard_answer3).setVisibility(View.VISIBLE);
            }
        });

        //show all answers
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            boolean isShowingAnswers =false;
            @Override
            public void onClick(View v) {
                if(isShowingAnswers){
                    findViewById(R.id.flashcard_answer1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.flashcard_answer2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.flashcard_answer3).setVisibility(View.INVISIBLE);
                    isShowingAnswers = false;
                }
                else{
                    findViewById(R.id.flashcard_answer1).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcard_answer2).setVisibility(View.VISIBLE);
                    findViewById(R.id.flashcard_answer3).setVisibility(View.VISIBLE);
                    isShowingAnswers = true;
                }

            }
        });
        //showing color based on answer choice, correct
        findViewById(R.id.flashcard_answer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer1).setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
            }
        });
        //wrong answer
        findViewById(R.id.flashcard_answer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer1).setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
                findViewById(R.id.flashcard_answer2).setBackgroundColor(getResources().getColor(R.color.my_red_color, null));
            }
        });
        //wrong answer
        findViewById(R.id.flashcard_answer3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer1).setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
                findViewById(R.id.flashcard_answer3).setBackgroundColor(getResources().getColor(R.color.my_red_color, null));
            }
        });

        //add new card
        findViewById(R.id.addCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
            }
        });

        //next button
        findViewById(R.id.nextCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;
                }

                // set the question and answer TextViews with data from the database
                ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                ((TextView) findViewById(R.id.flashcard_answer1)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_CARD_REQUEST_CODE && data != null) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            String question = data.getExtras().getString("question"); // 'string1' needs to match the key we used when we put the string in the Intent
            String answer = data.getExtras().getString("answer");
            TextView changeQuestion = (TextView) findViewById(R.id.flashcard_question);
            changeQuestion.setText(question);
            TextView changeAnswer = (TextView) findViewById(R.id.flashcard_answer1);
            changeAnswer.setText(answer);
            findViewById(R.id.flashcard_answer2).setVisibility(View.INVISIBLE);
            findViewById(R.id.flashcard_answer3).setVisibility(View.INVISIBLE);
            flashcardDatabase.insertCard(new Flashcard(question, answer));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }
}
