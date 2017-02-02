package com.heros.follow;

import com.heros.follow.SocketServer.EchoServer;
import com.heros.follow.source.PHA.PHA_MainController;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		new Thread() {
			@Override
			public void run() {
				PHA_MainController mainController = new PHA_MainController();
				mainController.start();
				super.run();
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				EchoServer echoServer = new EchoServer(1234);
				try {
					echoServer.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				super.run();
			}
		}.start();

		return application.sources(GambleApplication.class);
	}

}
