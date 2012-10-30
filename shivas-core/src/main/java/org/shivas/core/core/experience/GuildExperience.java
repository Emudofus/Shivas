package org.shivas.core.core.experience;

import static java.lang.Math.abs;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 11:56
 */
public class GuildExperience implements Experience<Long> {
    private org.shivas.data.entity.Experience template;
    private long experience;

    public GuildExperience(org.shivas.data.entity.Experience template) {
        this.template = template;
        this.experience = template.getGuild();
    }

    public GuildExperience(org.shivas.data.entity.Experience template, long experience) {
        this.template = template;
        this.experience = experience;
    }

    @Override
    public short level() {
        return template.getLevel();
    }

    @Override
    public void addLevel(short level) {
        for (int i = 0; i < level; ++i) {
            template = template.next();
        }
        experience = template.getGuild();
    }

    @Override
    public void removeLevel(short level) {
        for (int i = 0; i < level; ++i) {
            template = template.previous();
        }
        experience = template.getGuild();
    }

    @Override
    public Long current() {
        return experience;
    }

    @Override
    public Long min() {
        return template.getGuild();
    }

    @Override
    public Long max() {
        return template.next().getGuild();
    }

    @Override
    public void plus(Long experience) {
        this.experience += abs(experience);

        while (this.experience > max()) {
            template = template.next();
        }
    }

    @Override
    public void minus(Long experience) {
        this.experience -= abs(experience);

        while (this.experience < min()) {
            template = template.previous();
        }
    }
}
