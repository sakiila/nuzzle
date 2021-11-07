package me.bob.nuzzle.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RpcRequest<T> {

    private Class<T> serviceClass;

    private String methodName;

    private Object[] params;
}
