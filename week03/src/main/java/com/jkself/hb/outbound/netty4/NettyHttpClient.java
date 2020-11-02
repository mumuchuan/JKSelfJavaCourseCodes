package com.jkself.hb.outbound.netty4;

/**
 * @author ：huabin
 * @date ：Created in 2020/10/26 20:55
 * @description：基于netty实现，参考网上例子
 * @modified By：
 * @version: $1.0.0
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.net.*;

public class NettyHttpClient {

    private String address;
    private int port;


    public NettyHttpClient(String backendUrl) throws IOException, URISyntaxException {
        backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;

        // 端口号
        port = parsePort(backendUrl);
        // 域名
        String host = parseHost(backendUrl);
        // IP 地址
        address = parseIp(host);

    }



    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new NettyHttpClientOutboundHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(address, port).sync();
            // 发送http请求
            f.channel().write(fullRequest);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }


    /**
     * 获取端口号
     *
     * @param href 网址, ftp, http, nntp, ... 等等
     * @return
     * @throws IOException
     */
    public static int parsePort(String href) throws IOException {
        //java.net中存在的类
        URL url = new URL(href);
        // 端口号; 如果 href 中没有明确指定则为 -1
        int port = url.getPort();
        if (port < 0) {
            // 获取对应协议的默认端口号
            port = url.getDefaultPort();
        }
        return port;
    }

    /**
     * 获取Host部分
     *
     * @param href 网址, ftp, http, nntp, ... 等等
     * @return
     * @throws IOException
     */
    public static String parseHost(String href) throws IOException {
        //
        URL url = new URL(href);
        // 获取 host 部分
        String host = url.getHost();
        return host;
    }

    /**
     * 根据域名(host)解析IP地址
     *
     * @param host 域名
     * @return
     * @throws IOException
     */
    public static String parseIp(String host) throws IOException {
        // 根据域名查找IP地址
        InetAddress inetAddress = InetAddress.getByName(host);
        // IP 地址
        String address = inetAddress.getHostAddress();
        return address;
    }

    public static void main(String[] args) throws Exception {
        NettyHttpClient client = new NettyHttpClient("http://localhost:8088");
//        client.handle();
    }
}