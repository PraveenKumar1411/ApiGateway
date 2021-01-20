package com.wipro.springboot.assignment.filter;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.wipro.springboot.assignment.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalPreFilter implements GlobalFilter, Ordered {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	final Logger logger =
		       LoggerFactory.getLogger(LoggingGlobalPreFilter.class);

		    @Override
		    public Mono<Void> filter(
		      ServerWebExchange exchange,
		      GatewayFilterChain chain) {
		        logger.info("Global Pre Filter executed");
		        logger.info(exchange.getRequest().getHeaders().getFirst("Authorization").toString().substring(7));
		       
		        if (jwtUtil.validateToken(exchange.getRequest().getHeaders().getFirst("Authorization").toString().substring(7))) {
			        return chain.filter(exchange);
		        }
		        else {
		        	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		        	return exchange.getResponse().setComplete();
		        }
		        
		    }
		    
		    @Override
		    public int getOrder() {
		        return -1;
		    }
}
