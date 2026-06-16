package com.xhsapp.controller;

import com.xhsapp.common.Result;
import com.xhsapp.entity.Message;
import com.xhsapp.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /** GET /api/v1/messages */
    @GetMapping
    public Result<List<Message>> getAllMessages() {
        return Result.ok(messageService.getAllMessages());
    }

    /** PUT /api/v1/messages/{id}/read */
    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(@PathVariable Integer id) {
        messageService.markAsRead(id);
        return Result.ok();
    }

    /** GET /api/v1/messages/unread-count */
    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount() {
        return Result.ok(messageService.getUnreadCount());
    }
}
