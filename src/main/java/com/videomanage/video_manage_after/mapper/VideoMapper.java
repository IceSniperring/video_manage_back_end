package com.videomanage.video_manage_after.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.videomanage.video_manage_after.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    @Select("select kind from video group by kind")
    String[] getKind();


    @Insert("insert into video(uid,title,upload_date,kind,file_path,post_path) values (${uid},'${title}','${uploadDate}','${kind}','${filePath}','${postPath}')")
    int InsertTest(int uid, String title,
                   String uploadDate, String kind,
                   String filePath, String postPath);

}
