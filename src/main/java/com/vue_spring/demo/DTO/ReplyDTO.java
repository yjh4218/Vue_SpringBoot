package com.vue_spring.demo.DTO;

import java.util.List;

public class ReplyDTO {

    static public class ReplyData{
        private Long replyId;
        private String changeReplyData;

        public Long getReplyId() {
            return replyId;
        }

        public String getChangeReplyData() {
            return changeReplyData;
        }
    }

    private Long id;
    private List<ReplyData> replyDataList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ReplyData> getReplyDataList() {
        return replyDataList;
    }

    public void setReplyDataList(List<ReplyData> replyDataList) {
        this.replyDataList = replyDataList;
    }
}
