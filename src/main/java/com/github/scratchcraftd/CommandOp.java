package com.github.scratchcraftd;

import com.google.gson.Gson;

import java.util.Map;

public class CommandOp {

    private String opId;
    private String category;
    private Map<String, Object> data;

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommandOp{" +
            "opId='" + opId + '\'' +
            ", category='" + category + '\'' +
            ", data=" + data +
            '}';
    }

    public static void main(String[] args) {
        System.out.println(new Gson().fromJson("{\"opId\": \"player_move_forward\", \"category\": \"player_motion\", \"data\": {\"steps\": 5}}", CommandOp.class));
    }
}
