package net.joedoe.utils;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Timer {
    private final ExecutorService service = Executors.newCachedThreadPool();
    private TimerListener listener;
    private boolean running;
    private LocalTime time = LocalTime.of(0, 0, 0);

    public void start() {
        running = true;
        service.submit(this::run);
    }

    private void run() {
        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (running) {
                time = time.plusSeconds(1);
                listener.onChange();
            }
        }
    }

    public String getTime() {
        return String.format("%02d:%02d", time.getMinute(), time.getSecond());
    }

    public void restart() {
        time = LocalTime.of(0, 0, 0);
    }

    public void stop() {
        running = false;
    }

    public void shutdown() {
        running = false;
        service.shutdown();
    }

    public void setListener(TimerListener listener) {
        this.listener = listener;
    }
}
