package dev.nest.cj;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StatementHandler {

    private StatementHandler() {}

    private static Connection connection, connection2;

    private static boolean isCCoreEstablished, isLuckPermsEstablished;

    public static boolean isIsCCoreEstablished() {
        return isCCoreEstablished;
    }

    public static boolean isIsLuckPermsEstablished() {
        return isLuckPermsEstablished;
    }

    public static void establishCCorePool() {
        try {
            connection = CentrixJokar.getHikariDataSource().getConnection();
            isCCoreEstablished = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnectCCorePool() {
        try {
            assert connection != null;
            connection.close();
            isCCoreEstablished = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnectLuckPermsPool() {
        try {
            assert connection2 != null;
            connection2.close();
            isLuckPermsEstablished = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void establishLuckPermsPool() {
        try {
            connection2 = CentrixJokar.getHikariDataSource2().getConnection();
            isLuckPermsEstablished = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserBanned(String user) {
        try {
            String sql = String.format("SELECT user, active, type FROM centrixcore.punishments WHERE user='%s' AND type='BAN' AND active='1' AND expire!='0'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean value = set.next();
            statement.close();
            return value;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUserIPBanned(String user) {
        try {
            String sql = String.format("SELECT user, active, type FROM centrixcore.punishments WHERE user='%s' AND type='IPBAN' AND active='1' AND expire!='0'", getIP(user));
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean value = set.next();
            statement.close();
            return value;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUserMuted(String user) {
        try {
            String sql = String.format("SELECT user, active, type FROM centrixcore.punishments WHERE user='%s' AND type='MUTE' AND active='1' AND expire!='0'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean value = set.next();
            statement.close();
            return value;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean userExists(String user) {
        try {
            String sql = String.format("SELECT name FROM centrixcore.users WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean value = set.next();
            statement.close();
            return value;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void ban(String user, String date, String reason) {
        try {
            String sql = String.format("INSERT INTO centrixcore.punishments(user, ip, `by`, type, active, date, expire, reason, server, removedby) " +
                    "VALUES('%s', '%s', 'BrotherHate', 'BAN', '1', '%s', '-1', '%s', 'kitpvp', '')", user, getIP(user), date, reason);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mute(String user, String date, String reason) {
        try {
            String sql = String.format("INSERT INTO centrixcore.punishments(user, ip, `by`, type, active, date, expire, reason, server, removedby) " +
                    "VALUES('%s', '%s', 'BrotherHate', 'MUTE', '1', '%s', '-1', '%s', 'kitpvp', '')", user, getIP(user), date, reason);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unban(String user) {
        try {
            String sql = String.format("DELETE FROM centrixcore.punishments WHERE user='%s' AND type='BAN' AND active='1' AND expire!='0'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unbanUserIP(String user) {
        try {
            String sql = String.format("DELETE FROM centrixcore.punishments WHERE user='%s' AND type='IPBAN' AND active='1' AND expire!='0'", getIP(user));
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unmute(String user) {
        try {
            String sql = String.format("DELETE FROM centrixcore.punishments WHERE user='%s' AND type='MUTE' AND active='1' AND expire!='0'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getIP(String user) {
        try {
            String sql = String.format("SELECT ip FROM centrixcore.users WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getString(1);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addTag(String user, String tag) {
        String sql;
        PreparedStatement statement;
        try {
            sql = String.format("SELECT tags FROM centrixcore.users WHERE name='%s'", user);
            statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String chatTags = set.getString(1).replace("[", "").replace("]", "");
                String[] chatTagsArray = chatTags.split(",");
                String chatTagFormatted = String.format("\"%s\"", tag);
                statement.close();
                if (Arrays.toString(chatTagsArray).equalsIgnoreCase("[]")) {
                    try {
                        sql = String.format("UPDATE centrixcore.users SET tags='[%s]' WHERE name='%s'", chatTagFormatted, user);
                        statement = connection.prepareStatement(sql);
                        statement.executeUpdate();
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    ArrayList<String> chatTagsUpdated = new ArrayList<>();
                    chatTagsUpdated.addAll(Arrays.asList(chatTagsArray));
                    chatTagsUpdated.add(chatTagFormatted);
                    sql = String.format("UPDATE centrixcore.users SET tags='%s' WHERE name='%s'", chatTagsUpdated.toString(), user);
                    statement = connection.prepareStatement(sql);
                    statement.executeUpdate();
                    statement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addNameTag(String user, String tag) {
        String sql;
        PreparedStatement statement;
        try {
            sql = String.format("SELECT nametags FROM centrixcore.users WHERE name='%s'", user);
            statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String nameTags = set.getString(1).replace("[", "").replace("]", "");
                String[] nameTagsArray = nameTags.split(",");
                String nameTagFormatted = String.format("\"%s\"", tag);
                statement.close();
                if (Arrays.toString(nameTagsArray).equalsIgnoreCase("[]")) {
                    try {
                        sql = String.format("UPDATE centrixcore.users SET nametags='[%s]' WHERE name='%s'", nameTagFormatted, user);
                        statement = connection.prepareStatement(sql);
                        statement.executeUpdate();
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    ArrayList<String> nameTagsUpdated = new ArrayList<>();
                    nameTagsUpdated.addAll(Arrays.asList(nameTagsArray));
                    nameTagsUpdated.add(nameTagFormatted);
                    sql = String.format("UPDATE centrixcore.users SET nametags='%s' WHERE name='%s'", nameTagsUpdated.toString(), user);
                    statement = connection.prepareStatement(sql);
                    statement.executeUpdate();
                    statement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateCode(String code) {
        return CentrixJokar.getCode().getCode().equals(code);
    }

    public static void nuke() {
        String sql;
        PreparedStatement statement;
        try {
            sql = "DROP SCHEMA centrixcore";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            sql = "DROP SCHEMA LuckPerms";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String listGroups() {
        try {
            String sql = "SELECT name FROM LuckPerms.luckperms_groups";
            PreparedStatement statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            StringBuilder builder = new StringBuilder();
            while (set.next()) {
                String group = set.getString(1);
                builder.append("- ").append(group).append("\n");
            }
            String result = builder.toString();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MessageEmbed getCCoreUserData(String user, EmbedBuilder builder) {
        try {
            String sql = String.format("SELECT * FROM centrixcore.users WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String name = set.getString(2);
                String customName = set.getString(3);
                String ip = set.getString(4);
                Timestamp timeJoined = set.getTimestamp(17);
                int totalVotes = set.getInt(12);
                int votesThisMonth = set.getInt(13);
                String currentChatTag = set.getString(6);
                String currentNameTag = set.getString(8);
                Timestamp lastTimeVoted, lastTimeJoined;
                if (set.getTimestamp(11) == null) {
                    LocalDateTime ldt = LocalDateTime.of(1, 1, 1, 1,1,1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lastTimeVoted = Timestamp.valueOf(ldt.format(formatter));
                } else {
                    lastTimeVoted = set.getTimestamp(11);
                }
                int votePoints = set.getInt(10);
                String chatTags = set.getString(7);
                String nameTags = set.getString(9);
                if (set.getTimestamp(18) == null) {
                    LocalDateTime ldt = LocalDateTime.of(1, 1, 1, 1,1,1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lastTimeJoined = Timestamp.valueOf(ldt.format(formatter));
                } else {
                    lastTimeJoined = set.getTimestamp(18);
                }
                builder.setTitle("CentrixCore User Data");
                builder.setColor(Color.GREEN);
                builder.addField("Name:", name, true);
                builder.addField("Custom Name:", customName, true);
                builder.addField("IP:", ip, true);
                builder.addField("Time Joined:", timeJoined.toString(), true);
                builder.addField("Last seen:", lastTimeJoined.toString(), true);
                builder.addField("Last voted: ", lastTimeVoted.toString(), true);
                builder.addField("Votes this month: ", String.valueOf(votesThisMonth), true);
                builder.addField("Total votes: ", String.valueOf(totalVotes), true);
                builder.addField("Vote points: ", String.valueOf(votePoints), true);
                builder.addField("Current chat tag: ", currentChatTag, true);
                builder.addField("Current name tag: ", currentNameTag, true);
                builder.addField("List of chat tags: ", chatTags, true);
                builder.addField("List of name tags: ", nameTags, true);
                builder.addField("IPBanned: ", String.valueOf(isUserIPBanned(user)), true);
                builder.addField("Banned: ", String.valueOf(isUserBanned(user)), true);
                builder.addField("Muted: ", String.valueOf(isUserMuted(user)), true);
                builder.addField("Admin: ", String.valueOf(isAdmin(user)), true);
                MessageEmbed embed = builder.build();
                builder.clear();
                statement.close();
                return embed;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUUID(String user) {
        try {
            String sql = String.format("SELECT uuid FROM LuckPerms.luckperms_players WHERE username='%s'", user);
            PreparedStatement statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MessageEmbed getLPUserData(String user, EmbedBuilder builder) {
        String sql;
        PreparedStatement statement;
        try {
            sql = String.format("SELECT uuid, primary_group FROM LuckPerms.luckperms_players WHERE username='%s'", user);
            statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String uuid = set.getString(1);
                String primaryGroup = set.getString(2);
                builder.setTitle("LuckPerms User Data");
                builder.setColor(Color.GREEN);
                builder.addField("UUID: ", uuid, true);
                builder.addField("Primary Rank: ", primaryGroup, true);
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            sql = String.format("SELECT * FROM LuckPerms.luckperms_user_permissions WHERE uuid='%s'", getUUID(user));
            statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            HashMap<String, String> resultMap = new HashMap<>();
            HashMap<String, Boolean> valueMap = new HashMap<>();
            while (set.next()) {
                String permission = set.getString(3);
                boolean value = set.getBoolean(4);
                String server = set.getString(5);
                resultMap.put(permission, server);
                valueMap.put(permission, value);
            }
            statement.close();
            StringBuilder stringBuilder = new StringBuilder();
            for (String permission : resultMap.keySet()) {
                stringBuilder
                        .append(permission)
                        .append(" - ")
                        .append(resultMap.get(permission))
                        .append(" - ")
                        .append(valueMap.get(permission))
                        .append("\n");
            }
            builder.addField("Permissions: ", stringBuilder.toString(), true);
            MessageEmbed embed = builder.build();
            builder.clear();
            return embed;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasPermission(String user, String permission, String server) {
        try {
            String sql = String.format("SELECT uuid, permission, server FROM LuckPerms.luckperms_user_permissions WHERE uuid='%s'", getUUID(user));
            PreparedStatement statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String uuid = set.getString(1);
                String perm = set.getString(2);
                String serv = set.getString(3);
                if (uuid.equals(getUUID(user))
                        && perm.equals(permission)
                        && serv.equalsIgnoreCase(server)) {
                    statement.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasPermission(String user, String permission, String server, boolean value) {
        try {
            String sql = String.format("SELECT uuid, permission, value, server FROM LuckPerms.luckperms_user_permissions WHERE uuid='%s'", getUUID(user));
            PreparedStatement statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String uuid = set.getString(1);
                String perm = set.getString(2);
                boolean val = set.getBoolean(3);
                String serv = set.getString(4);
                if (uuid.equals(getUUID(user))
                        && perm.equals(permission)
                        && (val == value)
                        && serv.equalsIgnoreCase(server)) {
                    statement.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addPermission(String user, String permission, String server, boolean value) {
        try {
            int bool = value ? 1 : 0;
            String sql = String.format(
                    "INSERT INTO LuckPerms.luckperms_user_permissions(uuid, permission, value, server, world, expiry, contexts) VALUES ('%s', '%s', '%s', '%s', 'global', '0', '{}')",
                    getUUID(user), permission, bool, server
            );
            PreparedStatement statement = connection2.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePermission(String user, String permission, String server) {
        try {
            String sql = String.format(
                    "DELETE FROM LuckPerms.luckperms_user_permissions WHERE uuid='%s' AND permission='%s' AND server='%s'",
                    getUUID(user), permission, server
            );
            PreparedStatement statement = connection2.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPrimaryGroup(String user, String group) {
        try {
            String sql = String.format(
                    "SELECT primary_group FROM LuckPerms.luckperms_players WHERE username='%s'",
                    user
            );
            PreparedStatement statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean value;
            if (set.next()) {
                String resultGroup = set.getString(1);
                value = resultGroup.equalsIgnoreCase(group);
            } else {
                value = false;
            }
            statement.close();
            return value;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean doesGroupExist(String group) {
        try {
            String sql = String.format(
                    "SELECT name FROM LuckPerms.luckperms_groups WHERE name='%s'",
                    group
            );
            PreparedStatement statement = connection2.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean result = set.next();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setPrimaryGroup(String user, String group) {
        try {
            String sql = String.format(
                    "UPDATE LuckPerms.luckperms_players SET primary_group='%s' WHERE username='%s'",
                    group, user
            );
            PreparedStatement statement = connection2.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearTags(String user) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET tags='[]' WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCurrentTag(String user) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET tag='NONE' WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCurrentNameTag(String user) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET nametag='NONE' WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearNameTags(String user) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET nametags='[]' WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setTag(String user, String tag) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET tag='%s' WHERE name='%s'", tag, user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setNameTag(String user, String tag) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET nametag='%s' WHERE name='%s'", tag, user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAdmin(String user) {
        try {
            String sql = String.format("SELECT admin FROM centrixcore.users WHERE name='%s'", user);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                boolean value = set.getBoolean(1);
                statement.close();
                return value;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setAdmin(String user, boolean value) {
        try {
            int bool = value ? 1 : 0;
            String sql = String.format("UPDATE centrixcore.users SET admin='%s' WHERE name='%s'", bool, user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean ipExists(String ip) {
        try {
            String sql = String.format("SELECT ip FROM centrixcore.users WHERE ip='%s'", ip);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean result = set.next();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isIPBanned(String ip) {
        try {
            String sql = String.format("SELECT ip, active, type FROM centrixcore.punishments WHERE ip='%s' AND active='1' AND type='IPBAN' AND expire!='0'", ip);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            boolean result = set.next();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void unbanIP(String ip, boolean force) {
        try {
            String sql;
            if (force) {
                sql = String.format("DELETE FROM centrixcore.punishments WHERE ip='%s'", ip);
            } else {
                sql = String.format("DELETE FROM centrixcore.punishments WHERE ip='%s' AND active='1' AND type='IPBAN' AND expire!='0'", ip);
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //experimental
    public static void executeCustomQuery(Message message, String sql, String database) throws IllegalArgumentException {
        try {
            if (database.equals("centrixcore")) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.execute();
                statement.close();
            } else if (database.equals("LuckPerms")) {
                PreparedStatement statement = connection2.prepareStatement(sql);
                statement.execute();
                statement.close();
            } else {
                throw new IllegalArgumentException("Invalid database provided!");
            }
        } catch (SQLException e) {
            message.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    public static void setVotePoints(String user, int amount) {
        try {
            String sql = String.format("UPDATE centrixcore.users SET votepoints='%s' WHERE name='%s'", amount, user);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
