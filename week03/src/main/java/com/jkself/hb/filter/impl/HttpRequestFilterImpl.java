package com.jkself.hb.filter.impl;

import com.jkself.hb.filter.HttpRequestFilter;
import com.jkself.hb.outbound.netty4.NettyHttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author ：huabin
 * @date ：Created in 2020/11/3 0:20
 * @description：
 * @modified By：
 * @version: $
 */
public class HttpRequestFilterImpl implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("nio","huabin");
    }


}
