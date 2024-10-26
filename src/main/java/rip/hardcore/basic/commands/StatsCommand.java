package rip.hardcore.basic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import rip.hardcore.basic.manager.LifeManager;
import rip.hardcore.filter.util.TranslationKt;

@CommandAlias("stats|whois")
public class StatsCommand extends BaseCommand {

    private LifeManager manager;

    public StatsCommand(LifeManager manager) {
        this.manager = manager;
    }

    @Default
    @CommandCompletion("@players")
    public void PlayerStats(Player player, @Optional String t) {
        OfflinePlayer target = null;
        player.sendMessage(TranslationKt.translate(" "));
        if (t == null) {
            player.sendMessage(TranslationKt.translate("&e&lYour Statistic"));
            target = player;
        } else {
            target = Bukkit.getOfflinePlayer(t);
            if (target.getName().indexOf((target.getName().length() - 1)) == 's'){
                player.sendMessage(TranslationKt.translate("&e&l " + target.getName() + "' Statistic"));
            } else {
                player.sendMessage(TranslationKt.translate("&e&l " + target.getName() + "'s Statistic"));
            }
        }

        player.sendMessage(TranslationKt.translate(" "));
        player.sendMessage(TranslationKt.translate("  &f» &eKills: &f" + target.getStatistic(Statistic.PLAYER_KILLS)));
        player.sendMessage(TranslationKt.translate("  &f» &eDeaths: &f" + target.getStatistic(Statistic.DEATHS)));
        player.sendMessage(TranslationKt.translate("  &f» &eK/D: &f" + ((1.0 * target.getStatistic(Statistic.PLAYER_KILLS))/(1.0 * target.getStatistic(Statistic.DEATHS)))));
        player.sendMessage(TranslationKt.translate("  &f» &eLives: &f" + manager.getLives(target.getUniqueId())));
        player.sendMessage(TranslationKt.translate(" "));
    }

}
