package Game.GameStates;

import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PacManEndGameState extends State {

    // The cooldowns for each text when you get a game over
    private int text1Cooldown = 120, text2Cooldown = 120, text3Cooldown = 120;

    public PacManEndGameState(Handler handler) {
        super(handler);
        refresh();
    }

    //TODO: make it display nice message if the game is completed


    @Override
    public void tick() {
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
            if (handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager().getPacmanHighScore()) {
                handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
            }
            handler.getScoreManager().setPacmanCurrentScore(0);
            handler.changeState(handler.getMenuState());
            handler.getMusicHandler().changeMusic("nature.wav");
        } else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER))) {
            if (handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager().getPacmanHighScore()) {
                handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
            }
            handler.getScoreManager().setPacmanCurrentScore(0);
            handler.changeState(handler.getPacManState());
        }
    }

    @Override
    public void render(Graphics g) {

        g.drawImage(Images.pacmanGameOver,handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2,20,Images.pacmanGameOver.getWidth(),Images.pacmanGameOver.getHeight(),null);
        int score = handler.getScoreManager().getPacmanCurrentScore();
        int previousHighScore = handler.getScoreManager().getPacmanHighScore();


        if (text1Cooldown <= 0) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 64));
            g.setColor(Color.MAGENTA);
            // Current Score
            g.drawString("Score: " + score,handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2,80 + Images.pacmanGameOver.getHeight());

            if (text2Cooldown <= 0) {
                // Previous High Score
                g.drawString("High Score: " + previousHighScore, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2,140 + Images.pacmanGameOver.getHeight());

                if (text3Cooldown <= 0) {

                    // Message if Score is beat or not

                    if (previousHighScore == 0) {
                        // They played for the first time
                        String text = "You set your first score!";
                        String text2 = "Now try to beat it!";
                        g.drawString(text, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 220 + Images.pacmanGameOver.getHeight());
                        g.drawString(text2, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 290 + Images.pacmanGameOver.getHeight());

                    }

                    else if (score > previousHighScore) {
                        if (text3Cooldown == 0) {handler.getMusicHandler().playEffect("party.wav"); handler.getMusicHandler().playEffect("yay.wav"); text3Cooldown--;}
                        String text = "Congratulations!";
                        String text2 = "You beat the High Score!";
                        // Draw congratulations
                        g.drawString(text, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 220 + Images.pacmanGameOver.getHeight());
                        g.drawString(text2, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 290 + Images.pacmanGameOver.getHeight());
                        // g.drawImage(Images.confetti, 0, handler.getHeight()/2, handler.getWidth(), handler.getWidth(),null);
                        g.drawImage(Images.confetti, 0, handler.getHeight()/2, handler.getWidth(), handler.getHeight(),null);
                    }

                    else if (score < previousHighScore) {
                        String text = "Don't give up!";
                        String text2 = "Try again!";
                        // So close
                        g.drawString(text, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 220 + Images.pacmanGameOver.getHeight());
                        g.drawString(text2, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 290 + Images.pacmanGameOver.getHeight());
                    }

                    else {
                        String text = "You managed to";
                        String text2 = "tie the score...";
                        // They tied the score...
                        g.drawString(text, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 220 + Images.pacmanGameOver.getHeight());
                        g.drawString(text2, handler.getWidth()/4 + (handler.getWidth()/2 - Images.pacmanGameOver.getWidth())/2, 290 + Images.pacmanGameOver.getHeight());
                    }

                }

                else {
                    text3Cooldown--;}
            }

            else {
                text2Cooldown--;}
        }

        else {
            text1Cooldown--;
        }
    }

    @Override
    public void refresh() {
        text1Cooldown = 120;
        text2Cooldown = 120;
        text3Cooldown = 120;
    }
}
