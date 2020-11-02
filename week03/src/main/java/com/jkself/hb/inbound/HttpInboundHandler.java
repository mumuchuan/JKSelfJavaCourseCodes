package com.jkself.hb.inbound;

import com.jkself.hb.filter.impl.HttpRequestFilterImpl;
import com.jkself.hb.outbound.httpclient4.HttpOutboundHandler;
import com.jkself.hb.outbound.netty4.NettyHttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private NettyHttpClient handler;
    private HttpRequestFilterImpl httpRequestFilter;
    
    public HttpInboundHandler(String proxyServer) throws IOException, URISyntaxException {
        this.proxyServer = proxyServer;
        handler = new NettyHttpClient(this.proxyServer);
        httpRequestFilter = new HttpRequestFilterImpl();
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            //过滤器
            httpRequestFilter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
