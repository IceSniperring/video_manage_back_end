package com.videomanage.video_manage_after.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.videomanage.video_manage_after.entity.History;
import com.videomanage.video_manage_after.entity.HistoryRecordDTO;
import com.videomanage.video_manage_after.mapper.HistoryMapper;
import com.videomanage.video_manage_after.status.DeleteStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin
public class HistoryController {
    private final HistoryMapper historyMapper;

    @Autowired
    public HistoryController(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    @GetMapping("/api/getHistory")
    public List<HistoryRecordDTO> getHistoryByUid(@RequestParam(value = "uid") int uid) {
        return historyMapper.selectHistoryInfo(uid);
    }

    @PostMapping("/api/setHistory")
    public boolean setHistory(@RequestParam(value = "uid") int uid,
                              @RequestParam(value = "vid") int vid) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 时区为shanghai
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        //LocalDateTime currentTime = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间
        QueryWrapper<History> queryWrapper = new QueryWrapper<History>();
        queryWrapper.eq("uid", uid).eq("vid", vid);
        String formattedDateTime = zonedDateTime.format(formatter);
        if (historyMapper.selectOne(queryWrapper) != null) {
            historyMapper.delete(queryWrapper);
        }
        return historyMapper.insertHistory(uid, vid, formattedDateTime) != 0;
    }

    @DeleteMapping("/api/deleteHistory")
    public DeleteStatus deleteHistory(@RequestParam long[] id) {
        try {
            for (long item : id) {
                if (historyMapper.deleteById(item) == 0) {
                    return new DeleteStatus(false, 5);
                }
            }
            return new DeleteStatus(true, 1);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new DeleteStatus(false, 6);
        }
    }
}
