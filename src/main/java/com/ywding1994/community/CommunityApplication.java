package com.ywding1994.community;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityApplication {

	/**
	 * 解决netty启动冲突问题
	 * <p>
	 * @see Netty4Utils.setAvailableProcessors()
	 * </P>
	 */
	@PostConstruct
	public void init() {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
