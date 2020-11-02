package com.jkself.hb.filter;

import com.jkself.hb.outbound.netty4.NettyHttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpRequestFilter {
    
    void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);


}
