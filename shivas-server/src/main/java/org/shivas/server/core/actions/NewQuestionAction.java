package org.shivas.server.core.actions;

import org.shivas.data.Container;
import org.shivas.data.entity.Action;
import org.shivas.data.entity.NpcQuestion;
import org.shivas.server.core.npcs.NpcDialogInteraction;
import org.shivas.server.services.game.GameClient;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/10/12
 * Time: 18:38
 */
public class NewQuestionAction implements Action {
    public static final int TYPE = 5;

    public static NewQuestionAction make(Map<String, String> params, Container ctner) {
        int questionId = Integer.parseInt(params.get("question"));
        return new NewQuestionAction(questionId);
    }

    private final Integer questionId;

    public NewQuestionAction(Integer questionId) {
        this.questionId = questionId;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public boolean able(Object target) {
        return target instanceof GameClient;
    }

    @Override
    public void apply(Object target) {
        GameClient client = (GameClient) target;
        NpcDialogInteraction dialog = client.interactions().current(NpcDialogInteraction.class);

        NpcQuestion question = dialog.getNpc().getQuestions().get(questionId);
        dialog.setCurrentQuestion(question);
    }
}