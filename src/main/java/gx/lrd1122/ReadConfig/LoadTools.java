package gx.lrd1122.ReadConfig;

import gx.lrd1122.RunningMan;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LoadTools {
    public static List<String> ToolList = new ArrayList<>();
    public static void LoadAvailableTools()
    {
        ConfigurationSection section = RunningMan.Config.getConfigurationSection("GameTools");
        Set<String> setlist = section.getKeys(true);
        List<String> list = new ArrayList<>(setlist);
        for(int i =0 ; i < list.size(); i ++)
        {
            if(section.getBoolean(list.get(i)))
            {
                ToolList.add(list.get(i));
            }
        }
    }
}
