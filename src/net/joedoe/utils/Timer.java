package net.joedoe.utils;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Timer {
    private final ExecutorService service = Executors.newCachedThreadPool();
    private TimerListener listener;
    private boolean running;
    private LocalTime startTime = LocalTime.of(0, 0, 0), currentTime;

    public void start() {
        currentTime = startTime;
        running = true;
        service.submit(this::run);
    }

    public void restart() {
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
            if (running) listener.onChange();
        }
    }

    public String getTime() {
        currentTime = currentTime.plusSeconds(1);
        return String.format("%02d:%02d", currentTime.getMinute(), currentTime.getSecond());
    }

    public String getStartTime(){
        return String.format("%02d:%02d", startTime.getMinute(), startTime.getSecond());
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
