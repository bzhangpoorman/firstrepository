package com.bzhang.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ConcurrentTest implements Runnable{

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			new Thread(new ConcurrentTest(i*10, i*10+10)).start();
		}
		TimeUnit.SECONDS.sleep(10);
	}

	private int i;
	private int j;

	public ConcurrentTest(int i, int j) {
		super();
		this.i = i;
		this.j = j;
	}

	public void connect(int begin,int end) throws MalformedURLException, UnsupportedEncodingException, IOException, Exception {
		
		for (int i = begin; i < end; i++) {
			//TimeUnit.MILLISECONDS.sleep(1);
			URL url=new URL("http://localhost/login?wife="+i);
			BufferedReader br= new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
			String msg=null;
			while (null!=(msg=br.readLine())) {
				System.out.println(msg+"**i="+i);
			}
			br.close();
		}
		
		
	}

	@Override
	public void run() {
		try {
			connect(i, j);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
