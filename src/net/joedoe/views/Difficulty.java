package net.joedoe.views;

public enum Difficulty {
    VERY_EASY(5, "very easy"), EASY(25, "easy"), MEDIUM(50, "medium"), HARD(75, "hard"), CHALLENGING(100, "challenging");
    private int isles;
    private String name;

    Difficulty(int isles, String name) {
        this.isles = isles;
        this.name = name;
    }

    public int getIsles() {
        return isles;
    }

    public String getName() {
        return name;
    }
}
