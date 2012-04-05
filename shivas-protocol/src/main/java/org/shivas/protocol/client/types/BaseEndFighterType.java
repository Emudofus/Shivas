package org.shivas.protocol.client.types;

/**
 * User: Blackrush
 * Date: 21/12/11
 * Time: 18:16
 * IDE : IntelliJ IDEA
 */
public class BaseEndFighterType {
    private long id;
    private String name;
    private int level, life;
    private boolean alive;
    private long floorExperienceMin, floorExperienceMax, experience;
    private long earnedExperience, earnedGuildExperience, earnedMountExperience;
    //todo earned items
    private long earnedKamas;

    public BaseEndFighterType() {

    }

    public BaseEndFighterType(long id, String name, int level, int life, boolean alive, long floorExperienceMin, long floorExperienceMax, long experience, long earnedExperience, long earnedGuildExperience, long earnedMountExperience, long earnedKamas) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.life = life;
        this.alive = alive;
        this.floorExperienceMin = floorExperienceMin;
        this.floorExperienceMax = floorExperienceMax;
        this.experience = experience;
        this.earnedExperience = earnedExperience;
        this.earnedGuildExperience = earnedGuildExperience;
        this.earnedMountExperience = earnedMountExperience;
        this.earnedKamas = earnedKamas;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public long getFloorExperienceMin() {
        return floorExperienceMin;
    }

    public void setFloorExperienceMin(long floorExperienceMin) {
        this.floorExperienceMin = floorExperienceMin;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public long getFloorExperienceMax() {
        return floorExperienceMax;
    }

    public void setFloorExperienceMax(long floorExperienceMax) {
        this.floorExperienceMax = floorExperienceMax;
    }

    public long getEarnedExperience() {
        return earnedExperience;
    }

    public void setEarnedExperience(long earnedExperience) {
        this.earnedExperience = earnedExperience;
    }

    public long getEarnedGuildExperience() {
        return earnedGuildExperience;
    }

    public void setEarnedGuildExperience(long earnedGuildExperience) {
        this.earnedGuildExperience = earnedGuildExperience;
    }

    public long getEarnedMountExperience() {
        return earnedMountExperience;
    }

    public void setEarnedMountExperience(long earnedMountExperience) {
        this.earnedMountExperience = earnedMountExperience;
    }

    public long getEarnedKamas() {
        return earnedKamas;
    }

    public void setEarnedKamas(long earnedKamas) {
        this.earnedKamas = earnedKamas;
    }
}
