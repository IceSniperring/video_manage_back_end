package com.videomanage.video_manage_after.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.videomanage.video_manage_after.entity.History;
import com.videomanage.video_manage_after.entity.HistoryRecordDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HistoryMapper extends BaseMapper<History> {
    @Insert("insert history(uid,vid,view_date) values (${uid},${vid},'${viewDate}')")
    int insertHistory(int uid, int vid, String viewDate);

    @Select("SELECT history.record_id, video.title, video.kind, video.file_path, video.post_path, history.view_date " +
            "FROM history INNER JOIN video ON history.vid = video.id " +
            "WHERE history.uid = ${uid}")
    List<HistoryRecordDTO> selectHistoryInfo(int uid);
}
