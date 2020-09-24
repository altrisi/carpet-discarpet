package discarpet;

import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.value.*;
import discarpet.classes.embedField;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DiscordFunctions {
    public static void apply(Expression expr) {
        expr.addLazyFunction("sd_send_message", 2, (c, t, lv) -> {
            Value ret;
            if(Discarpet.discordEnabled) {
                String channel = ((LazyValue)lv.get(0)).evalValue(c).getString();
                String message = ((LazyValue)lv.get(1)).evalValue(c).getString();
                Discarpet.discordBot.sendMessage(channel, message);
                ret = Value.TRUE;
            } else {
                ret = Value.FALSE;
            }
            return (cc, tt) -> {
                return ret;
            };
        });


        expr.addLazyFunction("sd_message_buffer_size", 0, (c, t, lv) -> {
            Value ret;
            if(Discarpet.discordEnabled) {
                ret = new NumericValue(Discarpet.discordBot.messageBuffer.size());
            } else {
                ret = new NumericValue(-1);
            }
            return (cc, tt) -> {
                return ret;
            };
        });


        expr.addLazyFunction("sd_read_message_buffer", 0, (c, t, lv) -> {
            Value ret;
            if(Discarpet.discordEnabled && Discarpet.discordBot.messageBuffer.size() > 0) {
                Bot.message msg = Discarpet.discordBot.readMessageBuffer();
                Map<Value, Value> retMap = new HashMap<Value,Value>();
                retMap.put(new StringValue("content"),new StringValue(msg.content));
                retMap.put(new StringValue("author"),new StringValue(msg.author));
                retMap.put(new StringValue("channel"),new StringValue(msg.channel));
                ret = MapValue.wrap(retMap);
            } else {
                ret = Value.NULL;
            }
            return (cc, tt) -> {
                return ret;
            };
        });



        expr.addLazyFunction("sd_chat_buffer_size", 0, (c, t, lv) -> {
            Value ret;
            if(Discarpet.discordEnabled) {
                ret = new NumericValue(Discarpet.minecraftChat.chatBuffer.size());
            } else {
                ret = new NumericValue(-1);
            }
            return (cc, tt) -> {
                return ret;
            };
        });


        expr.addLazyFunction("sd_read_chat_buffer", 0, (c, t, lv) -> {
            Value ret;
            if(Discarpet.discordEnabled && Discarpet.minecraftChat.chatBuffer.size() > 0) {
                Chat.chat cht = Discarpet.minecraftChat.readChatBuffer();
                Map<Value, Value> retMap = new HashMap<Value,Value>();
                retMap.put(new StringValue("text"),new StringValue(cht.text));
                retMap.put(new StringValue("key"),new StringValue(cht.key));
                ret = MapValue.wrap(retMap);
            } else {
                ret = Value.NULL;
            }
            return (cc, tt) -> {
                return ret;
            };
        });
        expr.addLazyFunction("sd_set_channel_topic", 2, (c, t, lv) -> {
            if(Discarpet.discordEnabled) {
                String channel = ((LazyValue)lv.get(0)).evalValue(c).getString();
                String description = ((LazyValue)lv.get(1)).evalValue(c).getString();
                Discarpet.discordBot.setChannelTopic(channel,description);
            }
            return (cc, tt) -> {
                return Value.TRUE;
            };
        });

        expr.addLazyFunction("sd_set_activity", 2, (c, t, lv) -> {
            if(Discarpet.discordEnabled) {
                int type = (NumericValue.asNumber(((LazyValue)lv.get(0)).evalValue(c)).getInt());
                String text = (lv.get(1)).evalValue(c).getString();
                Discarpet.discordBot.setActivity(type,text);
            }
            return (cc, tt) -> {
                return Value.TRUE;
            };
        });

        expr.addLazyFunction("sd_set_status", 1, (c, t, lv) -> {
            if(Discarpet.discordEnabled) {
                int status = (NumericValue.asNumber(((LazyValue)lv.get(0)).evalValue(c)).getInt());
                Discarpet.discordBot.setStatus(status);
            }
            return (cc, tt) -> {
                return Value.TRUE;
            };
        });


        expr.addLazyFunction("sd_send_embed", 12, (c, t, lv) -> {
            if(Discarpet.discordEnabled) {
				/*List<Value> l = new ArrayList<>();
				Value v = ListValue.wrap(l);
				((ListValue)v).getItems().
				((MapValue)v).get(new NumericValue(2));
				//(NumericValue(((LazyValue)lv.get(0)).evalValue(c)).getInt());
				int status = (NumericValue.asNumber(((LazyValue)lv.get(0)).evalValue(c)).getInt());
				System.out.println(status);*/
                String channel = (lv.get(0)).evalValue(c).getString();
                String title = (lv.get(1)).evalValue(c).getString();
                String description = (lv.get(2)).evalValue(c).getString();
                String authorName = (lv.get(3)).evalValue(c).getString();
                String authorLink = (lv.get(4)).evalValue(c).getString();
                String authorAvatar = (lv.get(5)).evalValue(c).getString();

                Value fieldValueList = ((lv.get(6)).evalValue(c));
                List<embedField> fieldEmbedList = new ArrayList<>();
                if(fieldValueList.getTypeString() == "list") {
                    List<Value> fieldListValue = ((ListValue)fieldValueList).getItems();
                    for(int i = 0; i < fieldListValue.size();i++) {
                        List<Value> im = ((ListValue)(fieldListValue.get(i))).getItems();
                        if(im.size() == 3) {
                            fieldEmbedList.add(new embedField((NumericValue.asNumber(im.get(0))).getBoolean(),im.get(1).getString(),im.get(2).getString()));
                        }
                    }
                }


                Value colorValueList = ((lv.get(7)).evalValue(c));
                Color color = new Color(0,0,0);
                if(colorValueList.getTypeString() == "list") {
                    List<Value> colorListValue = ((ListValue)colorValueList).getItems();
                    if(colorListValue.size() == 3) {
                        color = new Color((NumericValue.asNumber(colorListValue.get(0))).getInt(),(NumericValue.asNumber(colorListValue.get(1))).getInt(),(NumericValue.asNumber(colorListValue.get(2))).getInt());
                    }
                }

                String footerName = (lv.get(8)).evalValue(c).getString();
                String footerAvatar = (lv.get(9)).evalValue(c).getString();
                String image = (lv.get(10)).evalValue(c).getString();
                String thumbnail = (lv.get(11)).evalValue(c).getString();
                Discarpet.discordBot.sendEmbed(channel, title, description, authorName, authorLink, authorAvatar, fieldEmbedList, color, footerName, footerAvatar, image, thumbnail);
            }
            return (cc, tt) -> {
                return Value.TRUE;
            };
        });
    }
}
