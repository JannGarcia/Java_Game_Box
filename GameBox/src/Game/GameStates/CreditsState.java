package Game.GameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Images;

public class CreditsState extends State {
	
	
	int scrollSpeed = 2, counter;
	boolean scrolling, finished;
	ArrayList<String> displayedText;
	int displayTime, displayed, finishedCD;

	public CreditsState(Handler handler) {
		super(handler);
		refresh();
		ZeldaGameState.gold = false;
		ZeldaGameState.silver = false;
		ZeldaGameState.bronze = false;
	}

	@Override
	public void tick() {

		if (finished) {
			
			if (finishedCD > 0) {finishedCD--;}
			
			else if (finishedCD <= 0) {
				handler.getMusicHandler().changeMusic("nature.wav");
				handler.getZeldaGameState().refresh();
				ZeldaGameState.timesDied = 0;
				ZeldaGameState.potionsUsed = 0;
				ZeldaGameState.rupeesSpent = 0;
				handler.changeState(handler.getMenuState());
			}
			
			return;
		}

		
		scrolling = displayedText.isEmpty();

		if (scrolling) {
			counter+= scrollSpeed;		
			
			if (Images.credits.getHeight() * 2 - counter < handler.getHeight()/2-100) {
				finished = true;
			}
		}
		
		else {
			if (displayed > displayTime) {
				displayedText.remove(0);
				displayed = 0;
			}
			
			else {
				displayed++;
				
				// Play sounds for text
				if (displayed == 1) {
					if (displayedText.get(0).equals("You deserve a trophy for that :)")) {
						handler.getMusicHandler().playEffect("yay.wav");
					}
						
					else if (displayedText.get(0).equals("Cheater.")) {
						handler.getMusicHandler().playEffect("boo.wav");
					}
				}
				

				
				if (displayedText.get(0).equals("Are you currently grading this project?") && displayed == displayTime / 2) {
					String answer = handler.getDisplayScreen().stringInputPopUp("Are you grading this project?", "Y/N");					
					if (answer == null) {
						displayedText.add("Can't even follow instructions, huh?");
						displayedText.add(":(");
					}
					
					else {
						switch(answer.toUpperCase()) {
						case "Y":
							displayedText.add("Thank you for taking your time.");
							displayedText.add("To see most of what we had to offer.");
							displayedText.add("This wouldn't have been possible without you.");
							displayedText.add("And because of that, you deserve a cookie.");
							displayedText.add("(I can't give you a cookie, but you get the gesture.)");
							break;
							
						case "N":
							displayedText.add("This game was made as a CIIC4010 project.");
							displayedText.add("It is the 3rd installment in the Java Game Box series.");
							displayedText.add("This is the work of 2 hard-working students");
							displayedText.add("who just wanted to have fun with Zelda :)");
							displayedText.add("Thank you for playing.");
							break;
							
						case "THIS IS BEING RECORDED":
							displayedText.add("Hello World!");
							displayedText.add("Hope you enjoyed the video! :)");
							break;
						default:
							displayedText.add("Can't even follow instructions, huh?");
							displayedText.add(":(");
							break;
						}
					}
				}
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		if (!displayedText.isEmpty()) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arcade Classic Regular", Font.BOLD, 24));
			// Draw Text here!
			this.drawCenteredString(g, displayedText.get(0), new Rectangle(0,0,handler.getWidth(), handler.getHeight()), g.getFont());
		}
		
		else {
			// Draw the Image Here!
			g.drawImage(Images.credits, 0, handler.getHeight() - counter, handler.getWidth(), Images.credits.getHeight() * 2, null);

		}
	}

	@Override
	public void refresh() {
		displayedText = new ArrayList<>();
		// Put all the text here
		
		displayedText.add("And just like that, your adventure was over...");
		displayedText.add("You have done what no other Link could do.");
		displayedText.add("Defeat He-Man.");
		displayedText.add("You have spent " + ZeldaGameState.rupeesSpent + " rupees.");
		
		if (ZeldaGameState.rupeesSpent > 500) {
			displayedText.add("Even I have never spent so much...");
		}

		else if (ZeldaGameState.rupeesSpent > 255) {
			displayedText.add("Fun fact:");
			displayedText.add("255 rupees was the most you could have");
			displayedText.add("in the original game.");
			displayedText.add("That's because rupee count were held in a byte.");
			displayedText.add("(Search it up if you don't know what a byte is.)");
		}
		
		else if (ZeldaGameState.rupeesSpent > 100) {
			displayedText.add("Hopefully Burgerpants is living the rich life.");
			displayedText.add("With all the money you gave him,");
			displayedText.add("he managed to buy a house on the surface.");
			displayedText.add(":)");
		}
		
		else if (ZeldaGameState.rupeesSpent > 20) {
			displayedText.add("You're not an avid spender, are you?");
		}
		
		else if (ZeldaGameState.rupeesSpent >= 1) {
			displayedText.add("Cheap.");
		}
		
		else {
			displayedText.add("We programmed a shop for you.");
			displayedText.add("Use it next time.");
		}
		
		
		displayedText.add("You have used " + ZeldaGameState.potionsUsed + " potions");
		
		if (ZeldaGameState.potionsUsed > 10) {
			displayedText.add("Potions are broken.");
			displayedText.add("In the original game, they did not heal you fully.");
			displayedText.add("I was merciful");
		}
		
		else if (ZeldaGameState.potionsUsed > 5) {
			displayedText.add("You love pressing the heal button huh?");
			displayedText.add("Don't worry, at least it was worth it.");
		}
		
		else if (ZeldaGameState.potionsUsed >= 1) {
			displayedText.add("Not too many potions.");
			displayedText.add("Nice.");
		}
		
		else {
			displayedText.add("You know how to use potions... right?");
			if (Handler.DEBUG) {
				displayedText.add("Since you're using DEBUG mode, press G.");
			}
			else {
				displayedText.add("Press H to heal when you have a potion.");
			}
			
			displayedText.add("The fact that you beat this game without using a potion...");
			displayedText.add("Even I can't do that.");
			displayedText.add("Good job");
			displayedText.add(":)");
		}
		
		
		


		displayedText.add("You have died " + ZeldaGameState.timesDied + " times." );
		if (ZeldaGameState.usedDEBUG && Handler.DEBUG) {
			displayedText.add("Now try playing the game without using DEBUG commands.");
			displayedText.add("Cheater.");
		}
		
		else {
			if (ZeldaGameState.timesDied == 0) {
				
				if (!ZeldaGameState.gold) {
					displayedText.add("And without DEBUG mode...");
					displayedText.add("That's impressive.");
					displayedText.add("You deserve a trophy for that :)");
					ZeldaGameState.gold = true;
				}
				
				else {
					displayedText.add("Beating the game AGAIN without dying or cheating...");
					displayedText.add("Truly is impressive...");
				}

	
			}
			
			else if (ZeldaGameState.timesDied <= 1) {
				displayedText.add("One failure is not bad.");
				
				if (!ZeldaGameState.silver && !ZeldaGameState.gold) {
					ZeldaGameState.silver = true;
					displayedText.add("So I'll reward you.");
					displayedText.add("With a Silver Trophy :)");
					displayedText.add("(Try to go for no deaths next time.)");
				}

			}
			
			else if (ZeldaGameState.timesDied <= 3) {
				displayedText.add("You had a few oopsies, but that's ok.");
				
				if (!ZeldaGameState.silver && !ZeldaGameState.gold && !ZeldaGameState.bronze) {
					ZeldaGameState.bronze = true;
					displayedText.add("I'll still give you this bronze trophy.");
					displayedText.add("(Try for a higher trophy, it's worth it.)");
				}

			}
			
			else if (ZeldaGameState.timesDied <= 5) {
				displayedText.add("The game was challenging, I understand.");
			}
			
			else if (ZeldaGameState.timesDied <= 10) {
				displayedText.add("I admire your dedication after dying so many times...");
			}
			
			else {
				displayedText.add("Was the game really that hard? :(");
			}
			
			displayedText.add("Anyways...");
			displayedText.add("you have shown us...");
			displayedText.add("that this game is indeed beatable.");
			displayedText.add("One question however...");
			displayedText.add("Are you currently grading this project?");
		}
		
		


		scrolling = false;
		finished = false;
		counter = 0;
		finishedCD = 200;
		displayed = 0;
		displayTime = 200;

	}
	
	
	
	// BORROWED FROM: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
	
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}

}
