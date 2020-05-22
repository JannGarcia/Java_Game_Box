package Main;

import java.util.ArrayList;

import javax.sound.sampled.Clip;

import Display.DisplayScreen;
import Game.GameStates.CreditsState;
import Game.GameStates.GalagaState;
import Game.GameStates.GameState;
import Game.GameStates.MenuState;
import Game.GameStates.PacManEndGameState;
import Game.GameStates.PacManState;
import Game.GameStates.PauseState;
import Game.GameStates.State;
import Game.GameStates.Zelda.BossBattleState;
import Game.GameStates.Zelda.GameOverState;
import Game.GameStates.Zelda.StoreState;
import Game.GameStates.Zelda.ZeldaGameState;
import Game.GameStates.Zelda.ZeldaIntroStates;
import Game.GameStates.Zelda.ZeldaMapMakerState;
import Game.PacMan.World.Map;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.Zelda.Dungeons.Dungeon1;
import Input.KeyManager;
import Input.MouseManager;
import Resources.MusicHandler;
import Resources.ScoreManager;


/**
 * Created by AlexVR on 1/24/2020.
 */

public class Handler {

    private GameSetUp game;
    private DisplayScreen screen;
    private ScoreManager scoreManager;
    private boolean fullScreen = false,mute=false;
    private Clip backgroundMusic;
    private MusicHandler musicHandler;
    private ArrayList<Clip> effects;
    private State lastState;
    public static boolean DEBUG = true;
    private Map map;
    private PacMan pacman;


    public Handler(GameSetUp game){
        this.game = game;
    }

    public int getWidth(){
        return getDisplayScreen().getCanvas().getWidth();
    }

    public int getHeight(){

        return getDisplayScreen().getCanvas().getHeight();
    }

    public GameSetUp getGameProperties() {
        return game;
    }

    public void setGameProperties(GameSetUp game) {
        this.game = game;
    }

    public KeyManager getKeyManager(){
        return game.getKeyManager();
    }

    public MouseManager getMouseManager(){
        return game.getMouseManager();
    }

    public DisplayScreen getDisplayScreen(){return screen;}

    public void setDisplayScreen(DisplayScreen screen){this.screen=screen;}

    public GameState getGameState (){
        return (GameState)getGameProperties().gameState;
    }

    public MenuState getMenuState (){
        return (MenuState) getGameProperties().menuState;
    }

    public PauseState getPauseState (){
        return (PauseState) getGameProperties().pauseState;
    }

    public GalagaState getGalagaState (){
        return (GalagaState)getGameProperties().galagaState;
    }

    public PacManState getPacManState (){
        return (PacManState)getGameProperties().pacmanState;
    }
    
    public GameOverState getGameOverState() {
		return (GameOverState) getGameProperties().gameOverState;
	}
    
    public BossBattleState getBossState() {
    	return (BossBattleState) getGameProperties().bossBattleState;
    }

    public ZeldaGameState getZeldaGameState (){
        return (ZeldaGameState)getGameProperties().zeldaGameState;
    }

    public ZeldaIntroStates getZeldaIntroState (){
        return (ZeldaIntroStates) getGameProperties().zeldaIntroState;
    }

    public ZeldaMapMakerState getZeldaMMState (){
        return (ZeldaMapMakerState)getGameProperties().zeldaMapMakerState;
    }
    
    public StoreState getStoreState() {
    	return (StoreState) getGameProperties().zeldaStoreState;
    }
    
    public CreditsState getCreditsState() {
    	return (CreditsState) this.getGameProperties().credits;
    }
    
    public Dungeon1 getDungeon1() {
    	return (Dungeon1) getGameProperties().dungeon1;  
    }

    public void changeState(State state){
    	setLastState(getState());
        State.setState(state);
    }

    public State getState(){
        return State.getState();
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public Clip getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(Clip backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public MusicHandler getMusicHandler() {
        return musicHandler;
    }

    public void setMusicHandler(MusicHandler musicHandler) {
        this.musicHandler = musicHandler;
    }

    public ArrayList<Clip> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<Clip> effects) {
        this.effects = effects;
    }

    public State getLastState() {
        return lastState;
    }

    public void setLastState(State lastState) {
        this.lastState = lastState;
    }

    public Map getMap() {
        return map;
    }
    public void setMap(Map map){
        this.map=map;
    }

    public PacMan getPacman() {
        return pacman;
    }

    public void setPacman(PacMan pacman) {
        this.pacman = pacman;
    }

	public PacManEndGameState getPacManEndGameState() {
		return (PacManEndGameState) getGameProperties().pacManEndGameState;
	}
}
