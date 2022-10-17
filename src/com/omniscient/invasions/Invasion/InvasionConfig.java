package com.omniscient.invasions.Invasion;

public class InvasionConfig {
    private int duration = 30*60;
    private int cooldown = 6*60*60;

    public int getDuration() {
        return duration;
    }
    public int getCooldown() {
        return cooldown;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
