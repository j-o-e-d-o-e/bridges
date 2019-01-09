package net.joedoe.utils;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Timer {
    private final ExecutorService service = Executors.newCachedThreadPool();
    private TimerListener listener;
    private boolean running;
    private LocalTime startTime;

    /**
     * Startet den Thread.
     */
    public void start() {
        startTime = LocalTime.of(0, 0, 0);
        running = true;
        service.submit(this::run);
    }

    private void run() {
        while (running) {
            listener.onChange();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTime() {
        startTime = startTime.plusSeconds(1);
        return String.format("%02d:%02d", startTime.getMinute(), startTime.getSecond());
    }

    /**
     * Stoppt Thread.
     */
    public void stop() {
        running = false;
    }

    /**
     * Schließt Thread.
     */
    public void shutdown() {
        running = false;
        service.shutdown();
    }

    /**
     * Checkt, ob Thread aktuell läuft.
     *
     * @return true, falls Thread aktuell läuft.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Setzt den Listener, um {@link net.joedoe.views.Grid} zu benachrichtigen.
     *
     * @param listener enthält auszuführende Methode
     */
    public void setListener(TimerListener listener) {
        this.listener = listener;
    }
}
