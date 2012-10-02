package org.shivas.data.entity;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/09/12
 * Time: 14:41
 */
public class NpcQuestion implements Serializable {
    private static final long serialVersionUID = 8993235643132285240L;

    private int id;
    private Map<Integer, NpcAnswer> answers = Maps.newHashMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, NpcAnswer> getAnswers() {
        return answers;
    }

    public NpcAnswer getAnswer(Integer id) {
        return answers.get(id);
    }

    public void setAnswers(Map<Integer, NpcAnswer> answers) {
        this.answers = answers;
    }

    public NpcQuestion addAnswer(NpcAnswer answer) {
        answers.put(answer.getId(), answer);
        return this;
    }
}
