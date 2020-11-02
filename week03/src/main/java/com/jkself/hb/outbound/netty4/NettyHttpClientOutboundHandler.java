package com.jkself.hb.outbound.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @author ：huabin
 * @date ：Created in 2020/10/26 20:56
 * @description：
 * @modified By：
 * @version: $
 */
public class NettyHttpClientOutboundHandler  extends ChannelInboundHandlerAdapter {

    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        
        
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
//    todo 怎么写进去        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
//    todo        ctx.write(content);
            System.out.println("--------------------------------------------->【NettyClient】请求结果:" + buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();
        }
    }


}