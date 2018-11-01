package net.joedoe.controllers;

public class NewGameController {

    public NewGameController() {
    }


    public void createGame(){
        System.out.println("Generate new Game: AUTO");
    }

    public void createGame(int width, int height) {
        System.out.println("Generate new Game: WIDTH + HEIGTH");
    }

    public void createGame(int width, int height, int isles) {
        System.out.println("Generate new Game: WIDTH + HEIGHT + ISLES");
    }
}
