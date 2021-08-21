package gx.lrd1122.ReadConfig;

import org.bukkit.configuration.file.YamlConfiguration;

public class GetConfig {
    public static String getConfigString(YamlConfiguration config, String string)
    {
        return config.getString(string);
    }
    public static String getConfigString(YamlConfiguration config, String section, String string)
    {
        return config.getConfigurationSection(section).getString(string);
    }
    public static Boolean getConfigBoolean(YamlConfiguration config, String string)
    {
        return config.getBoolean(string);
    }
}
