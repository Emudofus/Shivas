package org.shivas.core.core.npcs;

import org.shivas.data.entity.NpcQuestion;
import org.shivas.protocol.client.formatters.DialogGameMessageFormatter;
import org.shivas.core.core.interactions.AbstractInteraction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.InteractionType;
import org.shivas.core.services.game.GameClient;
import org.shivas.core.utils.Converters;

import static org.shivas.common.collections.CollectionQuery.from;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/10/12
 * Time: 18:11
 */
public class NpcDialogInteraction extends AbstractInteraction {
    private final GameClient client;
    private final GameNpc npc;

    private NpcQuestion currentQuestion;

    public NpcDialogInteraction(GameClient client, GameNpc npc) {
        this.client = client;
        this.npc = npc;
        this.currentQuestion = npc.getStartQuestion();
    }

    protected void sendQuestion() {
        client.write(DialogGameMessageFormatter.questionMessage(
                currentQuestion.getId(),
                null,
                from(currentQuestion.getAnswers().values()).transform(Converters.NPCANSWER_TO_ID).computeList()
        ));
    }

    public GameNpc getNpc() {
        return npc;
    }

    public NpcQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(NpcQuestion currentQuestion) {
        this.currentQuestion = currentQuestion;
        if (this.currentQuestion != null) {
            sendQuestion();
        }
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.NPC_DIALOG;
    }

    @Override
    public void begin() throws InteractionException {
        client.write(DialogGameMessageFormatter.dialogSuccessMessage(npc.getTemplate().getId()));
        sendQuestion();
    }

    @Override
    public void cancel() throws InteractionException {
        end();
    }

    @Override
    protected void internalEnd() throws InteractionException {
        client.write(DialogGameMessageFormatter.dialogEndMessage());
    }
}
