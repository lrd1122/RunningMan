package gx.lrd1122.PlayerControl;

import gx.lrd1122.GameStates.PlayerData;
import gx.lrd1122.GameStates.PlayerState;
import gx.lrd1122.RunningMan;
import org.bukkit.entity.Player;

public class ShowDeathInGame {
    public static void ShowDeathInGame(Player player)
    {
        PlayerData data = PlayerData.getDataByName(player.getName());
        if(data.isShowdeathingame()) {
            data.setShowdeathingame(false);
            player.sendMessage(RunningMan.MessageConfig.getString("ShowDeathInGameClose").replace("&", "§"));
            return;
        }
        if(!data.isShowdeathingame()) {
            data.setShowdeathingame(true);
            player.sendMessage(RunningMan.MessageConfig.getString("ShowDeathInGameOpen").replace("&", "§"));
            return;
        }
    }
}
