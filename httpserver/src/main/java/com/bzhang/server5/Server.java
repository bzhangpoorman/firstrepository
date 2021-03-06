package com.bzhang.server5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIO编程方式手写httpServer，模拟tomcat
 * @author bzhang
 *
 */
public class Server implements Runnable{
	//选择器，多路复用器
	private Selector selector;
	
	//服务器通道
	private ServerSocketChannel serverSocketChannel;
	
	//状态码
	private int code;
	
	//服务器核心数
	private static final int CPU_CORE=Runtime.getRuntime().availableProcessors();
	
	private static ExecutorService pool=Executors.newFixedThreadPool(CPU_CORE);
	
	//private Request request;
	//private Response response;
	
	
	public static void main(String[] args) {
		new Thread(new Server(80)).start();
	}
	
	public Server(int port) {
		this(port,1024);
		
	}
	public Server(int port,int capacity) {
		init(port);
		
	}

	
	/**
	 * 初始化selector及serverSocketChannel,并准备接受客户端连接
	 * @param port
	 */
	private void init(int port) {
		try {
			//打开选择器
			selector=Selector.open();
			
			InetSocketAddress iAddress=new InetSocketAddress(port);
			//打开服务通道
			serverSocketChannel=ServerSocketChannel.open();
			//设置为非阻塞
			serverSocketChannel.configureBlocking(false);
			//绑定端口
			serverSocketChannel.bind(iAddress);
			//在seletor注册服务通道，注册为ACCEPT状态，可接受客户端连接
			serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		while (true) {
			try {
				selector.select();
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = (SelectionKey) keys.next();
					keys.remove();
					if (key.isValid()) {
						// 阻塞状态
						try{
							if(key.isAcceptable()){
								accept(key);
							}
						}catch(CancelledKeyException cke){
							// 断开连接。 出现异常。
							key.cancel();
						}
						// 可读/可写状态
						try{
							if(key.isReadable()){
								pool.execute(new DataHandler(key));
							}
						}catch(CancelledKeyException cke){
							key.cancel();
						}
						
						
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	private class DataHandler implements Runnable{
		private SocketChannel socketChannel;
		
		private Request request;
		
		private Response response;
		
		private SelectionKey key;
		
		public DataHandler(SelectionKey key) {
			this.key=key;
			this.socketChannel=(SocketChannel)key.channel();
			this.request=new Request(socketChannel);
			this.response=new Response();
		}
		
		private void write() {
			try {
				
				//System.out.println("url***"+request.getUrl());
				Servlet servlet=WebApp.getServlet(request.getUrl());
				if (servlet==null) {
					code=404;
				}else {
					code=200;
					servlet.service(request, response);
					
				}
				response.pushToClient(code, socketChannel);
				//key.interestOps(SelectionKey.OP_READ);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					socketChannel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				key.cancel();
				code=500;
			}finally {
				try {
					socketChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				key.cancel();
			}
			
		}
		
		private void read() {
			
			try {
				if (request.isRequset()) {
					
					key.interestOps(SelectionKey.OP_WRITE);
				}else {
					socketChannel.close();
					key.cancel();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				try {
					socketChannel.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				key.cancel();
			}
		}

		@Override
		public void run() {
			
			if (socketChannel.isOpen()) {
				read();
				write();
			}
			
			
		}
		
	}
	private void accept(SelectionKey key) {
		try {
			ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
			SocketChannel client=serverChannel.accept();
			//设置为非阻塞
			client.configureBlocking(false);
			
			System.out.println("client:"+client.getRemoteAddress());
			
			//注册该通道为可读
			client.register(selector, SelectionKey.OP_READ);
			
		} catch (IOException e) {
			e.printStackTrace();
			try {
				key.channel().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			key.cancel();
		}
		
	}
	
}
