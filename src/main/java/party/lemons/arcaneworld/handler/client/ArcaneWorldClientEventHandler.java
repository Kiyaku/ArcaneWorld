package party.lemons.arcaneworld.handler.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.arcaneworld.ArcaneWorld;
import party.lemons.arcaneworld.item.ArcaneWorldItems;
import party.lemons.arcaneworld.item.impl.ItemRecaller;

/**
 * Created by Sam on 19/09/2018.
 */
@Mod.EventBusSubscriber(modid = ArcaneWorld.MODID,  value = Side.CLIENT)
public class ArcaneWorldClientEventHandler
{
    public static EntityItem entityItem = null;

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;

        renderRecallerHand(minecraft, player, EnumHand.MAIN_HAND, event.getPartialTicks());
        renderRecallerHand(minecraft, player, EnumHand.OFF_HAND, event.getPartialTicks());
    }

    public static void renderRecallerHand(Minecraft mc, EntityPlayerSP player, EnumHand hand, float partialTicks)
    {
        ItemStack stack = player.getHeldItem(hand);

        if(stack.getItem() == ArcaneWorldItems.RECALLER)
        {
            if(ItemRecaller.getPosition(stack) != null && ItemRecaller.getDimension(stack) != Integer.MAX_VALUE)
            {
                BlockPos pos = ItemRecaller.getPosition(stack);

                if(entityItem == null)
                {
                    entityItem = new EntityItem(mc.world, 0, 0, 0, new ItemStack(ArcaneWorldItems.RECALL_EYE));
                    entityItem.setInfinitePickupDelay();
                    entityItem.setNoDespawn();
                    entityItem.setGlowing(true);
                }
                double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
                double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
                double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
                GlStateManager.translate(-playerX, -playerY, -playerZ);
                entityItem.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
                mc.getRenderManager().renderEntity(entityItem, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 0, partialTicks, false);
                GlStateManager.translate(0,0,0);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(entityItem != null)
            entityItem.onUpdate();
    }
}
