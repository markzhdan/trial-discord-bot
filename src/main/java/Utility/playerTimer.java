package Utility;

import Main.Main;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class playerTimer
{
    public void startTimer()
    {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {

        }, 0, 5, TimeUnit.SECONDS);
    }
}
