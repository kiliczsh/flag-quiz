// FlagQuizGame.java
// Main Activity for the Flag Quiz Game App
package com.deitel.flagquizgame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FlagQuizGame extends Activity {
    // String used when logging error messages
    private static final String TAG = "FlagQuizGame Activity";
    private int clickcount = 0;
    private Boolean firstClickTrue = false;
    private List<String> fileNameList;        // flag file names
    private List<String> quizCountriesList;   // names of countries in quiz
    private Map<String, Boolean> regionsMap;  // which regions are enabled
    private String correctAnswer;             // correct country for the current flag
    private int totalGuesses;                 // number of guesses made
    private int correctAnswers;               // number of correct guesses
    private int correctCapitalAnswers;        // number of correct capital guesses
    private int guessRows;                    // number of rows displaying choices
    private Random random;                    // random number generator
    private Handler handler;                  // used to delay loading next flag
    private Animation shakeAnimation;         // animation for incorrect guess


    private String capitals [][] = { // country-capital matches
            {"Afghanistan", "Kabul"},
            {"Albania", "Tirana"},
            {"Algeria", "Algiers"},
            {"Andorra", "Andorra la Vella"},
            {"Angola", "Luanda"},
            {"Antigua and Barbuda", "Saint John's"},
            {"Argentina", "Buenos Aires"},
            {"Armenia", "Yerevan"},
            {"Australia", "Canberra"},
            {"Austria", "Vienna"},
            {"Azerbaijan", "Baku"},
            {"The Bahamas", "Nassau"},
            {"Bahrain", "Manama"},
            {"Bangladesh", "Dhaka"},
            {"Barbados", "Bridgetown"},
            {"Belarus", "Minsk"},
            {"Belgium", "Brussels"},
            {"Belize", "Belmopan"},
            {"Benin", "Porto Novo"},
            {"Bhutan", "Thimphu"},
            {"Bolivia", "La Paz"},
            {"Bosnia and Herzegovina", "Sarajevo"},
            {"Botswana", "Gaborone"},
            {"Brazil", "Brasilia"},
            {"Brunei", "Bandar Seri Begawan"},
            {"Bulgaria", "Sofia"},
            {"Burkina Faso", "Ouagadougou"},
            {"Burundi", "Bujumbura"},
            {"Cambodia", "Phnom Penh"},
            {"Cameroon", "Yaounde"},
            {"Canada", "Ottawa"},
            {"Cape Verde", "Praia"},
            {"Central African Republic", "Bangui"},
            {"Chad", "N'Djamena"},
            {"Chile", "Santiago"},
            {"China", "Beijing"},
            {"Colombia", "Bogota"},
            {"Comoros", "Moroni"},
            {"Congo Republic of the", "Brazzaville"},
            {"Congo Democratic Republic of the", "Kinshasa"},
            {"Costa Rica", "San Jose"},
            {"Cote d'Ivoire", "Yamoussoukro"},
            {"Croatia", "Zagreb"},
            {"Cuba", "Havana"},
            {"Cyprus", "Nicosia"},
            {"Czech Republic", "Prague"},
            {"Denmark", "Copenhagen"},
            {"Djibouti", "Djibouti"},
            {"Dominica", "Roseau"},
            {"Dominican Republic", "Santo Domingo"},
            {"East Timor", "Dili"},
            {"Ecuador", "Quito"},
            {"Egypt", "Cairo"},
            {"El Salvador", "San Salvador"},
            {"Equatorial Guinea", "Malabo"},
            {"Eritrea", "Asmara"},
            {"Estonia", "Tallinn"},
            {"Ethiopia", "Addis Ababa"},
            {"Fiji", "Suva"},
            {"Finland", "Helsinki"},
            {"France", "Paris"},
            {"Gabon", "Libreville"},
            {"The Gambia", "Banjul"},
            {"Georgia", "Tbilisi"},
            {"Germany", "Berlin"},
            {"Ghana", "Accra"},
            {"Greece", "Athens"},
            {"Grenada", "Saint George's"},
            {"Guatemala", "Guatemala City"},
            {"Guinea", "Conakry"},
            {"Guinea-Bissau", "Bissau"},
            {"Guyana", "Georgetown"},
            {"Haiti", "Port au Prince"},
            {"Honduras", "Tegucigalpa"},
            {"Hungary", "Budapest"},
            {"Iceland", "Reykjavik"},
            {"India", "New Delhi"},
            {"Indonesia", "Jakarta"},
            {"Iran", "Tehran"},
            {"Iraq", "Baghdad"},
            {"Ireland", "Dublin"},
            {"Israel", "Tel Aviv"},
            {"Italy", "Rome"},
            {"Jamaica", "Kingston"},
            {"Japan", "Tokyo"},
            {"Jordan", "Amman"},
            {"Kazakhstan", "Astana"},
            {"Kenya", "Nairobi"},
            {"Kiribati", "Tarawa Atoll"},
            {"Korea North", "Pyongyang"},
            {"Korea South", "Seoul"},
            {"Kosovo", "Pristina"},
            {"Kuwait", "Kuwait City"},
            {"Kyrgyzstan", "Bishkek"},
            {"Laos", "Vientiane"},
            {"Latvia", "Riga"},
            {"Lebanon", "Beirut"},
            {"Lesotho", "Maseru"},
            {"Liberia", "Monrovia"},
            {"Libya", "Tripoli"},
            {"Liechtenstein", "Vaduz"},
            {"Lithuania", "Vilnius"},
            {"Luxembourg", "Luxembourg"},
            {"Macedonia", "Skopje"},
            {"Madagascar", "Antananarivo"},
            {"Malawi", "Lilongwe"},
            {"Malaysia", "Kuala Lumpur"},
            {"Maldives", "Male"},
            {"Mali", "Bamako"},
            {"Malta", "Valletta"},
            {"Marshall Islands", "Majuro"},
            {"Mauritania", "Nouakchott"},
            {"Mauritius", "Port Louis"},
            {"Mexico", "Mexico City"},
            {"Micronesia Federated States of", "Palikir"},
            {"Moldova", "Chisinau"},
            {"Monaco", "Monaco"},
            {"Mongolia", "Ulaanbaatar"},
            {"Montenegro", "Podgorica"},
            {"Morocco", "Rabat"},
            {"Mozambique", "Maputo"},
            {"Myanmar (Burma)", "Naypyidaw"},
            {"Namibia", "Windhoek"},
            {"Nauru", "Yaren District"},
            {"Nepal", "Kathmandu"},
            {"Netherlands", "Amsterdam"},
            {"New Zealand", "Wellington"},
            {"Nicaragua", "Managua"},
            {"Niger", "Niamey"},
            {"Nigeria", "Abuja"},
            {"Norway", "Oslo"},
            {"Oman", "Muscat"},
            {"Pakistan", "Islamabad"},
            {"Palau", "Melekeok"},
            {"Panama", "Panama City"},
            {"Papua New Guinea", "Port Moresby"},
            {"Paraguay", "Asuncion"},
            {"Peru", "Lima"},
            {"Philippines", "Manila"},
            {"Poland", "Warsaw"},
            {"Portugal", "Lisbon"},
            {"Qatar", "Doha"},
            {"Romania", "Bucharest"},
            {"Russia", "Moscow"},
            {"Rwanda", "Kigali"},
            {"Saint Kitts and Nevis", "Basseterre"},
            {"Saint Lucia", "Castries"},
            {"Saint Vincent and the Grenadines", "Kingstown"},
            {"Samoa", "Apia"},
            {"San Marino", "San Marino"},
            {"Sao Tome and Principe", "Sao Tome"},
            {"Saudi Arabia", "Riyadh"},
            {"Senegal", "Dakar"},
            {"Serbia", "Belgrade"},
            {"Seychelles", "Victoria"},
            {"Sierra Leone", "Freetown"},
            {"Singapore", "Singapore"},
            {"Slovakia", "Bratislava"},
            {"Slovenia", "Ljubljana"},
            {"Solomon Islands", "Honiara"},
            {"Somalia", "Mogadishu"},
            {"South Africa", "Cape Town"},
            {"South Sudan", "Juba"},
            {"Spain", "Madrid"},
            {"Sri Lanka", "Colombo"},
            {"Sudan", "Khartoum"},
            {"Suriname", "Paramaribo"},
            {"Swaziland", "Mbabane"},
            {"Sweden", "Stockholm"},
            {"Switzerland", "Bern"},
            {"Syria", "Damascus"},
            {"Taiwan", "Taipei"},
            {"Tajikistan", "Dushanbe"},
            {"Tanzania", "Dar es Salaam"},
            {"Thailand", "Bangkok"},
            {"Togo", "Lome"},
            {"Tonga", "Nuku'alofa"},
            {"Trinidad and Tobago", "Port of Spain"},
            {"Tunisia", "Tunis"},
            {"Turkey", "Ankara"},
            {"Turkmenistan", "Ashgabat"},
            {"Tuvalu", "Vaiaku village, Funafuti province"},
            {"Uganda", "Kampala"},
            {"Ukraine", "Kyiv"},
            {"United Arab Emirates", "Abu Dhabi"},
            {"United Kingdom", "London"},
            {"United States of America", "Washington D.C."},
            {"Uruguay", "Montevideo"},
            {"Uzbekistan", "Tashkent"},
            {"Vanuatu", "Port Villa"},
            {"Vatican City (Holy See)", "Vatican City"},
            {"Venezuela", "Caracas"},
            {"Vietnam", "Hanoi"},
            {"Yemen", "Sanaa"},
            {"Zambia", "Lusaka"},
            {"Zimbabwe", "Harare"}
    };


    private TextView answerTextView;          // displays Correct! or Incorrect!
    private TextView questionNumberTextView;  // shows current question #
    private TextView guessCountryTextView;
    private ImageView flagImageView;          // displays a flag
    private TableLayout buttonTableLayout;    // table of answer Buttons

    // called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call the superclass's method
        setContentView(R.layout.main);      // inflate the GUI

        fileNameList = new ArrayList<String>(); // list of image file names
        quizCountriesList = new ArrayList<String>(); // flags in this quiz
        regionsMap = new HashMap<String, Boolean>(); // HashMap of regions
        guessRows = 1; // default to one row of choices
        random = new Random(); // initialize the random number generator
        handler = new Handler(); // used to perform delayed operations

        // load the shake animation that's used for incorrect answers
        shakeAnimation =
                AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times

        // get array of world regions from strings.xml
        String[] regionNames =
                getResources().getStringArray(R.array.regionsList);

        // by default, countries are chosen from all regions
        for (String region : regionNames)
            regionsMap.put(region, true);

        // get references to GUI components
        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView) findViewById(R.id.flagImageView);
        buttonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        // set questionNumberTextView's text
        questionNumberTextView.setText(getResources().getString(R.string.question) + " 1 " + getResources().getString(R.string.of) + " 10");

        guessCountryTextView = (TextView) findViewById(R.id.guessCountryTextView);


        resetQuiz(); // start a new quiz
    } // end method onCreate

    // set up and start the next quiz
    private void resetQuiz() {
        // use the AssetManager to get the image flag
        // file names for only the enabled regions
        AssetManager assets = getAssets(); // get the app's AssetManager

        fileNameList.clear(); // empty the list

        try {

            Set<String> regions = regionsMap.keySet(); // get Set of regions

            // loop through each region
            for (String region : regions) {
                if (regionsMap.get(region)) // if region is enabled
                {
                    // get a list of all flag image files in this region
                    String[] paths = assets.list(region);

                    for (String path : paths)
                        fileNameList.add(path.replace(".png", ""));
                } // end if
            } // end for

        } // end try
        catch (IOException e) {
            Log.e(TAG, "Error loading image file names", e);
        } // end catch

        correctAnswers = 0; // reset the number of correct answers made

        totalGuesses = 0; // reset the total number of guesses the user made
        quizCountriesList.clear(); // clear prior list of quiz countries

        // add 10 random file names to the quizCountriesList
        int flagCounter = 1;
        int numberOfFlags = fileNameList.size(); // get number of flags

        while (flagCounter <= 10) {
            int randomIndex = random.nextInt(numberOfFlags); // random index

            // get the random file name
            String fileName = fileNameList.get(randomIndex);

            // if the region is enabled and it hasn't already been chosen
            if (!quizCountriesList.contains(fileName)) {
                quizCountriesList.add(fileName); // add the file to the list
                ++flagCounter;
            } // end if
        } // end while

        loadNextFlag(); // start the quiz by loading the first flag
    } // end method resetQuiz



    // after the user guesses a correct flag, load the next flag
    private void loadNextFlag() {
        // get file name of the next flag and remove it from the list
        String nextImageName = quizCountriesList.remove(0);
        correctAnswer = nextImageName; // update the correct answer

        answerTextView.setText(""); // clear answerTextView

        // display the number of the current question in the quiz
        questionNumberTextView.setText(
                getResources().getString(R.string.question) + " " + (correctAnswers + 1) + " " +
                        getResources().getString(R.string.of) + " 10");

        // extract the region from the next image's name
        String region =
                nextImageName.substring(0, nextImageName.indexOf('-'));

        // use AssetManager to load next image from assets folder
        AssetManager assets = getAssets(); // get app's AssetManager
        InputStream stream; // used to read in flag images

        try {
            // get an InputStream to the asset representing the next flag
            stream = assets.open(region + "/" + nextImageName + ".png");

            // load the asset as a Drawable and display on the flagImageView
            Drawable flag = Drawable.createFromStream(stream, nextImageName);
            flagImageView.setImageDrawable(flag);
        } // end try
        catch (IOException e) {
            Log.e(TAG, "Error loading " + nextImageName, e);
        } // end catch

        // clear prior answer Buttons from TableRows
        for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
            ((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();

        Collections.shuffle(fileNameList); // shuffle file names

        // put the correct answer at the end of fileNameList
        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        if(!firstClickTrue){

            // add 3, 6, or 9 answer Buttons based on the value of guessRows
            for (int row = 0; row < guessRows; row++) {
                TableRow currentTableRow = getTableRow(row);

                // place Buttons in currentTableRow
                for (int column = 0; column < 3; column++) {
                    // inflate guess_button.xml to create new Button
                    Button newGuessButton = (Button) inflater.inflate(R.layout.guess_button, null);

                    // get country name and set it as newGuessButton's text
                    String fileName = fileNameList.get((row * 3) + column);
                    newGuessButton.setText(getCountryName(fileName));

                    // register answerButtonListener to respond to button clicks
                    newGuessButton.setOnClickListener(guessButtonListener);

                    currentTableRow.addView(newGuessButton);
                } // end for
            } // end for

            // randomly replace one Button with the correct answer
            int row = random.nextInt(guessRows); // pick random row
            int column = random.nextInt(3); // pick random column
            String countryName = getCountryName(correctAnswer);
            TableRow randomTableRow = getTableRow(row); // get the TableRow
            ((Button) randomTableRow.getChildAt(column)).setText(countryName);
        }else{
            //capitalQuiz(buttonTableLayout);

            // add 3, 6, or 9 answer Buttons based on the value of guessRows
            for (int row = 0; row < guessRows; row++) {
                TableRow currentTableRow = getTableRow(row);

                // place Buttons in currentTableRow
                for (int column = 0; column < 3; column++) {
                    // inflate guess_button.xml to create new Button
                    Button newGuessButton = (Button) inflater.inflate(R.layout.guess_button, null);

                    // get country name and set it as newGuessButton's text
                    String fileName = fileNameList.get((row * 3) + column);
                    newGuessButton.setText(getCountryCapital(getCountryName(fileName)));

                    // register answerButtonListener to respond to button clicks
                    newGuessButton.setOnClickListener(capitalListener);

                    currentTableRow.addView(newGuessButton);
                } // end for
            } // end for

            // randomly replace one Button with the correct answer
            int row = random.nextInt(guessRows); // pick random row
            int column = random.nextInt(3); // pick random column
            String countryName = getCountryName(correctAnswer);
            TableRow randomTableRow = getTableRow(row); // get the TableRow
            ((Button) randomTableRow.getChildAt(column)).setText(getCountryCapital(countryName));

        }
        firstClickTrue = false;

    } // end method loadNextFlag

    // returns the specified TableRow
    private TableRow getTableRow(int row) {
        return (TableRow) buttonTableLayout.getChildAt(row);
    } // end method getTableRow

    // parses the country flag file name and returns the country name
    private String getCountryName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ');
    } // end method getCountryName


    private String getCountryCapital(String country){ // get capital of a country

        String retVal ="";
        for(int c = 0; c< capitals.length; c++){
            if(country.equalsIgnoreCase(capitals[c][0])){
                retVal= capitals[c][1];
                break;
            }else{
                retVal = "--";
            }
        }
        return retVal;
    }

    // called when the user selects an answer
    private void submitGuess(Button guessButton, boolean firstClick) {
        String guess = guessButton.getText().toString();
        String answer = getCountryName(correctAnswer);
        ++totalGuesses; // increment the number of guesses the user has made

        // if the guess is correct
        if (guess.equals(answer)) {
            ++correctAnswers; // increment the number of correct answers
            // display "Correct!" in green text
            answerTextView.setText(answer + "!");
            answerTextView.setTextColor(getResources().getColor(R.color.correct_answer));

            // TODO
            disableButtons(); // disable all answer Buttons

/********************************/
           if(firstClick){
                capitalQuiz(buttonTableLayout);
           }
/********************************/

            // if the user has correctly identified 10 flags
            if (correctAnswers == 10) {

                // create a new AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.reset_quiz); // title bar string

                // set the AlertDialog's message to display game results
                builder.setMessage(String.format("%d %s, %.02f%% %s",
                        totalGuesses, getResources().getString(R.string.guesses),
                        (1000 / (double) totalGuesses),
                        getResources().getString(R.string.correct)));

                builder.setCancelable(false);

                // add "Reset Quiz" Button
                builder.setPositiveButton(R.string.reset_quiz,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resetQuiz();
                            } // end method onClick
                        } // end anonymous inner class
                ); // end call to setPositiveButton

                // create AlertDialog from the Builder
                AlertDialog resetDialog = builder.create();
                resetDialog.show(); // display the Dialog
            } // end if
            else // answer is correct but quiz is not over
            {
                // load the next flag after a 1-second delay
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                guessCountryTextView.setText(getResources().getString(R.string.guess_country));
                                flagImageView.setVisibility(View.VISIBLE);
                                loadNextFlag();
                            }
                        }, 1000); // 1000 milliseconds for 1-second delay
            } // end else
        } // end if
        else // guess was incorrect
        {
            // play the animation
            flagImageView.startAnimation(shakeAnimation);

            // display "Incorrect!" in red
            answerTextView.setText(R.string.incorrect_answer);
            answerTextView.setTextColor(
                    getResources().getColor(R.color.incorrect_answer));
            guessButton.setEnabled(false); // disable the incorrect answer
        } // end else

    } // end method submitGuess

    // utility method that disables all answer Buttons
    private void disableButtons() {
        for (int row = 0; row < buttonTableLayout.getChildCount(); ++row) {
            TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);
            for (int i = 0; i < tableRow.getChildCount(); ++i)
                tableRow.getChildAt(i).setEnabled(false);
        } // end outer for
    } // end method disableButtons


    // create constants for each menu id
    private final int CHOICES_MENU_ID = Menu.FIRST;
    private final int REGIONS_MENU_ID = Menu.FIRST + 1;

    // called when the user accesses the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // add two options to the menu - "Choices" and "Regions"
        menu.add(Menu.NONE, CHOICES_MENU_ID, Menu.NONE, R.string.choices);
        menu.add(Menu.NONE, REGIONS_MENU_ID, Menu.NONE, R.string.regions);

        return true; // display the menu
    }  // end method onCreateOptionsMenu

    // called when the user selects an option from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch the menu id of the user-selected option
        switch (item.getItemId()) {
            case CHOICES_MENU_ID:
                // create a list of the possible numbers of answer choices
                final String[] possibleChoices =
                        getResources().getStringArray(R.array.guessesList);

                // create a new AlertDialog Builder and set its title
                AlertDialog.Builder choicesBuilder =
                        new AlertDialog.Builder(this);
                choicesBuilder.setTitle(R.string.choices);

                // add possibleChoices's items to the Dialog and set the
                // behavior when one of the items is clicked
                choicesBuilder.setItems(R.array.guessesList,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // update guessRows to match the user's choice
                                guessRows = Integer.parseInt(
                                        possibleChoices[item].toString()) / 3;
                                resetQuiz(); // reset the quiz
                            } // end method onClick
                        } // end anonymous inner class
                );  // end call to setItems

                // create an AlertDialog from the Builder
                AlertDialog choicesDialog = choicesBuilder.create();
                choicesDialog.show(); // show the Dialog
                return true;

            case REGIONS_MENU_ID:
                // get array of world regions
                final String[] regionNames =
                        regionsMap.keySet().toArray(new String[regionsMap.size()]);

                // boolean array representing whether each region is enabled
                boolean[] regionsEnabled = new boolean[regionsMap.size()];
                for (int i = 0; i < regionsEnabled.length; ++i)
                    regionsEnabled[i] = regionsMap.get(regionNames[i]);

                // create an AlertDialog Builder and set the dialog's title
                AlertDialog.Builder regionsBuilder =
                        new AlertDialog.Builder(this);
                regionsBuilder.setTitle(R.string.regions);

                // replace _ with space in region names for display purposes
                String[] displayNames = new String[regionNames.length];
                for (int i = 0; i < regionNames.length; ++i)
                    displayNames[i] = regionNames[i].replace('_', ' ');

                // add displayNames to the Dialog and set the behavior
                // when one of the items is clicked
                regionsBuilder.setMultiChoiceItems(
                        displayNames, regionsEnabled,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                // include or exclude the clicked region
                                // depending on whether or not it's checked
                                regionsMap.put(
                                        regionNames[which].toString(), isChecked);
                            } // end method onClick
                        } // end anonymous inner class
                ); // end call to setMultiChoiceItems

                // resets quiz when user presses the "Reset Quiz" Button
                regionsBuilder.setPositiveButton(R.string.reset_quiz,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int button) {
                                resetQuiz(); // reset the quiz
                            } // end method onClick
                        } // end anonymous inner class
                ); // end call to method setPositiveButton

                // create a dialog from the Builder
                AlertDialog regionsDialog = regionsBuilder.create();
                regionsDialog.show(); // display the Dialog
                return true;
        } // end switch

        return super.onOptionsItemSelected(item);
    } // end method onOptionsItemSelected

    // called when a guess Button is touched

    private OnClickListener guessButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            clickcount=clickcount+1;
            if(clickcount==1){
                firstClickTrue = true;
            }else{
                firstClickTrue = false;
            }
                submitGuess((Button) v,firstClickTrue); // pass selected Button to submitGuess
        } // end method onClick

    }; // end answerButtonListener



    private void submitCapital(Button guessCapitalButton) {

        String capitalGuess = guessCapitalButton.getText().toString();
        String correctCapital = getCountryCapital(correctAnswer);

        if(correctCapital.equalsIgnoreCase(capitalGuess)){
            ++correctCapitalAnswers;
            answerTextView.setText(capitalGuess + "!");
            answerTextView.setTextColor(getResources().getColor(R.color.correct_answer));

            // TODO
            disableButtons(); // disable all answer Buttons

        }else{
            // play the animation
            flagImageView.startAnimation(shakeAnimation);

            // display "Incorrect!" in red
            answerTextView.setText(R.string.incorrect_answer);
            answerTextView.setTextColor(
                    getResources().getColor(R.color.incorrect_answer));
            guessCapitalButton.setEnabled(false); // disable the incorrect answer
        }

    }

    private void capitalQuiz(TableLayout buttonTableLayout){

        for(int row = 0; row < guessRows; row++){
            TableRow currentTableRow = (TableRow) buttonTableLayout.getChildAt(row);
            for (int column = 0; column < 3; column++) {
                Button currentButton = (Button)currentTableRow.getChildAt(column);

                try {
                    String buttonText = currentButton.getText().toString();
                    currentButton.setText(getCountryCapital(buttonText));
                }catch (Exception e){
                    currentButton.setText("---");
                }
            }
        }

        for(int row = 0; row < guessRows; row++){
            TableRow currentTableRow = (TableRow) buttonTableLayout.getChildAt(row);
            for (int column = 0; column < 3; column++) {
                Button currentButton = (Button)currentTableRow.getChildAt(column);
                currentButton.setEnabled(true);
                currentButton.setOnClickListener(capitalListener);
            }
        }

    }


    private OnClickListener capitalListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            submitCapital((Button) v);
        } // end method onClick

    }; // end capitalButtonListener



} // end FlagQuizGame

/*************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
