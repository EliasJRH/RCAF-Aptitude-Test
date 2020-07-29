package rafapttest;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class RAFAptTest extends JFrame implements KeyListener {

    public int lives = 3; //lives counter
    public Color blueBG = new Color(2, 1, 118); //colors matching the original game
    public Color greyBG = new Color(192, 192, 192);
    public JPanel ULWordsBox = new JPanel(); //the box to hold the random strings of words
    public JPanel URWordsBox = new JPanel();
    public JPanel LLWordsBox = new JPanel();
    public JPanel LRWordsBox = new JPanel();
    public JPanel blankWords = new JPanel();
    public JLabel ULWords = new JLabel(""); // labels to hold random words
    public JLabel URWords = new JLabel("");
    public JLabel LLWords = new JLabel("");
    public JLabel LRWords = new JLabel("");
    public JLabel blankWordsText = new JLabel(""); //label to hold words at the top of the frame
    public JProgressBar wordsTimeLeft = new JProgressBar(0, 13); //progress bar timer for the memorization game
    public String stringToMatch = ""; //string for the correct word
    public JPanel ABox = new JPanel(); //the boxes for the corners of the word boxes 
    public JPanel BBox = new JPanel();
    public JPanel CBox = new JPanel();
    public JPanel DBox = new JPanel();
    public JLabel A = new JLabel("A"); //the labels for the corners of the word boxes
    public JLabel B = new JLabel("B");
    public JLabel C = new JLabel("C");
    public JLabel D = new JLabel("D");
    public JPanel middleColorBox = new JPanel(); //panels and hitboxes for the middle color box
    public JPanel redRectangle = new JPanel();
    public JPanel yellowRectangle = new JPanel();
    public JPanel greenRectangle = new JPanel();
    public Rectangle redRectangleHB = new Rectangle();
    public Rectangle yellowRectangleHB = new Rectangle();
    public Rectangle greenRectangleHB = new Rectangle();
    public Rectangle endRectangle = new Rectangle();
    public JPanel[] objs = new JPanel[13]; //panels and hit boxes for the objects for the middle color box game
    public Rectangle[] objsRects = new Rectangle[13];
    public boolean[] objMoving = new boolean[13]; //booleans to check whether the object is moving
    public JPanel mathBox = new JPanel(); //panels and labels for the math game
    public JLabel mathText = new JLabel(""); // 
    public JProgressBar mathTimeLeft = new JProgressBar(0, 13); //progress bar timer for the math game
    public JPanel timePanel = new JPanel(); //panel and label for time and lives counter
    public JLabel timeText = new JLabel("Time: 00.00.0000");
    public JLabel livesText = new JLabel("Lives: " + lives);
    public static int mins = 0, secs = 0, msecs = 0; //int and strings to hold the minute second and millisecond timer values
    public static String Smins = "", Ssecs = "", Smsecs = "";
    public Border grayLine = BorderFactory.createLineBorder(Color.gray); //creates a black border with border factory
    public Border whiteLine = BorderFactory.createLineBorder(Color.WHITE); //creates a white border
    public GridBagLayout centerLayout = new GridBagLayout(); //layout that sets text at center of screen
    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //array of letters for the word game
    public Timer t = new Timer(); //timer to run games simotaneously
    public TimerTask memorizeLetters = new TimerTask() {
        public void run() {
            if (memorizationRunning) { //if the letters have been presented
                memoSecondsRun++; //add one to a timer
                if (memoSecondsRun == 5000) { //once the timer reaces 5 seconds
                    ULWords.setText(""); //it hides all the words 
                    URWords.setText("");
                    LLWords.setText("");
                    LRWords.setText("");
                    blankWordsText.setText(stringToMatch); //shows the correct word and waits for the user to match
                    stringToMatch = ""; //clears the stringToMatch
                    memoSecondsRun = 0; //resets the time of the memorization game
                    memorizationRunning = false; //stops this timer
                    memoTimerCountdown = true; //starts the other timer
                }
            }

        }
    }; //timer task for the letter memorization challenge
    public int memoProgressBarSecs = 13, mathProgressBarSecs = 13; //integer represeting 13 seconds on the timer for the memorization game and the math game
    public TimerTask memorizationTimer = new TimerTask() {
        public void run() {
            if (memoTimerCountdown) { //while the countdown is activated
                wordsTimeLeft.setString("Time Left: " + memoProgressBarSecs + " Seconds"); //a string appears in the progress bar telling the user how much time they have left
                memoSecondsRun++; //constantly increasing the number of seconds by 1 millisecond
                if (memoSecondsRun == 1000) { //every second the timer sets back to 0 and 1 second is taken off the timer
                    memoSecondsRun = 0;
                    memoProgressBarSecs--;
                    wordsTimeLeft.setValue(wordsTimeLeft.getValue() - 1);
                }
                if (memoProgressBarSecs == 0) { //if the countdown reaches 0 it acts as if the user got a wrong answer
                    memoButtonClick = true; //acts as if a wrong button was clicked
                    memoTimerCountdown = false; //stops this timer
                    wordsTimeLeft.setString("Out of time"); //says that the user ran out of the time
                    blankWordsText.setForeground(Color.RED); //makes the colors red 
                    memoSecondsRun = 0; //resets the seconds for this game's timer
                    memoProgressBarSecs = 13; //resets the seconds of the progress bar
                    lives--; //takes away a life 
                }
            }
        }
    }; //timer task for the countdown when the words question pops up
    public TimerTask displayMemorizationAnswer = new TimerTask() {
        public void run() {
            if (memoButtonClick) { //if the user has made a guess
                memoSecondsRun++; //it displays whether the user was correct or not for 2 seconds 
                if (memoSecondsRun == 2000) { //then it resets the upper middle box
                    memoSecondsRun = 0;
                    blankWordsText.setText("");
                    blankWordsText.setForeground(Color.BLACK);
                    memorizationGame();
                    memoButtonClick = false;
                    memorizationRunning = true;
                }
            }
        }
    }; //timer task that runs after the user makes a choice for the letter game
    public TimerTask moveObjs = new TimerTask() {
        public void run() {
            for (int i = 0; i < objMoving.length; i++) { //for each object
                if (objMoving[i]) { //if it has been selected to move
                    objs[i].setLocation((objs[i].getX() + 2), objs[i].getY());
                    objsRects[i].setLocation((objs[i].getX() + 2), objs[i].getY());
                    //the panel and it's rectangle moves 
                }
                if (objsRects[i].intersects(endRectangle)) { //if the object intersects with the 
                    objs[i].setLocation(270, objs[i].getY()); //the object and it's hitbox goes back to it's original position
                    objsRects[i].setLocation(270, objs[i].getY());
                    lives--; //one life is lost
                }
                //this section checks the color of the object with whether it's intersecting with a correct color box
                if (objs[i].getBackground().getRGB() == Color.RED.getRGB() && objsRects[i].intersects(redRectangleHB)) {
                    redColliding[i] = true; //if it is, then it's corresponding color variable for that object gets set to true
                    //this will allow hit to be registered as a proper hit in the key listener
                } else {
                    redColliding[i] = false;
                }
                if (objs[i].getBackground().getRGB() == Color.YELLOW.getRGB() && objsRects[i].intersects(yellowRectangleHB)) {
                    yellowColliding[i] = true;
                } else {
                    yellowColliding[i] = false;
                }
                if (objs[i].getBackground().getRGB() == Color.GREEN.getRGB() && objsRects[i].intersects(greenRectangleHB)) {
                    greenColliding[i] = true;
                } else {
                    greenColliding[i] = false;
                }

            }
        }
    }; //timer task that moves the squares based on whether or not they have been selected to move
    public TimerTask setObjsToMove = new TimerTask() {
        public void run() {
            int rndMover = new Random().nextInt(objMoving.length); //randomly picks and object to move
            while (objMoving[rndMover]) { //if the object is already moving, it picks a new object to move
                rndMover = new Random().nextInt(objMoving.length);
            }
            objMoving[rndMover] = true; //sets the object moving to true
            objs[rndMover].setVisible(true);
            int rndMoverColor = new Random().nextInt(3);//picks a random color to change the object to
            switch (rndMoverColor) {
                case 0: //0 = red, 1 = yellow, 2 = green
                    objs[rndMover].setBackground(Color.RED);
                    break;
                case 1:
                    objs[rndMover].setBackground(Color.YELLOW);
                    break;
                case 2:
                    objs[rndMover].setBackground(Color.GREEN);
                    break;
            }
        }
    }; //timer task that makes the objects for the color game move
    public TimerTask counter = new TimerTask() {
        public void run() {
            livesText.setText("Lives: " + lives); //constantly updating the lives counter
            if (lives == 0) { //if the lives counter reaches 0 the game is over
                JOptionPane.showMessageDialog(null, "You have run of out lives \n"
                        + "You lasted for " + Smins + "." + Ssecs + "." + Smsecs); //tells the user they ran out of lives and how long they lasted for
                System.exit(0); //exits the game
            }
            msecs++; //the timer adds one to milliseconds each time it's run (every 1 millisecond)
            if (msecs == 1000) { //if there are a thousand milliseconds, convert it to one second
                secs++;
                msecs = 0000;
            }
            if (secs == 60) { //60 seconds to 1 minute
                mins++;
                secs = 00;
            }
            Smins = String.valueOf(mins); //parse all the strings
            Ssecs = String.valueOf(secs);
            Smsecs = String.valueOf(msecs);
            if (mins < 10) { //chaning all the text
                Smins = "0" + mins;
            }
            if (secs < 10) {
                Ssecs = "0" + secs;
            }
            if (msecs < 10) {
                Smsecs = "00" + msecs;
            } else if (msecs >= 10 && msecs <= 99) {
                Smsecs = "0" + msecs;
            }
            timeText.setText("Time: " + Smins + "." + Ssecs + "." + Smsecs); //changed the text of the on screen timer

        }
    }; //Timer task for the game timer\
    public TimerTask mathAnswerTimer = new TimerTask() {
        public void run() {
            if (mathAnswerPending) { //if a math question has been posted
                mathSeconds++; //the mathSeconds increases by 1
                mathTimeLeft.setString("Time Left: " + mathProgressBarSecs + " Seconds"); //constantly updating the time the user has left
                if (mathSeconds == 1000) { //if 1 second has fully passed
                    mathSeconds = 0; //resets the time so it can register one second again
                    mathProgressBarSecs--; //takes one away from the amount of time left
                    mathTimeLeft.setValue(mathTimeLeft.getValue() - 1); //decreases the progress bar
                }
                if (mathProgressBarSecs == 0) { //if the user runs out of time for the math game
                    mathProgressBarSecs = 13; //resets the timer
                    mathSeconds = 0; //resets the seconds
                    mathAnswerPending = false; //starts the timer to display the correct answer
                    userMathAnsS = ""; //resets the user math answer string
                    mathText.setForeground(Color.RED); //sets the background color to red
                    lives--; //takes away a life
                }
                if (correctMathAns == userMathAns) { //if the user got the answer correct
                    mathSeconds = 0; //does everything as if the user got the answer wrong except taking away a life and changes the text to green
                    userMathAnsS = "";
                    mathProgressBarSecs = 13;
                    mathText.setForeground(Color.GREEN);
                    mathAnswerPending = false;
                }
            }
        }
    }; //timer task to handle the math game
    public TimerTask mathAnswerDisplay = new TimerTask() {
        public void run() {
            if (mathAnswerPending == false) { //if the answer boolean is false
                mathSeconds++;
                mathText.setText(mathTextString + correctMathAns);
                if (mathSeconds == 2000) { //displays the correct answer for two seconds
                    mathSeconds = 0; //then resets the game
                    mathGame();
                }
            }
        }
    }; //timer task that displays the answer of the math game for two seconds then starts it again
    public int memoSecondsRun = 0; //seconds run for the memorization game
    public boolean memorizationRunning = true, memoButtonClick = false, memoTimerCountdown = false; //booleans that handle the memorization game
    public boolean redColliding[] = new boolean[13]; //booleans that handle the color game
    public boolean yellowColliding[] = new boolean[13];
    public boolean greenColliding[] = new boolean[13];
    int correct; //integer representing the correct choice for the word game
    int correctMathAns, userMathAns; //integers for the math portion of the game
    int mathSeconds = 0; //seconds timer for the math games
    String userMathAnsS = "", mathTextString = ""; //strings for the math game
    boolean mathAnswerPending = false; //boolean for the math game

    public static void main(String[] args) {
        new RAFAptTest(); //creates new instance of the game
    }

    public RAFAptTest() {
        createFrame(); //creates the frame 
        JOptionPane.showMessageDialog(null, "Welcome to the RCAF Aptitude Test \n"
                + "The test will start after closing this window \n"
                + "Good luck.");
        requestFocus(); //requests focus for the frame
        addKeyListener(this); //adds the key listener
        memorizationGame(); //starts the memorization game
        mathGame(); //starts the math game
        t.scheduleAtFixedRate(counter, 0, 1); //schedules timer for the actual in game timer
        t.scheduleAtFixedRate(memorizeLetters, 0, 1); //schedules timers for memorization game
        t.scheduleAtFixedRate(displayMemorizationAnswer, 0, 1);
        t.scheduleAtFixedRate(memorizationTimer, 0, 1);
        t.scheduleAtFixedRate(setObjsToMove, 0, 4500); //timers for the middle coolor game
        t.scheduleAtFixedRate(moveObjs, 0, 100);
        t.scheduleAtFixedRate(mathAnswerTimer, 0, 1); //timers for math game
        t.scheduleAtFixedRate(mathAnswerDisplay, 0, 1);
    } //creates the game

    public void createFrame() {
        setSize(1000, 700); //sets size of frame
        setTitle("RAF Aptitude Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //sets exit operation
        setResizable(false); //makes it unresizable 
        setLocationRelativeTo(null); //sets the frame at the center of the screen
        getContentPane().setBackground(blueBG); //gives the frame the blue background
        setVisible(true); //makes it visible
        setLayout(null); //gives it a null layout   
        add(timeText); //the time and lives indicators
        add(livesText);
        add(timePanel);
        add(ABox); //adds the small letter indication boxes
        add(BBox);
        add(CBox);
        add(DBox);
        for (int i = 0; i < objs.length; i++) { //instatiates te 5 squares for the middle black box and adds them to the frame
            objs[i] = new JPanel();
            objsRects[i] = new Rectangle();
            objMoving[i] = false;
            add(objs[i]);
        }
        add(redRectangle); //adds the colored rectangles
        add(yellowRectangle);
        add(greenRectangle);
        add(ULWordsBox); //adds all the jpanels to the frame
        add(URWordsBox);
        add(LLWordsBox);
        add(LRWordsBox);
        add(blankWords);
        add(wordsTimeLeft);
        add(middleColorBox); //the middle black box
        add(mathBox);  //the grey math box on the bottom
        add(mathTimeLeft);
        ULWordsBox.setSize(240, 120); //configures upper left box
        ULWordsBox.setLocation(0, 0);
        ULWordsBox.setBackground(greyBG);
        ULWordsBox.setLayout(centerLayout);
        URWordsBox.setSize(240, 120); //configures upper right box
        URWordsBox.setLocation(760, 0);
        URWordsBox.setBackground(greyBG);
        URWordsBox.setLayout(centerLayout);
        LLWordsBox.setSize(240, 120); //configures lower left box
        LLWordsBox.setLocation(0, 540);
        LLWordsBox.setBackground(greyBG);
        LLWordsBox.setLayout(centerLayout);
        LRWordsBox.setSize(240, 120); //configures lower right box
        LRWordsBox.setLocation(760, 540);
        LRWordsBox.setBackground(greyBG);
        LRWordsBox.setLayout(centerLayout);
        blankWords.setSize(274, 115); //configure blank words box
        blankWords.setOpaque(false);
        blankWords.setBorder(grayLine);
        blankWords.setLocation(363, 25);
        blankWords.setLayout(centerLayout);
        blankWordsText.setForeground(Color.BLACK);
        ULWords.setFont(new Font("Georgia", Font.BOLD, 36)); //changes the fonts of the jlabels
        URWords.setFont(new Font("Georgia", Font.BOLD, 36));
        LLWords.setFont(new Font("Georgia", Font.BOLD, 36));
        LRWords.setFont(new Font("Georgia", Font.BOLD, 36));
        blankWordsText.setFont(new Font("Georgia", Font.BOLD, 36));
        ULWordsBox.add(ULWords); //adds labels to panel
        URWordsBox.add(URWords);
        LLWordsBox.add(LLWords);
        LRWordsBox.add(LRWords);
        blankWords.add(blankWordsText);
        wordsTimeLeft.setSize(300, 25); //configures word game progress bar
        wordsTimeLeft.setLocation(350, 150);
        wordsTimeLeft.setValue(13);
        wordsTimeLeft.setStringPainted(true);
        wordsTimeLeft.setForeground(Color.ORANGE);
        wordsTimeLeft.setString("Memorize the letters");
        ABox.setBackground(Color.BLACK); //configuring the letter indication boxes
        ABox.setSize(15, 15);
        ABox.setLocation(220, 100);
        ABox.setLayout(centerLayout);
        A.setForeground(Color.WHITE);
        ABox.add(A);
        BBox.setBackground(Color.BLACK);
        BBox.setSize(15, 15);
        BBox.setLocation(770, 100);
        BBox.setLayout(centerLayout);
        B.setForeground(Color.WHITE);
        BBox.add(B);
        CBox.setBackground(Color.BLACK);
        CBox.setSize(15, 15);
        CBox.setLocation(220, 545);
        CBox.setLayout(centerLayout);
        C.setForeground(Color.WHITE);
        CBox.add(C);
        DBox.setBackground(Color.BLACK);
        DBox.setSize(15, 15);
        DBox.setLocation(770, 545);
        DBox.setLayout(centerLayout);
        D.setForeground(Color.WHITE);
        DBox.add(D);
        mathBox.setSize(274, 115); //configures the mathbox at the botom of the screen
        mathBox.setBackground(Color.GRAY);
        mathBox.setLocation(363, 500);
        mathBox.setLayout(centerLayout);
        mathBox.setBorder(whiteLine);
        mathText.setFont(new Font("Georgia", Font.BOLD, 36));
        mathText.setForeground(Color.BLACK);
        mathBox.add(mathText);
        mathTimeLeft.setSize(300, 25); //configures the progress bar for the math game
        mathTimeLeft.setLocation(350, 630);
        mathTimeLeft.setValue(13);
        mathTimeLeft.setStringPainted(true);
        mathTimeLeft.setForeground(Color.ORANGE);
        mathTimeLeft.setString("Answer the math question");
        middleColorBox.setSize(460, 270); //configures the middle black box with the red green and yellow stripes
        middleColorBox.setBackground(Color.BLACK);
        middleColorBox.setLocation(265, 190);
        middleColorBox.setBorder(whiteLine);
        redRectangle.setSize(65, 268);
        redRectangle.setBackground(Color.RED);
        redRectangle.setLocation(500, 191);
        redRectangleHB.setSize(65, 268);
        redRectangleHB.setLocation(500, 191);
        yellowRectangle.setSize(65, 268);
        yellowRectangle.setBackground(Color.YELLOW);
        yellowRectangle.setLocation(565, 191);
        yellowRectangleHB.setSize(65, 268);
        yellowRectangleHB.setLocation(565, 191);
        greenRectangle.setSize(65, 268);
        greenRectangle.setBackground(Color.GREEN);
        greenRectangle.setLocation(630, 191);
        greenRectangleHB.setSize(65, 268);
        greenRectangleHB.setLocation(630, 191);
        endRectangle.setSize(29, 268);
        endRectangle.setLocation(715, 191);
        for (int i = 0; i < objs.length; i++) { //initializes and configures the objects moving
            objs[i].setSize(10, 10);
            objs[i].setBackground(Color.RED);
            objs[i].setLocation(270, (200 + (i * 20)));
            objs[i].setVisible(false);
            objsRects[i].setSize(10, 10);
            objsRects[i].setLocation(270, (200 + (i + 20)));
        }
        timePanel.setLocation(825, 200); //configures the panel holding information about time and lives remianing
        timePanel.setSize(100, 100);
        timePanel.setOpaque(false);
        timePanel.add(timeText);
        timeText.setLocation(0, 0);
        timeText.setForeground(Color.WHITE);
        livesText.setForeground(Color.WHITE);
        timePanel.add(livesText);
        setFocusable(true); //makes the frame focusable
    } //method to create the frame

    public void memorizationGame() {
        wordsTimeLeft.setString("Memorize the letters"); //resets the timer bar
        wordsTimeLeft.setValue(13);
        int rnd; //random integer
        int stringLength = (int) (Math.random() * ((7 - 5) + 1) + 5); //random string length from 5-7
        String exString1 = "", exString2 = "", exString3 = ""; //four strings one is correct the others are one letter off
        for (int i = 0; i < stringLength; i++) { //for loop that generates the correct string from an array of letters 
            rnd = new Random().nextInt(letters.length);
            stringToMatch += letters[rnd];
        }
        rnd = new Random().nextInt(stringToMatch.length()); //creates a new random int, that will be the letter that is changed
        ArrayList<String> otherLetters = new ArrayList<String>(); //array list of letters that aren't the ones in the random sport selected 
        for (int i = 0; i < letters.length; i++) { //gets the list of all the other random letters 
            if (letters[i].charAt(0) != stringToMatch.charAt(rnd)) {
                otherLetters.add(letters[i]);
            }
        }
        for (int i = 0; i < stringToMatch.length(); i++) { //creates the other strings with the random letter changed
            if (i != rnd) { //if i is not the random int selected to be changed
                exString1 += String.valueOf(stringToMatch.charAt(i)); //copies the string
                exString2 += String.valueOf(stringToMatch.charAt(i));
                exString3 += String.valueOf(stringToMatch.charAt(i));
            } else { //otherwise
                rnd = new Random().nextInt(otherLetters.size()); //gets a random integer
                exString1 += otherLetters.get(rnd); //selectes a random letter from the list of extra letters and adds it in it's place
                otherLetters.remove(rnd); //removes that letter
                //repeated for the 2 other extra strings
                rnd = new Random().nextInt(otherLetters.size());
                exString2 += otherLetters.get(rnd);
                otherLetters.remove(rnd);
                rnd = new Random().nextInt(otherLetters.size());
                exString3 += otherLetters.get(rnd);
                otherLetters.remove(rnd);
            }
        }
        ArrayList<Integer> option = new ArrayList<Integer>(); //makes a array list that contains 0,1,2,3
        for (int i = 0; i < 4; i++) {
            option.add(i);
        }
        for (int i = 0; i < 4; i++) { //for loop that places a random sequence of the words in a random sequence of the boxes                         
            rnd = option.get(new Random().nextInt(option.size())); // generates a random number from the array list 
            //that random number dictates which word gets placed into which box
            switch (i) { //UL box
                case 0:
                    switch (rnd) {
                        case 0: //string to match
                            ULWords.setText(stringToMatch);
                            correct = 0;
                            break;
                        case 1:
                            ULWords.setText(exString1);
                            break;
                        case 2:
                            ULWords.setText(exString2);
                            break;
                        case 3:
                            ULWords.setText(exString3);
                            break;
                    }
                    option.remove(new Integer(rnd));
                    break;
                case 1: //UR box
                    switch (rnd) {
                        case 0: //string to match
                            URWords.setText(stringToMatch);
                            correct = 1;
                            break;
                        case 1:
                            URWords.setText(exString1);
                            break;
                        case 2:
                            URWords.setText(exString2);
                            break;
                        case 3:
                            URWords.setText(exString3);
                            break;
                    }
                    option.remove(new Integer(rnd));
                    break;
                case 2: //LL box
                    switch (rnd) {
                        case 0: //string to match
                            LLWords.setText(stringToMatch);
                            correct = 2;
                            break;
                        case 1:
                            LLWords.setText(exString1);
                            break;
                        case 2:
                            LLWords.setText(exString2);
                            break;
                        case 3:
                            LLWords.setText(exString3);
                            break;
                    }
                    option.remove(new Integer(rnd));
                    break;
                case 3: //LR box
                    switch (rnd) {
                        case 0: //string to match
                            LRWords.setText(stringToMatch);
                            correct = 3;
                            break;
                        case 1:
                            LRWords.setText(exString1);
                            break;
                        case 2:
                            LRWords.setText(exString2);
                            break;
                        case 3:
                            LRWords.setText(exString3);
                            break;
                    }
                    option.remove(new Integer(rnd));
                    break;
            }
        }
        exString1 = ""; //the extra strings are cleared
        exString2 = "";
        exString3 = "";
    } //method that sets up the word memorization game

    public void mathGame() {
        mathText.setForeground(Color.BLACK);
        int maxMult = 13, maxAddSub = 100; //setting up the max numbers to be used in the math game
        int num1 = 0, num2 = 0; //initializes two blank number variables
        int questionChoice = new Random().nextInt(3); //generates one of three possible question types, multiplication addition subtraction
        switch (questionChoice) {
            case 0: //question will be an addition question
                num1 = new Random().nextInt(maxAddSub); //generates random number in the bounds of the addition option
                num2 = new Random().nextInt(maxAddSub);
                correctMathAns = num1 + num2; //gets the correct answer
                mathTextString = num1 + " + " + num2 + " = "; //prepares and displays the text
                mathText.setText(mathTextString);
                break;
            case 1: //question will be a subtraction question
                num1 = new Random().nextInt(maxAddSub); //the subtraction game always has the greater number has the first number to avoid negatives
                num2 = new Random().nextInt(maxAddSub);
                correctMathAns = Math.max(num1, num2) - Math.min(num1, num2);
                mathTextString = Math.max(num1, num2) + " - " + Math.min(num1, num2) + " = ";
                mathText.setText(mathTextString);
                break;
            case 2://question will be a multiplication question
                num1 = new Random().nextInt(maxMult); //generates random number in the bounds of the multiplication option
                num2 = new Random().nextInt(maxMult);
                correctMathAns = num1 * num2;
                mathTextString = num1 + " x " + num2 + " = ";
                mathText.setText(mathTextString);
                break;
        }
        mathAnswerPending = true; //starts the math timer
        mathTimeLeft.setValue(13); //resets the math progress bar
    } //method that sets up the math game

    @Override
    public void keyTyped(KeyEvent arg0) {
    } //un used

    @Override
    public void keyPressed(KeyEvent arg0) {
        int e = arg0.getKeyCode(); //gets the key code of the key that was pressed
        /////////////////////////////////////////////////////////
        ///////////Key Presses for the memorization game/////////
        /////////////////////////////////////////////////////////
        if (memorizationRunning == false) { //if the user is in a time where they can select a word
            if (e == KeyEvent.VK_A && correct == 0) { //if statements get which key they selected and check if it's the right choice
                //if it is, it sets the color of the words to green
                blankWordsText.setForeground(Color.GREEN);
                wordsTimeLeft.setString("Correct");
                memoButtonClick = true;
                memoTimerCountdown = false; //stops the countdown timer
            } else if (e == KeyEvent.VK_A && correct != 0) {
                //if it's not, it sets the color of the words to red and one life is lost
                blankWordsText.setForeground(Color.RED);
                wordsTimeLeft.setString("Incorrect");
                memoButtonClick = true;
                memoTimerCountdown = false; //stop the countdown timer
                lives--;
            }
            if (e == KeyEvent.VK_B && correct == 1) {
                blankWordsText.setForeground(Color.GREEN);
                wordsTimeLeft.setString("Correct");
                memoButtonClick = true;
                memoTimerCountdown = false;
            } else if (e == KeyEvent.VK_B && correct != 1) {
                blankWordsText.setForeground(Color.RED);
                wordsTimeLeft.setString("Incorrect");
                memoButtonClick = true;
                memoTimerCountdown = false;
                lives--;
            }
            if (e == KeyEvent.VK_C && correct == 2) {
                blankWordsText.setForeground(Color.GREEN);
                wordsTimeLeft.setString("Correct");
                memoButtonClick = true;
                memoTimerCountdown = false;
            } else if (e == KeyEvent.VK_C && correct != 2) {
                blankWordsText.setForeground(Color.RED);
                wordsTimeLeft.setString("Incorrect");
                memoButtonClick = true;
                memoTimerCountdown = false;
                lives--;
            }
            if (e == KeyEvent.VK_D && correct == 3) {
                blankWordsText.setForeground(Color.GREEN);
                wordsTimeLeft.setString("Correct");
                memoButtonClick = true;
                memoTimerCountdown = false;
            } else if (e == KeyEvent.VK_D && correct != 3) {
                blankWordsText.setForeground(Color.RED);
                wordsTimeLeft.setString("Incorrect");
                memoButtonClick = true;
                memoTimerCountdown = false;
                lives--;
            }
        }
        /////////////////////////////////////////////////////////
        ///////////Key Presses for the color game///////////////
        /////////////////////////////////////////////////////////
        if (e == KeyEvent.VK_R) {
            boolean checkingRed = false; //a check to make sure that something actually registers
            for (int i = 0; i < redColliding.length; i++) { //for loop that checks every possible object if it's colliding
                if (redColliding[i]) { //if it's true
                    objMoving[i] = false; //makes the object stop moving
                    objs[i].setVisible(false); //makes it invisible
                    objs[i].setLocation(270, objs[i].getY()); //it sends the object back
                    objsRects[i].setLocation(270, objs[i].getY());
                    checkingRed = true; //sets it true that a red object was hit
                    break; //breaks the loop
                }
            }
            if (checkingRed == false) { //if a red object wasn't hit 
                lives--; //then the lives go down by one
            }
        }
        if (e == KeyEvent.VK_Y) {
            boolean checkingYellow = false; //a check to make sure that something actually registers
            for (int i = 0; i < yellowColliding.length; i++) { //for loop that checks every possible object if it's colliding
                if (yellowColliding[i]) { //if it's true
                    objMoving[i] = false;
                    objs[i].setVisible(false);
                    objs[i].setLocation(270, objs[i].getY()); //it sends the object back
                    objsRects[i].setLocation(270, objs[i].getY());
                    checkingYellow = true; //sets it true that a yellow object was hit
                    break; //breaks the loop
                }
            }
            if (checkingYellow == false) { //if a yewllo object wasn't hit 
                lives--; //then the lives go down by one
            }
        }
        if (e == KeyEvent.VK_G) {
            boolean checkingGreen = false; //a check to make sure that something actually registers
            for (int i = 0; i < greenColliding.length; i++) { //for loop that checks every possible object if it's colliding
                if (greenColliding[i]) { //if it's true
                    objMoving[i] = false;
                    objs[i].setVisible(false);
                    objs[i].setLocation(270, objs[i].getY()); //it sends the object back
                    objsRects[i].setLocation(270, objs[i].getY());
                    checkingGreen = true; //sets it true that a green object was hit
                    break; //breaks the loop
                }
            }
            if (checkingGreen == false) { //if a green object wasn't hit 
                lives--; //then the lives go down by one
            }
        }
        /////////////////////////////////////////////////////////
        ///////////Key Presses for the math game/////////////////
        /////////////////////////////////////////////////////////
        if (mathAnswerPending) { //keys are active if a question is present
            if (e == KeyEvent.VK_0) { //when a number is pressed
                userMathAnsS += "0"; //the number is added to a string
                userMathAns = Integer.parseInt(userMathAnsS); //the string is parsed
                mathText.setText(mathTextString + userMathAnsS); //the text is changed
            }//same for all other number keys 
            if (e == KeyEvent.VK_1) {
                userMathAnsS += "1";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_2) {
                userMathAnsS += "2";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_3) {
                userMathAnsS += "3";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_4) {
                userMathAnsS += "4";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_5) {
                userMathAnsS += "5";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_6) {
                userMathAnsS += "6";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_7) {
                userMathAnsS += "7";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_8) {
                userMathAnsS += "8";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_9) {
                userMathAnsS += "9";
                userMathAns = Integer.parseInt(userMathAnsS);
                mathText.setText(mathTextString + userMathAnsS);
            }
            if (e == KeyEvent.VK_BACK_SPACE) { //key for the backspace
                if (userMathAnsS.length() == 1) { //if the answers was only one digit long, performing a backspace would mean resetting the answer
                    userMathAnsS = ""; //answer is reset
                    userMathAns = 0;
                } else if (userMathAnsS.length() > 0) { //if the answer is more than one long but not one
                    userMathAnsS = userMathAnsS.substring(0, userMathAnsS.length() - 1); //just does a regular back space by chaning to the string to the string minus the last character
                    userMathAns = Integer.parseInt(userMathAnsS); //parses the integer
                }
                mathText.setText(mathTextString + userMathAnsS); //at the end the text is changed
            }
        }

    } //handles all the key presses

    @Override
    public void keyReleased(KeyEvent arg0) {
    } //un used
}
