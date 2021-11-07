package me.bob.nuzzle.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RpcRequest {
    
    public String interfaceName;

    public String methodName;
}
