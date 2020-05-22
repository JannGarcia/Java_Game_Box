package Game.GameStates.Zelda;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Game.GameStates.State;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Dynamic.Link;
import Game.Zelda.Entities.Statics.Item;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class StoreState extends State {
	
	private BufferedImage[] burgerPants = Images.burgerPants;
	private BufferedImage BG = Images.storeBG, selector = Images.linkHeart;
	private int selectorW = selector.getWidth() * 4, selectorH = selector.getHeight() * 4;
	private ArrayList<String> text, renderedText;
	private ArrayList<Integer> emotionList;
	private String[] options;
	private int textReset = 120, textDelay = textReset;
	private int emotion = 0;
	private int menu = 0, selection = 1;
	private int borderWidth = 5;
	private Link link = handler.getZeldaGameState().link;
	private boolean leaving = false, switchSong = false, songPlaying = false;
	private Animation rupeeAnim;
	
	private final int HEART_PRICE = 10, CONTAINER_PRICE = 100, POTION_PRICE = 50;
	private final int PRICE_PER_ARROW = 1, MASTER_PRICE = 300, KEY_PRICE = 50;
	private int itemCount = 0;
	public boolean boughtSword = false;
	
	private int exitX = 2, exitY = 2;


	public StoreState(Handler handler) {
		super(handler);
		refresh();
	}

	@Override
	public void tick() {
		
		if (this.textDelay<=0) {
			String dialogue;
			if (!text.isEmpty() && ((dialogue = text.get(0)) != null)) {
				renderedText.add(dialogue);
				this.emotion = emotionList.get(0);
				text.remove(0); emotionList.remove(0);
				this.resetTextCooldown();
				
			}
			
		}
		
		else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) ){
			this.textDelay = 0;
			return;
		}
		
		else {this.textDelay--;}
		
		if (this.textEnded()) {
			
			if (leaving) {
				this.refresh();
				link.move(Direction.DOWN);
				handler.getMusicHandler().changeMusic("overworld.wav");
				handler.changeState(handler.getZeldaGameState());
			}
			
			if (switchSong) {
				handler.getMusicHandler().changeMusic("megalosuck.wav");
				switchSong = false;
				songPlaying = true;
			}
			
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) {
				selection = selection-1 < 0 ? options.length-1 : selection-1;
			}
			
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) {
				selection = (selection+1 >= options.length) ? 0 : selection+1;
			}
			
			
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE)) {
				
				switch(this.menu) {
				case 0: // Intro
					
					switch(this.selection) {
					case 0: // Buy
						addDialogue("Hey little buddy. You wanna buy something?", 6);
						menu = 1;
						break;
						
					case 1: // Sell
						addDialogue("Little buddy, I highly doubt you have anything I want.", 6);
						addDialogue("I'm here to make money, not give it to some kid.", 5);
						addDialogue("If you want money",1);
						addDialogue("You can always kill some more weirdos.", 3);
						break;
						
					case 2: // Talk
						addDialogue("Really? You wanna talk to me?", 0);
						addDialogue("Alright. What'd you wanna ask?", 5);
						menu = 10;
						break;
						
					case 3: // Leave
						leaving = true;
						
						if (!songPlaying) {
							addDialogue("Stay safe, little buddy.", 6);
						}
						
						else {
							addDialogue("Try not to get dunked on, little buddy.", 6);
						}

						break;
					}
					
					break;
					
				case 1: // Choosing something to buy
					switch(this.selection) {
					case 0: // Selecting Arrows
						if (link.rupees < PRICE_PER_ARROW) {addDialogue("Little buddy, ya gotta have money.", 3); break;}

						try {
							itemCount = Integer.valueOf(handler.getDisplayScreen().stringInputPopUp("How many arrows do you want to buy?", String.valueOf(5)));
							
							if (itemCount >= 1000) {
								addDialogue("No.", 0);
								addDialogue("I will not sell more than 1000 arrows to a kid.", 3);
								break;
							}
							
							else if (PRICE_PER_ARROW * itemCount > link.rupees) {
								addDialogue("You can't afford that much, little buddy.", 3);
								break;
							}

							else if (itemCount <= 0) {
								addDialogue("Little buddy...", 0);
								addDialogue("That makes no sense.", 3);
								break;
							}
							
							else {
								addDialogue("That'll be " + PRICE_PER_ARROW * itemCount + " rupees, you want?", 0);
							}

						}

						catch (Exception e) {
							addDialogue("Little buddy...", 0);
							addDialogue("I didn't understand a word you just said.", 5);
							itemCount = link.rupees > 5 ? 5 : link.rupees;
							addDialogue("How about " + itemCount +" arrows for " + PRICE_PER_ARROW*itemCount +" rupees, will that do?", 0);
						}


						menu = 2;
						break;
						
					case 1: // Selecting Hearts
						if (link.rupees < HEART_PRICE) {addDialogue("I need the rupees, little buddy.", 3); break;}
						
						if (link.health == link.maxHealth) {
							addDialogue("Buddy, I know I'm in it for the money but...",4);
							addDialogue("You don't need this right now",5);
							break;
						}
						
						addDialogue("That'll be " + HEART_PRICE + " rupees, you want?", 0);
						menu = 3;
						break;
						
					case 2: // MORE
						menu = 4;
						addDialogue("I guess I got a few more things...", 6);
						break;
						
					case 3: // Going Back
						String[] randomOptions = {"So what do ya wanna do?", "Can't make up your mind?",
								"Don't wanna buy anything? Or are you just poor?"};
						int index = new Random().nextInt(randomOptions.length);
						int emotion = index==0 ? (6) : index == 1? (3) : index == 2? (5) : 0;
						addDialogue(randomOptions[index], emotion);
						menu = 0;
						break;
					}
					break;
					
				case 2: // Buying Arrows
					if (selection == 0) { // YES
						
						if (itemCount >= 50) {
							addDialogue("That's a whole lot of arrows, buddy.",4);
							addDialogue("What are you planning?", 6);
						}
						
						else {
							addDialogue("Be sure where you save those, they are sharp...", 0);
							addDialogue("Don't complain to the manager later.", 2);
							addDialogue("...Wait.", 0);
							addDialogue("I AM the manager.", 5);
						}
						

						link.rupees-=PRICE_PER_ARROW * itemCount;
						ZeldaGameState.rupeesSpent+=PRICE_PER_ARROW * itemCount;
						link.arrows += itemCount;
						handler.getMusicHandler().playEffect("heart.wav");
					}
					
					else {
						addDialogue("So why did you want it?", 5);
					}
					menu = 1;
					break;
					
				case 3: // Buying Heart
					
					if (selection == 0) { // YES
						addDialogue("Hope that heart fills you with determination.", 0);
						addDialogue("...Man. What a trip that was.", 6);
						link.rupees-=HEART_PRICE;
						ZeldaGameState.rupeesSpent+=HEART_PRICE;
						link.addHealth(2);
						handler.getMusicHandler().playEffect("heart.wav");
					}
					
					else {
						addDialogue("Guess you didn't wanna waste the money?", 5);
					}
					
					menu = 1;
					break;
					
				case 4: // MORE OPTIONS
					switch(selection) {
					case 0: // Selecting Health Potion
						if (link.rupees < POTION_PRICE) {addDialogue("You can't get this stuff for free, buddy.", 3); break;}
						addDialogue("That'll be " + POTION_PRICE + " rupees, you want?", 0);
						menu = 5;
						break;
						
					case 1: // Selecting Heart container
						if (link.rupees < CONTAINER_PRICE) {addDialogue("Little buddy, did you bring your wallet?", 3); break;}
						addDialogue("That'll be " + CONTAINER_PRICE + " rupees, you want?", 0);
						menu = 6;
						break;
						
					case 2: // More options
						addDialogue("Alright...This is what I have left. No more.", 5);
						menu = 7;
						break;
						
					case 3: // BACK TO MENU
						String[] randomOptions = {"Let me just... wait for you to decide", "Can't make up your mind?",
						"Don't wanna buy anything? Or are you just poor?"};
						int index = new Random().nextInt(randomOptions.length);
						int emotion = index==0 ? (5) : index == 1? (3) : index == 2? (5) : 0;
						addDialogue(randomOptions[index], emotion);
						menu = 0;
						break;
					}
					break;
					
				case 5: // Buying Health Potion
					if (selection == 0) { // YES
						addDialogue("Do you even know how to use that...?", 1);
						link.rupees-=POTION_PRICE;
						ZeldaGameState.rupeesSpent+=POTION_PRICE;
						link.potions++;
						handler.getMusicHandler().playEffect("heart.wav");
					}
					
					else {addDialogue("Got something else in mind?", 3);}
					menu = 4;
					break;
					
				case 6: // Buying Heart Container
					if (selection == 0) { // YES
						addDialogue("These containers were all over the place.", 6);
						addDialogue("I just grabbed a few from outside, and here we are.", 6);
						link.rupees-=CONTAINER_PRICE;
						ZeldaGameState.rupeesSpent+=CONTAINER_PRICE;
						link.increaseMaxHealth();
						handler.getMusicHandler().playEffect("itemGet.wav");
					}
					
					else {addDialogue("Kid, you gotta choose something.", 3);}
					menu = 4;
					break;
					
					
				case 7: // MORE MORE OPTIONS
					switch(selection) {
					case 0: // Selecting MASTER SWORD
						
						if (boughtSword) {
							addDialogue("Hey buddy, I already sold this to ya.",0);
							
							if (!link.hasFinalSword) {
								addDialogue("Did ya lose it?", 3);
								addDialogue("What a waste...", 4);
							}
							
							else {
								addDialogue("You're carrying it on your back", 6);
							}
							
							break;
							
						}
						
						if (link.rupees < MASTER_PRICE) {addDialogue("You can't get this one for free, kid.", 3); break;}
						addDialogue("This one's a big one, little buddy.", 6);
						addDialogue("I'll sell it for " + MASTER_PRICE + " rupees, deal?", 0);
						menu = 8;
						break;
						
					case 1: // Selecting Mysterious Key
						if (link.rupees < KEY_PRICE) {addDialogue("It's either you pay or you don't get it.", 3); break;}

						addDialogue("That'll be " + KEY_PRICE + " rupees.", 0);
						addDialogue("Trust me, it's worth buying.", 1);

						menu = 9;
						
						break;
						
					case 2: // BACK
						String[] randomOptions = {"Let me just... wait for you to decide", "Can't make up your mind?",
						"Don't wanna buy anything? Or are you just poor?"};
						int index = new Random().nextInt(randomOptions.length);
						int emotion = index==0 ? (5) : index == 1? (3) : index == 2? (5) : 0;
						addDialogue(randomOptions[index], emotion);
						menu = 0;
						break;
					}
					break;
					
				case 8: // Buying Master Sword
					if (selection == 0) { // YES
						addDialogue("Don't ask me how I got this.", 5);
						link.rupees-=MASTER_PRICE;
						ZeldaGameState.rupeesSpent+=MASTER_PRICE;
						handler.getZeldaGameState().toTick.add(new Item(exitX, exitY, "mastersword", Images.masterSword, handler));
						handler.getMusicHandler().playEffect("itemGet.wav");
						boughtSword = true;
					}
					
					else {addDialogue("Giving THAT up? Alright...", 3);}
					menu = 7;
					break;
					
				case 9: // Buying Key
					if (selection == 0) { // YES
						addDialogue("Good luck with whatever this is for.", 4);
						addDialogue("I keep finding these things on the floor.", 4);
						link.rupees-=KEY_PRICE;
						ZeldaGameState.rupeesSpent+=KEY_PRICE;
						link.keys++;
						handler.getMusicHandler().playEffect("itemGet.wav");
					}
					
					else {addDialogue("...", 3);}
					menu = 7;
					break;
					
				case 10:
					switch(this.selection) {
					case 0: // Why are you in this game?
						addDialogue("Listen here, little buddy.", 6);
						addDialogue("I don't ask you what you're killing out there.", 3);
						addDialogue("Don't ask me why I'm doing my job.", 3);
						break;
						
					case 1: // New job?
						addDialogue("After the whole 'freedom' thing underground", 5);
						addDialogue("Found this nifty spot on the Surface", 2);
						addDialogue("No one was here so I thought", 1);
						addDialogue("Hey! I can be my own boss!...", 2);
						addDialogue("I'm still a cashier, though...", 4);
						break;
						
					case 2: // So there's this song...
						addDialogue("Megalo-- what?", 0);
						addDialogue("I've never heard that name before.", 0);
						addDialogue("...", 4);
						addDialogue("That's weird, I felt a tingle on my back.", 5);
						addDialogue("Is this a bad time?", 5);
						switchSong = true;
						break;
						
					case 3: // Something random
						
						int choice = new Random().nextInt(6);
						
						switch(choice) {
						case 0:
							addDialogue("...Did you...", 0);
							addDialogue("Did you just call me a milennial?", 1);
							break;
							
						case 1:
							addDialogue("No, I don't know why that old man is giving balls.", 1);
							addDialogue("I'm not the cops, little buddy.", 6);
							addDialogue("Take it up with them.", 6);
							break;
							
						case 2:
							addDialogue("I can't leave the shop alone.", 5);
							addDialogue("How am I gonna earn money if I'm not here?", 5);
							addDialogue("...", 4);
							addDialogue("Also there's weirdos out there.", 5);
							break;
							
						case 3:
							addDialogue("Hey, here's an idea little buddy", 6);
							addDialogue("Why not buy something while you're here?", 1);
							break;
							
						case 4:
							addDialogue("Hey, I got a TV and some snacks over here", 5);
							addDialogue("If you're REALLY this bored", 5);
							addDialogue("You're welcome to just chill and hang.", 6);
							addDialogue("...", 5);
							addDialogue("Or not. You look busy.", 4);
							break;
							
						case 5:
							if (handler.getZeldaGameState().bossDefeated) {
								// TODO: Give a reward for beating heman, depending on your deaths
								addDialogue("He-Man what?", 3);
								addDialogue("You're telling me there was a literal god in that cave?",3);
								addDialogue("Wow.",4);
								
								if (ZeldaGameState.timesDied == 0) {
									addDialogue("I'm sure he wasn't a problem for you, right?", 6);
								}
								
								else if (ZeldaGameState.timesDied <= 2) {
									addDialogue("Must've been fun for you, huh?", 5);
								}
								
								else if (ZeldaGameState.timesDied <= 5) {
									addDialogue("Must've been tough. You look beat, kid.", 1);
									addDialogue("Why not relax and stay with ol' Burgerpants?",2);
								}
								
								else if (ZeldaGameState.timesDied > 10) {
									addDialogue("How are you still standing?", 4);
								}
							
							}
							
							else {
								addDialogue("I heard about this spooky cave nearby.", 0);
								addDialogue("If you're REALLY filled with determination", 6);
								addDialogue("You should go look for that cave", 6);
								addDialogue("Tell me what you find, and maybe I'll make you a deal.", 3);
							}
							break;
						}
						
						
						break;
					}
					menu = 0; // Menu
					break;
					
				}
				selection = 0;
				this.renderedText.clear();
			}
		}



		
	}
	
	private void addDialogue(String text, int emotion) {
		this.text.add(text);
		this.emotionList.add(emotion);
	}


	@Override
	public void render(Graphics g) {
		BufferedImage img = burgerPants[emotion];
		Graphics2D g2 = (Graphics2D) g;
		int width = handler.getWidth(), height = handler.getHeight();
		
		g.drawImage(BG, 0, 0, width, height/2, null);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, height/2, width, height/2);
		g2.setColor(Color.BLACK);
		g2.fillRect(borderWidth, height/2+borderWidth, width-borderWidth * 2, height/2 -borderWidth * 2);
		
		g2.setColor(Color.WHITE);
		g2.fillRect( (int) (width * .70) , height/2, (int) (width * .005), height/2);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arcade Classic Regular", Font.BOLD, 24));
		
		for (int i = 0; i < renderedText.size(); i++) {
			g.drawString("*     " + renderedText.get(i), 50, height/2 + (i * (g2.getFont().getSize() + 20)) + 75);
		}
	
		g.drawImage(img, width/2 -img.getWidth()*2, height/2 -img.getHeight()*4 , img.getWidth() * 4, img.getHeight() * 4, null);
		
		if (leaving && songPlaying) {
			g.drawImage(Images.sansEye, width/2-20, height/8 - 50, 60*3, 48*3, null);
		}
		
		if (emotion == 3) {
			g.drawImage(Images.burgerArms,width/4 , height/2 - Images.burgerArms.getHeight() * 4 , Images.burgerArms.getWidth() * 4, Images.burgerArms.getHeight() * 4, null);
		}
		
		// RUPEES
		g.setColor(Color.BLACK);
		// RUPEES
		rupeeAnim.tick();
		g.drawImage(rupeeAnim.getCurrentFrame(),handler.getWidth() - 150, ZeldaGameState.stageHeight/2+110, 32, 50, null);
		g.drawString("X",handler.getWidth() - 100, ZeldaGameState.stageHeight/2+160);
		g.drawString(Integer.toString(link.rupees),handler.getWidth() - 80, ZeldaGameState.stageHeight/2+160);
	
		
		if (textEnded()) {
			
			g.setColor(Color.WHITE);

			switch(this.menu) {	
			case 0: // Intro
				String[] options = {"BUY", "SELL", "TALK", "LEAVE"};
				this.options = options;
				break;
				
			case 1: // MENU 1
				String[] options2 = {"Arrows", "Hearts", "MORE", "BACK"};
				this.options = options2;
				break;
				
			case 4: // MENU 2
				String[] options4 = {"Health Potion", "Heart Container", "MORE", "BACK"};
				this.options = options4;
				break;
				
			case 7: // MENU 3
				String[] options5 = {"MASTER SWORD", "Dungeon Key", "BACK"};
				this.options = options5;
				break;
				
			case 10: // TALK
				String[] options6 = {"Why are you in this game?", "New job?", "So there's this song...", "Something random"};
				this.options = options6;
				break;
				
			default: // Confirmation
				String[] options3 = {"YES", "NO"};
				this.options = options3;
				break;
			
			}

			
			drawOptions(g);
		}
	
	}

	@Override
	public void refresh() {
		text = new ArrayList<>();
		renderedText = new ArrayList<>();
		emotionList = new ArrayList<>();
		menu = 0; selection = 0;
		addDialogue("Hello valued Shop customer.", 0);
		addDialogue("What would you like to buy?", 0);
		leaving = false;
		songPlaying = false;
		link = handler.getZeldaGameState().link;
		rupeeAnim = new Animation(256, Images.rupeeFrames);
	}
	
	private void resetTextCooldown() {
		this.textDelay=this.textReset;
	}
	
	private boolean textEnded() {
		return this.text.isEmpty() && this.textDelay <= 0;
	}
	
	private void drawOptions(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int width = handler.getWidth(), height = handler.getHeight();
		for (int i = 0; i < options.length; i++) {
			g.drawString(options[i], (int) (width * .75), height/2 + (i * (g2.getFont().getSize() + 50)) + 75 );
		}
		
		g.drawImage(selector, (int) (width * .72) , height/2  + (this.selection * (g2.getFont().getSize() + 50)) + 50 , selectorW , selectorH, null);

	}

}
