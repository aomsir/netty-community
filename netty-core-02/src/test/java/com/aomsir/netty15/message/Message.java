package com.aomsir.netty15.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
abstract public class Message implements Serializable {

    @JsonIgnore
    public abstract int getMessageType();

}
