package com.aomsir.netty.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
abstract public class Message {

    @JsonIgnore
    public abstract int getMessageType();

}
