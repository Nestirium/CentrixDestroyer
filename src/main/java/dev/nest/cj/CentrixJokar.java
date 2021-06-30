package dev.nest.cj;

import com.zaxxer.hikari.HikariDataSource;
import dev.nest.cj.commands.*;
import dev.nest.cj.io.FlatFile;
import dev.nest.cj.mysql.DataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class CentrixJokar extends JavaPlugin {

    private static JDA jda;
    private static HikariDataSource hikariDataSource, hikariDataSource2;
    private static Code code;

    public void onEnable() {
        try {
            jda = JDABuilder.createDefault("ODQ2Mzk5ODE0MjUwMTM1NTY0.YKu9QQ.9lw-XFsNt8k1debGBwS-vu8H3hw")
                    .build()
                    .awaitReady();
            jda.addEventListener(new Ban());
            jda.addEventListener(new Unban());
            jda.addEventListener(new Uinfo());
            jda.addEventListener(new AddTag());
            jda.addEventListener(new AddNameTag());
            jda.addEventListener(new Mute());
            jda.addEventListener(new Unmute());
            jda.addEventListener(new ListGroups());
            jda.addEventListener(new Help());
            jda.addEventListener(new Nuke());
            jda.addEventListener(new CheckPerm());
            jda.addEventListener(new AddPerm());
            jda.addEventListener(new RemovePerm());
            jda.addEventListener(new SetPrimary());
            jda.addEventListener(new ClearNameTags());
            jda.addEventListener(new ClearTags());
            jda.addEventListener(new SetTag());
            jda.addEventListener(new SetNameTag());
            jda.addEventListener(new ToggleAdmin());
            jda.addEventListener(new UnbanIP());
            jda.addEventListener(new CustomStatementListener());
            jda.addEventListener(new Connect());
            jda.addEventListener(new Disconnect());
            jda.addEventListener(new SetVotePoints());
            DataSource dataSource = new DataSource();
            hikariDataSource = new HikariDataSource(dataSource.getHikariConfig());
            hikariDataSource2 = new HikariDataSource(dataSource.getHikariConfig2());
            code = new Code();
            new FlatFile(this, code);
            StatusThread.run();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {}

    public static JDA getJDA() {
        return jda;
    }

    public static HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }

    public static HikariDataSource getHikariDataSource2() {
        return hikariDataSource2;
    }

    public static Code getCode() {
        return code;
    }

}
