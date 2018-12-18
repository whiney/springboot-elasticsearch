package com.elasticsearch.Model;

import lombok.Data;

/**
 * Created by baishuai on 2018/12/18
 */
@Data
public class Es {

    private String index;

    private String type;

    public Es(String index, String type) {
        this.index = index;
        this.type = type;
    }
}
