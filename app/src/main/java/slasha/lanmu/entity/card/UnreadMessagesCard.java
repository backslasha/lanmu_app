package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UnreadMessagesCard {
    @Expose
    private UserCard talkTo;
    @Expose
    private List<MessageCard> unreadMsgCards;

    public UserCard getTalkTo() {
        return talkTo;
    }

    public void setTalkTo(UserCard talkTo) {
        this.talkTo = talkTo;
    }

    public List<MessageCard> getUnreadMsgCards() {
        return unreadMsgCards;
    }

    public void setUnreadMsgCards(List<MessageCard> unreadMsgCards) {
        this.unreadMsgCards = unreadMsgCards;
    }
}
