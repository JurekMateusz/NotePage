package pl.mjurek.notepage.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTask {
  public static void execute(LongTermTask task) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(task);
    executor.shutdown();
  }
}
