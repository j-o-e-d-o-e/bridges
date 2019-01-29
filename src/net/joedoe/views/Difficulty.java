package net.joedoe.views;

enum Difficulty {
    VERY_EASY(1, "very easy"), EASY(5, "easy"), MEDIUM(10, "medium"), HARD(15, "hard"), CHALLENGING(20, "challenging");
    private int level;
    private String name;

    Difficulty(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
