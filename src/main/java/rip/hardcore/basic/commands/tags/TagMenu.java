package rip.hardcore.basic.commands.tags;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import rip.hardcore.basic.manager.TagManager;
import rip.hardcore.basic.storage.Tags;
import rip.hardcore.filter.util.TranslationKt;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static rip.hardcore.basic.Basic.spiGUI;


public class TagMenu {

    private final TagManager playerData;
    private final int ITEMS_PER_PAGE = 28; // 7 items per row, 4 rows for tags
    private final Tags tagManager;

    public TagMenu(TagManager playerData, Tags tagManager) {
        this.playerData = playerData;
        this.tagManager = tagManager;
    }

    public void OpenGUI(Player player, Integer page){

        SGMenu tagGUI = spiGUI.create("Tags - Page " + (page + 1), 6);

        SGButton border = new SGButton(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name(" ")
                .lore("")
                .build());
        for (int i = 0; i < 9; i++) {
            tagGUI.setButton(i, border);
        }
        for (int i = 45; i < 54; i++) {
            tagGUI.setButton(i, border);
        }
        for (int i = 0; i <= 45; i += 9) {
            tagGUI.setButton(i, border);
        }
        for (int i = 8; i <= 53; i += 9) {
            tagGUI.setButton(i, border);
        }
        try{
            List<String> playerTags = playerData.getTags(player.getUniqueId());
            int totalItems = playerTags.size();
            int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
            int start = page * ITEMS_PER_PAGE;
            int end = Math.min(start + ITEMS_PER_PAGE, totalItems);
            int slot = 10;
            for (int i = start; i < end; i++) {
                String tagName = playerTags.get(i);
                String toggleMessage = "";
                boolean enabled = false;
                try {
                    if (Objects.equals(playerData.getTag(player), tagName)){
                        enabled = true;
                        toggleMessage = TranslationKt.translate("&cClick to disable " + tagName);
                    } else {
                        enabled = false;
                        toggleMessage = TranslationKt.translate("&#46e086Click to enable " + tagName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean finalEnabled = enabled;
                String lores = (
                        TranslationKt.translate("\n&#e8cb61❙ &fTag: " + tagManager.getTag(tagName) + "\n&#e8cb61❙ &fName: " + tagName + "\n\n&#e8cb61❙ " + toggleMessage)
                        );
                String[] lines = lores.split("\n");

                /*
                    Redo lore system to make it better
                    Ideas:
                        - Create a custom format
                        - Use mini message
                */

                String properCase = tagName;
                if (tagName.length() > 1){
                    properCase = tagName.replaceFirst(properCase, String.valueOf(tagName.charAt(0)).toUpperCase()) + tagName.substring(1);
                } else {
                    properCase = tagName.toUpperCase();
                }
                SGButton tagData = new SGButton(new ItemBuilder(Objects.requireNonNull(Material.getMaterial(String.valueOf(Material.NAME_TAG))))
                        .name(TranslationKt.translate("&#e8cb61" + properCase + " Tag"))
                        .lore(lines)
                        .build()
                ).withListener((InventoryClickEvent event) -> {
                    try {
                        if (!Objects.equals(playerData.getTag(player), tagName)){
                            playerData.updateTag(player, tagName);
                            player.sendMessage(TranslationKt.translate("&#46e086Enabled the " + tagName + " tag!"));
                            player.closeInventory();
                        } else {
                            playerData.updateTag(player, "");
                            player.sendMessage(TranslationKt.translate("&cDisabled the " + tagName + " tag!"));
                            player.closeInventory();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                });
                while ((slot % 9 == 0 || slot % 9 == 8) && slot < 45) {
                    slot++;
                }
                if (slot >= 45) {
                    break;
                }
                tagGUI.setButton(slot, tagData);
                slot++;
            }


            SGButton close = new SGButton(new ItemBuilder(Objects.requireNonNull(Material.getMaterial(String.valueOf((Material.BARRIER)))))
                    .name(TranslationKt.translate("&cClick to close!"))
                    .build()
            ).withListener((inventoryClickEvent ->{
                player.closeInventory();
            }));

            tagGUI.setButton(49, close);

            if (page > 0) {

                SGButton previousPage = new SGButton(new ItemBuilder(Objects.requireNonNull(Material.getMaterial(String.valueOf(Material.ARROW))))
                        .name(TranslationKt.translate("&7&oPrevious page"))
                        .build()
                ).withListener((inventoryClickEvent -> {
                    OpenGUI(player, page - 1);
                }));

                tagGUI.setButton(48, previousPage);

            }

            if (page < totalPages - 1) {
                SGButton nextPage = new SGButton(new ItemBuilder(Objects.requireNonNull(Material.getMaterial(String.valueOf(Material.ARROW))))
                        .name(TranslationKt.translate("&7&oNext page"))
                        .build()
                ).withListener((inventoryClickEvent -> {
                    OpenGUI(player, page + 1);
                }));
                tagGUI.setButton(50, nextPage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        player.openInventory(tagGUI.getInventory());

    }
}
