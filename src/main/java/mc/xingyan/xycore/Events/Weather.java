package mc.xingyan.xycore.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        event.getWorld().setStorm(false);
        event.getWorld().setWeatherDuration(0);
    }

}
