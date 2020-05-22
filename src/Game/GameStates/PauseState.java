package Game.GameStates;

import Display.UI.UIImageButton;
import Display.UI.UIManager;
import Main.Handler;
import Resources.Images;

import java.awt.*;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class PauseState extends State {

    private UIManager uiManager;

    public PauseState(Handler handler) {
        super(handler);
        refresh();

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.pauseBackground,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);

    }

    @Override
    public void refresh() {
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(56, 223, 128, 64, Images.pauseResumeButton, () -> {
            handler.getMouseManager().setUimanager(null);
            if (handler.getLastState() == null){
                handler.changeState(handler.getMenuState());
            }else {
            	handler.getMusicHandler().changeMusic(handler.getMusicHandler().getLastSong());
                handler.changeState(handler.getLastState());
            }
        }));

        uiManager.addObjects(new UIImageButton(56, 223+(64+16), 128, 64, Images.pauseOptionsButton, () -> {
//            handler.getMouseManager().setUimanager(null);
//            handler.changeState(handler.getMenuState());
        }));

        uiManager.addObjects(new UIImageButton(56, (223+(64+16))+(64+16), 128, 64, Images.pauseToTitleButton, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getMusicHandler().changeMusic("nature.wav");
            handler.changeState(handler.getMenuState());
        }));
    }
}
