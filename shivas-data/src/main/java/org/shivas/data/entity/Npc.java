package org.shivas.data.entity;

import org.shivas.protocol.client.enums.OrientationEnum;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/09/12
 * Time: 18:25
 */
public class Npc implements Serializable {
    private static final long serialVersionUID = -903644772609069966L;

    private int id;
    private NpcTemplate template;
    private MapTemplate map;
    private short cell;
    private OrientationEnum orientation;
    private NpcQuestion startQuestion;
    private Map<Integer, NpcQuestion> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NpcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NpcTemplate template) {
        this.template = template;
    }

    public MapTemplate getMap() {
        return map;
    }

    public void setMap(MapTemplate map) {
        this.map = map;
    }

    public short getCell() {
        return cell;
    }

    public void setCell(short cell) {
        this.cell = cell;
    }

    public OrientationEnum getOrientation() {
        return orientation;
    }

    public void setOrientation(OrientationEnum orientation) {
        this.orientation = orientation;
    }

    public NpcQuestion getStartQuestion() {
        return startQuestion;
    }

    public void setStartQuestion(NpcQuestion startQuestion) {
        this.startQuestion = startQuestion;
    }

    public Map<Integer, NpcQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<Integer, NpcQuestion> questions) {
        this.questions = questions;
    }
}
