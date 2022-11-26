package com.aomsir.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
abstract public class AbstractResponseMessage extends Message {
    //200成功 ，500失败
    private String code;
    private String reason;

}
