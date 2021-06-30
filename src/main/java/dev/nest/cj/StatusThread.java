package dev.nest.cj;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatusThread {

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public static void run() {
        service.scheduleAtFixedRate(() -> {
            Random random = new Random();
            String[] statuses = {"ONLINE", "DO_NOT_DISTURB", "IDLE"};
            String[] games = {"تم الدعس Centrix Jokar", "تم الدعس Nestirium", "with omak", "Free Palestine تم الدعس"};
            int randomGames = random.nextInt(games.length);
            int randomStatus = random.nextInt(statuses.length);
            CentrixJokar.getJDA().getPresence().setStatus(OnlineStatus.fromKey(statuses[randomStatus]));
            CentrixJokar.getJDA().getPresence().setActivity(Activity.playing(games[randomGames]));
        }, 60, 120, TimeUnit.SECONDS);
    }

    public static void terminate() {
        service.shutdown();
    }

}
