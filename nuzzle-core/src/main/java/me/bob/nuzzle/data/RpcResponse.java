package me.bob.nuzzle.data;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RpcResponse {
    
    public String message;
}
