import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

class MainTest {

  @RepeatedTest(10)
  void testDialogueOrder() throws InterruptedException {
    Scene scene = new Scene();
    Semaphore semaphore = new Semaphore(1);

    List<String> dialogueOrder = new ArrayList<>(); // Хранит порядок диалогов

    Thread joeyThread = new Thread(new CharacterRunnable("Joey", scene, semaphore, dialogueOrder));
    Thread chandlerThread =
        new Thread(new CharacterRunnable("Chandler", scene, semaphore, dialogueOrder));
    Thread phoebeThread =
        new Thread(new CharacterRunnable("Phoebe", scene, semaphore, dialogueOrder));
    Thread monicaThread =
        new Thread(new CharacterRunnable("Monica", scene, semaphore, dialogueOrder));
    Thread rachelThread =
        new Thread(new CharacterRunnable("Rachel", scene, semaphore, dialogueOrder));
    Thread rossThread = new Thread(new CharacterRunnable("Ross", scene, semaphore, dialogueOrder));

    // Запускаем потоки
    joeyThread.start();
    chandlerThread.start();
    phoebeThread.start();
    monicaThread.start();
    rachelThread.start();
    rossThread.start();

    // Ждем завершения всех потоков
    joeyThread.join();
    chandlerThread.join();
    phoebeThread.join();
    monicaThread.join();
    rachelThread.join();
    rossThread.join();

    // Ожидаемый порядок диалогов
    String[] expectedOrder = {
      "Joey: Hey, hey.",
      "Chandler: Hey.",
      "Phoebe: Hey.",
      "Chandler: And this from cry-for-help department. Are you wearing makeup?",
      "Joey: Yes, I am. As for today, I am officially Joey Tribbiani, actor slash model.",
      "Chandler: That's so funny, 'cause I was thinking you look more like Joey Tribbiani, man slash woman.",
      "Phoebe: What were you modelling for?",
      "Joey: You know those posters for City Free Clinic?",
      "Monica: Oh, wow, so you're gonna be one of those \"healthy, healthy, healthy guys\"?",
      "Phoebe: You know, the asthma guys was really cute.",
      "Chandler: Do you know which one you're gonna be?",
      "Joey: No, but I hear that lyme disease is open, so... (crosses fingers)",
      "Chandler: Good luck, man. I hope you get it.",
      "Joey: Thanks.",
      "Rachel: Hey, guys!",
      "Ross: I can't believe I'm single again."
    };

    // Преобразуем список в массив
    String[] actualOrder = dialogueOrder.toArray(new String[0]);

    // Проверяем, что фактический порядок соответствует ожидаемому порядку
    Assertions.assertArrayEquals(expectedOrder, actualOrder);
  }

  static class CharacterRunnable implements Runnable {
    private final String character;
    private final Scene scene;
    private final Semaphore semaphore;
    private final List<String> dialogueOrder;

    public CharacterRunnable(
        String character, Scene scene, Semaphore semaphore, List<String> dialogueOrder) {
      this.character = character;
      this.scene = scene;
      this.semaphore = semaphore;
      this.dialogueOrder = dialogueOrder;
    }

    @Override
    public void run() {
      for (String[] dialogue : scene.getDialogues(character)) {
        String phrase = dialogue[1];

        try {
          semaphore.acquire();
          while (!scene.isCharacterTurn(character)) {
            semaphore.release();
            Thread.yield();
            semaphore.acquire();
          }
          String dialogueLine = character + ": " + phrase;
          System.out.println(dialogueLine);
          dialogueOrder.add(dialogueLine); // Добавляем диалог в список
          scene.moveToNextCharacter();
          semaphore.release();
          Thread.sleep(200);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  static class Scene {
    private final String[][] dialogues = {
      {"Joey", "Hey, hey."},
      {"Chandler", "Hey."},
      {"Phoebe", "Hey."},
      {"Chandler", "And this from cry-for-help department. Are you wearing makeup?"},
      {"Joey", "Yes, I am. As for today, I am officially Joey Tribbiani, actor slash model."},
      {
        "Chandler",
        "That's so funny, 'cause I was thinking you look more like Joey Tribbiani, man slash woman."
      },
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

    public synchronized String[][] getDialogues(String character) {
      return filterDialoguesByCharacter(dialogues, character);
    }

    public synchronized boolean isCharacterTurn(String character) {
      String currentCharacter = dialogues[currentCharacterIndex][0];
      return currentCharacter.equals(character);
    }

    public synchronized void moveToNextCharacter() {
      currentCharacterIndex = (currentCharacterIndex + 1) % dialogues.length;
    }

    private String[][] filterDialoguesByCharacter(String[][] dialogues, String character) {
      return java.util.Arrays.stream(dialogues)
          .filter(dialogue -> dialogue[0].equals(character))
          .toArray(String[][]::new);
    }
  }
}
