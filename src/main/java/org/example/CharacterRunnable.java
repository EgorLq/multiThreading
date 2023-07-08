package org.example;

import java.util.concurrent.Semaphore;

public class CharacterRunnable implements Runnable {
  private final String character;
  private final Scene scene;
  private final Semaphore semaphore;

  public CharacterRunnable(String character, Scene scene, Semaphore semaphore) {
    this.character = character;
    this.scene = scene;
    this.semaphore = semaphore;
  }

  @Override
  public void run() {
    for (String[] dialogue : scene.getDialogues(character)) {
      String phrase = dialogue[1];

      try {
        semaphore.acquire(); // Захватить семафор перед доступом к сцене
        while (!scene.isCharacterTurn(character)) {
          semaphore.release(); // Временно освободить семафор, если не наступила очередь персонажа
          Thread.yield(); // Уступить другим потокам возможность захватить семафор
          semaphore.acquire(); // Снова захватить семафор перед доступом к сцене
        }
        System.out.println(character + ": " + phrase);
        scene.moveToNextCharacter(); // Перейти к очереди следующего персонажа
        semaphore.release(); // Освободить семафор после печати реплики
        Thread.sleep(200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
