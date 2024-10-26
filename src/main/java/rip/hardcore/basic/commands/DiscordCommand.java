package rip.hardcore.basic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.hardcore.filter.util.TranslationKt;

@CommandAlias("discord")
public class DiscordCommand extends BaseCommand {

    @Default
    public void onCommand(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(TranslationKt.translate("&#6435b5&lDiscord &f» discord.gg/dupe") );
        sender.sendMessage(" ");
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendTitle(TranslationKt.translate("&#6435b5&lDISCORD"), TranslationKt.translate("&fJoin at &#6435b5discord.gg/dupe"));
        }
        return;
    }
}
