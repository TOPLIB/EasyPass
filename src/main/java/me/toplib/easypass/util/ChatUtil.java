package me.toplib.easypass.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.toplib.easypass.EasyPass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F\0-9]{6}");

    public static String color(String message){
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace("&#", "x");
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch)
                builder.append("&").append(c);
            message = message.replace(hexCode, builder.toString());
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void checkUpdate(Player p) throws URISyntaxException, IOException, InterruptedException {
        if(EasyPass.getInstance().getConfig().getBoolean("checkForUpdates")){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.github.com/repos/TOPLIB/anarchyutils/releases/latest"))
                    .GET()
                    .build();

            String body = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            JsonObject json = new JsonParser().parse(body).getAsJsonObject();

            String latestVersion = json.get("name").getAsString();
            String downloadLink = json
                    .getAsJsonArray("assets")
                    .get(0).getAsJsonObject()
                    .get("browser_download_url").getAsString();

            if (!EasyPass.getInstance().getDescription().getVersion().equals(latestVersion)) {
                EasyPass.getInstance().getLogger().warning("New version is available!");
                EasyPass.getInstance().getLogger().warning("Latest version: " + latestVersion + " Your version: " + EasyPass.getInstance().getDescription().getVersion());
                p.sendMessage(color("&fNew version! &c(Only for admins)"));
                p.sendMessage(color("&fLatest version: &a" + latestVersion + " &fYour version: &c" + EasyPass.getInstance().getDescription().getVersion()));
                p.sendMessage(color("&fYou can download update on: Github, BlackMinecraft, Spigot"));
            }
        }
    }
}
