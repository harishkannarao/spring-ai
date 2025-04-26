package com.harishkannarao.spring.spring_ai.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpClientConfiguration {

	@Bean
	public McpSyncClient createOwnMcpClient(
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server.url}")
		String url,
		@Value("${spring.ai.mcp.client.sse.connections.spring-ai-mcp-server.sse-endpoint}")
		String sseEndpoint
		) {
		McpSyncClient mcpSyncClient = McpClient
			.sync(HttpClientSseClientTransport.builder(url).sseEndpoint(sseEndpoint).build())
			.build();
		mcpSyncClient.initialize();
		return mcpSyncClient;
	}

	@Bean
	@Qualifier("remoteMcpTools")
	public List<ToolCallback> remoteMcpTools(List<McpSyncClient> mcpSyncClients) {
		return SyncMcpToolCallbackProvider.syncToolCallbacks(mcpSyncClients);
	}
}
