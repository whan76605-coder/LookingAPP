package com.xhsapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xhsapp.entity.Message;
import com.xhsapp.mapper.MessageMapper;
import com.xhsapp.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageMapper.selectList(
                new LambdaQueryWrapper<Message>().orderByDesc(Message::getId));
    }

    @Override
    public void markAsRead(Integer id) {
        messageMapper.update(null,
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, id)
                        .set(Message::getIsRead, true));
    }

    @Override
    public int getUnreadCount() {
        return messageMapper.selectCount(
                new LambdaQueryWrapper<Message>().eq(Message::getIsRead, false)).intValue();
    }
}
