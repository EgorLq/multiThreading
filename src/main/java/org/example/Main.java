package org.example;

import java.util.concurrent.Semaphore;

public class Main {
  public static void main(String[] args) {
    Scene scene = new Scene();
    Semaphore semaphore = new Semaphore(1); // Семафор для управления выполнением потоков

    Thread joeyThread = new Thread(new CharacterRunnable("Joey", scene, semaphore));
    Thread chandlerThread = new Thread(new CharacterRunnable("Chandler", scene, semaphore));
    Thread phoebeThread = new Thread(new CharacterRunnable("Phoebe", scene, semaphore));
    Thread monicaThread = new Thread(new CharacterRunnable("Monica", scene, semaphore));
    Thread rachelThread = new Thread(new CharacterRunnable("Rachel", scene, semaphore));
    Thread rossThread = new Thread(new CharacterRunnable("Ross", scene, semaphore));

    joeyThread.start();
    chandlerThread.start();
    phoebeThread.start();
    monicaThread.start();
    rachelThread.start();
    rossThread.start();
  }
}
