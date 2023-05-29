package org.example;

public class Scene {
    private final String[][] dialogues = {
            {"Joey", "Hey, hey."},
            {"Chandler", "Hey."},
            {"Phoebe", "Hey."},
            {"Chandler", "And this from cry-for-help department. Are you wearing makeup?"},
            {"Joey", "Yes, I am. As for today, I am officially Joey Tribbiani, actor slash model."},
            {"Chandler", "That's so funny, 'cause I was thinking you look more like Joey Tribbiani, man slash woman."},
            {"Phoebe", "What were you modelling for?"},
            {"Joey", "You know those posters for City Free Clinic?"},
            {"Monica", "Oh, wow, so you're gonna be one of those \"healthy, healthy, healthy guys\"?"},
            {"Phoebe", "You know, the asthma guys was really cute."},
            {"Chandler", "Do you know which one you're gonna be?"},
            {"Joey", "No, but I hear that lyme disease is open, so... (crosses fingers)"},
            {"Chandler", "Good luck, man. I hope you get it."},
            {"Joey", "Thanks."},
            {"Rachel", "Hey, guys!"},
            {"Ross", "I can't believe I'm single again."}
    };

    private int currentCharacterIndex = 0;

    /**
     * Возвращает диалоги, связанные с указанным персонажем.
     *
     * @param character персонаж, чьи диалоги необходимо получить
     * @return двумерный массив с диалогами персонажа
     */
    public synchronized String[][] getDialogues(String character) {
        return filterDialoguesByCharacter(dialogues, character);
    }
    /**
     * Проверяет, является ли текущий ход указанного персонажа.
     *
     * @param character персонаж для проверки хода
     * @return true, если это ход указанного персонажа, в противном случае - false
     */
    public synchronized boolean isCharacterTurn(String character) {
        String currentCharacter = dialogues[currentCharacterIndex][0];
        return currentCharacter.equals(character);
    }
    /**
     * Переходит к следующему персонажу в порядке диалогов.
     */
    public synchronized void moveToNextCharacter() {
        currentCharacterIndex = (currentCharacterIndex + 1) % dialogues.length;
    }
    /**
     * Фильтрует диалоги по указанному персонажу.
     *
     * @param dialogues массив диалогов
     * @param character персонаж для фильтрации
     * @return двумерный массив с отфильтрованными диалогами
     */
    private String[][] filterDialoguesByCharacter(String[][] dialogues, String character) {
        return java.util.Arrays.stream(dialogues)
                .filter(dialogue -> dialogue[0].equals(character))
                .toArray(String[][]::new);
    }
}
