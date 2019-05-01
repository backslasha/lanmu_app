package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

public class ConversationCard {

    @Expose
    private UserCard talkTo;

    @Expose
    private MessageCard recentMsg;

    public UserCard getTalkTo() {
        return talkTo;
    }

    public void setTalkTo(UserCard talkTo) {
        this.talkTo = talkTo;
    }

    public MessageCard getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(MessageCard recentMsg) {
        this.recentMsg = recentMsg;
    }
}
