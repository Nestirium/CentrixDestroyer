package dev.nest.cj.io;

import dev.nest.cj.Code;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class FlatFile {

    public FlatFile(Plugin plugin, Code code) {
        if (!plugin.getDataFolder().exists() && plugin.getDataFolder().mkdir()) {
            File file = new File(plugin.getDataFolder(), "code.cfg");
            try {
                if (file.createNewFile()) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    /**
                     * In this case, we know that a database file didn't exist so we create a new file and write default data passed by the constructor to it.
                     */
                    writer.write("code=" + "pkvWUa46");
                    writer.flush();
                    writer.close();
                    code.setCode("pkvWUa46");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(plugin.getDataFolder(), "code.cfg");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String[] line1 = reader.readLine().split("=");
                if (line1.length < 2) {
                    code.setCode("null");
                } else {
                    code.setCode(line1[1]);
                }
                /**
                 * In this case, we know that a data file already exists, so we read it and update the default data passed by the constructor with the new read data.
                 */
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
