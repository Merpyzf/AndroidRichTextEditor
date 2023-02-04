package com.chinalwb.are.demo.mood_diary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: WK
 * @date: 2023/2/3
 */
public class MoodDiary {
    // 日记标题
    private String title = "";
    // 日记话题
    private List<Tag> tags = new ArrayList<>();
    // 日记内容
    private List<Block> content = new ArrayList<>();
    // 日记中添加的装饰贴纸
    private List<Sticker> stickers = new ArrayList<>();

    class TextStyle {
        // 文本颜色
        private int textColor;
        // 文字阴影设置
        private float shadowRadius;
        private float shadowDx;
        private float shadowDy;
    }
}
