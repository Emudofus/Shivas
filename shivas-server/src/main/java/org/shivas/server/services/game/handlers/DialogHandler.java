package org.shivas.server.services.game.handlers;

import org.shivas.data.entity.Action;
import org.shivas.data.entity.NpcAnswer;
import org.shivas.data.entity.NpcQuestion;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.npcs.GameNpc;
import org.shivas.server.core.npcs.NpcDialogInteraction;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/10/12
 * Time: 18:06
 */
public class DialogHandler extends AbstractBaseHandler<GameClient> {
    public DialogHandler(GameClient client) {
        super(client);
    }

    @Override
    public void init() throws Exception { }

    @Override
    public void onClosed() { }

    @Override
    public void handle(String message) throws Exception {
        String[] args;
        switch (message.charAt(1)) {
        case 'C':
            parseStartDialogMessage(Integer.parseInt(message.substring(2)));
            break;

        case 'R':
            args = message.substring(2).split("\\|");
            parseAnswerMessage(Integer.parseInt(args[1]));
            break;

        case 'V':
            parseCloseDialogMessage();
            break;
        }
    }

    private void parseStartDialogMessage(int npcId) throws CriticalException, InteractionException {
        GameActor actor = client.player().getLocation().getMap().get(npcId);
        assertTrue(actor != null, "unknown npc %d", npcId);
        assertTrue(actor instanceof GameNpc, "actor %d is not a npc", npcId);

        client.interactions().push(new NpcDialogInteraction(client, (GameNpc) actor)).begin();
    }

    private void parseAnswerMessage(int answerId) throws CriticalException, InteractionException {
        NpcDialogInteraction dialog = client.interactions().current(NpcDialogInteraction.class);

        NpcQuestion question = dialog.getCurrentQuestion();
        NpcAnswer answer = question.getAnswer(answerId);
        assertTrue(answer != null, "unknown answer %d", answerId);

        for (Action action : answer.getActions()) {
            if (action.able(client)) {
                action.apply(client);
            }
        }

        if (dialog.getCurrentQuestion() == question) {
            dialog.end(); // stop the dialog if current question has not changed
        }
    }

    private void parseCloseDialogMessage() throws InteractionException {
        client.interactions().remove(NpcDialogInteraction.class).end();
    }
}
