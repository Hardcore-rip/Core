package rip.hardcore.basic.commands.tags;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.hardcore.basic.manager.TagManager;
import rip.hardcore.basic.storage.Tags;
import rip.hardcore.filter.util.TranslationKt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommandAlias("tag|tags")
public class TagCommand extends BaseCommand {

    TagManager manager;
    Tags tags;
    TagMenu menu;

    public TagCommand(TagManager manager, TagMenu tagMenu, Tags tags) {
        this.manager = manager;
        this.menu = tagMenu;
        this.tags = tags;
    }

    @Default
    public void onCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationKt.translate("&cError: Console cannot execute this command!"));
            return;
        }
        menu.OpenGUI((Player) sender,0);
    }

    @Subcommand("create")
    @CommandPermission("basic.admin")
    public void createTag(CommandSender sender, @Optional String[] args) {
        String tag = args[0].toLowerCase();
        String display = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (args.length != 2){
            sender.sendMessage(TranslationKt.translate("&cUsage: /tag create <tag> <display>"));
            return;
        }
        if (tags.tagList().contains(tag)) {
            sender.sendMessage(TranslationKt.translate("&cError: Tag already exists!"));
            return;
        }
        tags.setTag(tag, TranslationKt.translate(display));
        sender.sendMessage(TranslationKt.translate("&#46e086Successfully created the " + tag + " tag!"));
    }

    @Subcommand("delete")
    @CommandPermission("basic.admin")
    public void deleteTag(CommandSender sender, @Optional String[] args) {
        if (args.length != 1) {
            sender.sendMessage(TranslationKt.translate("&cUsage: /tag delete <tag>"));
            return;
        }
        String tag = args[0].toLowerCase();
        if (!tags.tagList().contains(tag)) {
            sender.sendMessage(TranslationKt.translate("&cError: Tag does not exist!"));
            return;
        }
        tags.deleteTag(tag);
        sender.sendMessage(TranslationKt.translate("&#46e086Deleted the " + tag + " tag!"));
    }

    @Subcommand("grant")
    @CommandPermission("basic.admin")
    @CommandCompletion("@players")
    public void grantTag(CommandSender sender, @Optional String[] args) {
        if (args.length != 2) {
            sender.sendMessage(TranslationKt.translate("&cUsage: /tag grant <player> <tag>"));
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        String tag = args[1].toLowerCase();
        if (!tags.tagList().contains(tag)) {
            sender.sendMessage(TranslationKt.translate("&cError: Tag does not exist!"));
            return;
        }

        if (!player.hasPlayedBefore()) {
            sender.sendMessage(TranslationKt.translate("&cError: Player has not joined before!"));
            return;
        }
        try {
            if ((manager.hasTag(player.getUniqueId(), tag))) {
                sender.sendMessage(TranslationKt.translate("&cError: Player already has the tag!"));
                return;
            }
            manager.addTags(player.getUniqueId(), tag);
            if (player.isOnline()) {
                Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
                onlinePlayer.sendMessage(TranslationKt.translate("&#46e086You were granted the " + tag + " tag!"));
            }
            sender.sendMessage(TranslationKt.translate("&#46e086Granted " + player.getName() + " the " + tag + " tag!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subcommand("revoke")
    @CommandPermission("basic.admin")
    @CommandCompletion("@players")
    public void revokeTag(CommandSender sender, @Optional String[] args) {
        if (args.length != 2) {
            sender.sendMessage(TranslationKt.translate("&cUsage: /tag revoke <player> <tag>"));
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        String tag = args[1].toLowerCase();

        if (!tags.tagList().contains(tag)) {
            sender.sendMessage(TranslationKt.translate("&cError: Tag does not exist!"));
            return;
        }

        if (!player.hasPlayedBefore()) {
            sender.sendMessage(TranslationKt.translate("&cPlayer does not have this tag!"));
            return;
        }
        try {
            if (!(manager.hasTag(player.getUniqueId(), tag))) {
                sender.sendMessage(TranslationKt.translate("&cError: Player does not have this tag!"));
                return;
            }

            manager.removeTags(player.getUniqueId(), tag);
            sender.sendMessage(TranslationKt.translate("&#46e086You revoked the " + tag + " tag from " + player.getName() + "!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subcommand("list")
    public void listTag(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(TranslationKt.translate("&#d43370&lTAGS"));
        sender.sendMessage(" ");
        for (String test : tags.tagList()){
            sender.sendMessage(TranslationKt.translate(" &f» &#d43370" + test));
        }
        if (tags.tagList().isEmpty()){
            sender.sendMessage(TranslationKt.translate("&cNo tags found!"));
        }
        sender.sendMessage(" ");

    }
}
