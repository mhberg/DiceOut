//-----------------------------------------------
// Obligatorisk Opgave 4                        |
// Author: Mads Heilberg                        |
// Email: mads.heilberg@gmail.com               |
// Notes: Added a menu option to reset          |
//-----------------------------------------------
package mh.diceout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Variables for game score and individual die values
    private int score, die1, die2, die3;

    //ArrayList declarations for dice (hold dice values) and dice images (holds the images)
    private ArrayList<Integer> dice;
    private ArrayList<ImageView> diceImageViews;

    //Resource object declarations
    private TextView rollResult, scoreText;
    private ImageView die1Image, die2Image, die3Image;

    //Random object declaration
    private Random ran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice(view);
            }
        });

        //Initialization of instances
        score = 0;
        ran = new Random();
        dice = new ArrayList<>();
        diceImageViews = new ArrayList<>();

        //Initializes resource references
        rollResult = (TextView) findViewById(R.id.rollResult);
        scoreText = (TextView) findViewById(R.id.scoreText);
        die1Image = (ImageView) findViewById(R.id.die1Image);
        die2Image = (ImageView) findViewById(R.id.die2Image);
        die3Image = (ImageView) findViewById(R.id.die3Image);

        //Load ImageViews into ArrayList
        diceImageViews.add(die1Image);
        diceImageViews.add(die2Image);
        diceImageViews.add(die3Image);

        //Initial greeting
        Toast.makeText(getApplicationContext(), "Welcome to DiceOut!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method rolls all three dice, sorts them in ArrayList dice, loops through a dynamic image
     * chooser, matching it with the roll of each dice. Lastly, rollDice use the rolls to define
     * points awarded to the player.
     * @param v view from OnClick (see floating button)
     */
    public void rollDice(View v){
        //dice rolls and set dice values into ArrayList
        die1 = ran.nextInt(6) + 1;
        die2 = ran.nextInt(6) + 1;
        die3 = ran.nextInt(6) + 1;

        dice.clear();
        dice.add(die1);
        dice.add(die2);
        dice.add(die3);

        for (int dieOfSet = 0; dieOfSet < 3; dieOfSet++){
            String imageName = "die_" + dice.get(dieOfSet) + ".png";
            try {
                InputStream stream = getAssets().open(imageName);
                Drawable image = Drawable.createFromStream(stream, null);
                diceImageViews.get(dieOfSet).setImageDrawable(image);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        //Roll message defined by roll condition
        String msg;

        if (die1 == die2 && die1 == die3){
            int scoreDelta = die1 * 100;
            msg = "You rolled a triple " + die1 + "! You score " + scoreDelta + " points!";
            score += scoreDelta;
        } else if (die1 == die2 || die1 == die3 || die2 == die3){
            msg = "You rolled a double for 50 points!";
            score += 50;
        } else {
            msg = "You didn't score this roll. Try again!";
        }

        //update rollResult
        rollResult.setText(msg);
        scoreText.setText("Score: " + score);
    }

    /**
     * This method resets score and updates the views to follow
     */
    public void resetGame(){
        rollResult.setText("You're score has been reset! Play again?");
        score = 0;
        scoreText.setText("");

        //draws new images, as in rolLDice
        for (ImageView imageView : diceImageViews){
            try {
                imageView.setImageDrawable(Drawable.createFromStream(
                        getAssets().open("die_1.png"), null));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            resetGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}