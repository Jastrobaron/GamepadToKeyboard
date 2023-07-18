package xyz.rtsvk.gamepad;

public class Binding {

    private long lastUsed;
    private int defaultCooldown;

    public Binding(int defaultCooldown) {
        this.defaultCooldown = defaultCooldown;
        this.lastUsed = System.currentTimeMillis();
    }

    public void used() {
        this.lastUsed = System.currentTimeMillis();
    }

    public void lockFor(int ms) {
        this.lastUsed += ms;
    }

    public boolean isAvailable() {
        return this.lastUsed < System.currentTimeMillis();
    }

    public int getDefaultCooldown() {
        return this.defaultCooldown;
    }
}
