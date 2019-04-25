package slasha.lanmu.business.chat;

import java.util.List;

import slasha.lanmu.entity.local.Message;

class ChatModelImpl implements ChatContract.ChatModel {

    @Override
    public List<Message> offer(long myId, long otherId) {
        // TODO: 2019/3/15 query data from somewhere
        return null;
    }
}
