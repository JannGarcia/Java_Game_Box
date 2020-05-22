package Game.GameStates.Zelda;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import Game.GameStates.State;
import Game.Zelda.Entities.Dynamic.Link;
import Main.Handler;
import Resources.Images;

public class GameOverState extends State {
	
	private int selection = 1;
	int fontSize = 48;
	int textAlignment = handler.getWidth()/2-50;
	BufferedImage selector = Images.linkHeart;

	public GameOverState(Handler handler) {
		super(handler);
	}

	@Override
	public void tick() {
		
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) {selection = 1;}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) {selection = 2;}

		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)) {
			handler.getZeldaGameState().refresh();
			handler.getZeldaIntroState().refresh();
			handler.getBossState().refresh();
			if (selection == 1) {
				handler.getMusicHandler().changeMusic("intro.wav");
				handler.changeState(handler.getZeldaIntroState());
			}
			
			else if (selection == 2) {
				handler.getMusicHandler().changeMusic("nature.wav");
				handler.changeState(handler.getMenuState());
			}

		}
	}

	@Override
	public void render(Graphics g) {

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arcade Classic Regular", Font.BOLD, fontSize));
		g.drawString("RETRY", textAlignment, handler.getHeight()/2 - fontSize * 2);
		g.drawString("QUIT", textAlignment, handler.getHeight()/2 - fontSize);
		
		g.drawImage(selector, textAlignment - 60, handler.getHeight()/2 - fontSize * (4-selection), (int) (fontSize * .9), (int) (fontSize * .9), null);


	}

	@Override
	public void refresh() {
		selection = 1;
	}

}
