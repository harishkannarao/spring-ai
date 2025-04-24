package com.harishkannarao.spring.spring_ai.controller;

import com.harishkannarao.spring.spring_ai.entity.InputDocument;
import com.harishkannarao.spring.spring_ai.entity.InputMetaData;
import com.harishkannarao.spring.spring_ai.repository.RagVectorRepository;
import com.harishkannarao.spring.spring_ai.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.chat.transformer.KeywordMetadataEnricher;
import org.springframework.ai.chat.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class RagController {

	private static final Logger log = LoggerFactory.getLogger(RagController.class);
	private final ClassPathResource questionTemplateResource = new ClassPathResource(
		"/prompts/question-template.st");
	private final ChatClient chatClient;
	private final ChatClient chatClientWithTools;
	private final VectorStore vectorStore;
	private final TokenTextSplitter tokenTextSplitter;
	private final KeywordMetadataEnricher keywordMetadataEnricher;
	private final SummaryMetadataEnricher summaryMetadataEnricher;
	private final RagVectorRepository ragVectorRepository;

	@Autowired
	public RagController(
		ChatClient chatClient,
		@Qualifier("textChatClientWithTools") ChatClient chatClientWithTools,
		VectorStore vectorStore,
		TokenTextSplitter tokenTextSplitter,
		KeywordMetadataEnricher keywordMetadataEnricher,
		SummaryMetadataEnricher summaryMetadataEnricher,
		RagVectorRepository ragVectorRepository) {
		this.chatClient = chatClient;
		this.chatClientWithTools = chatClientWithTools;
		this.vectorStore = vectorStore;
		this.tokenTextSplitter = tokenTextSplitter;
		this.keywordMetadataEnricher = keywordMetadataEnricher;
		this.summaryMetadataEnricher = summaryMetadataEnricher;
		this.ragVectorRepository = ragVectorRepository;
	}

	@GetMapping("rag-chat")
	public Flux<String> chatWithRag(@RequestParam String q) {
		log.info("Question {}", q);
		String documents = Objects.requireNonNull(vectorStore
				.similaritySearch(SearchRequest.builder().query(q).build()))
			.stream()
			.map(Document::getText)
			.collect(Collectors.joining(System.lineSeparator()));
		log.info("RAG documents: {}", documents);
		final PromptTemplate promptTemplate;
		promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("context", documents);
		promptTemplate.add("question", q);
		Message userMessage = promptTemplate.createMessage();
		Message systemMessage = new SystemMessage(
			"You are a helpful AI Assistant answering questions");
		return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage)))
			.stream()
			.content();
	}

	@GetMapping("rag-chat-tools-callback")
	public String chatWithRagWithTools(@RequestParam String q) {
		log.info("Question {}", q);
		String documents = Objects.requireNonNull(vectorStore
				.similaritySearch(SearchRequest.builder().query(q).build()))
			.stream()
			.map(Document::getText)
			.collect(Collectors.joining(System.lineSeparator()));
		log.info("RAG documents: {}", documents);
		PromptTemplate promptTemplate = new PromptTemplate(questionTemplateResource);
		promptTemplate.add("question", q);
		promptTemplate.add("context", documents);
		Message userMessage = promptTemplate.createMessage();
		Message systemMessage = new SystemMessage(
			"You are a helpful AI Assistant answering questions");
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		return chatClientWithTools
			.prompt(prompt)
			.toolContext(Map.of(Constants.X_REQUEST_ID, MDC.get(Constants.X_REQUEST_ID)))
			.call()
			.content();
	}

	@PostMapping("/ingest-document")
	public ResponseEntity<Void> ingestDocument(
		@RequestBody List<InputDocument> input) {
		List<Document> vectorDocuments = input.stream()
			.peek(inputDocument -> log.info("Received input {}", inputDocument))
			.map(inputDocument -> {
				Map<String, Object> metaData = inputDocument.metaData().stream()
					.collect(Collectors.toUnmodifiableMap(InputMetaData::key, InputMetaData::value));
				return new Document(inputDocument.content(), metaData);
			})
			.toList();
		List<Document> transformedDocuments = tokenTextSplitter.apply(vectorDocuments);
		vectorStore.add(transformedDocuments);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/clear-vector-db")
	public ResponseEntity<Void> deleteVectorDb() {
		ragVectorRepository.deleteAll();
		return ResponseEntity.noContent().build();
	}


	@PostMapping("ingest-pdf")
	public ResponseEntity<Void> ingestPdf(@RequestParam("file") MultipartFile file) {
		PagePdfDocumentReader reader = new PagePdfDocumentReader(file.getResource());
		List<Document> rawDocuments = reader.read();
		log.info("raw documents count: {}", rawDocuments.size());
		List<Document> documents = tokenTextSplitter.apply(rawDocuments);
		log.info("documents count after split: {}", documents.size());
		IntStream.range(0, documents.size())
			.boxed()
			.forEachOrdered(index -> {
				Document document = documents.get(index);
				log.info("Ingesting document index {} with content {}", index, document.getText());
				vectorStore.accept(List.of(document));
			});
		return ResponseEntity.noContent().build();
	}

	@PostMapping("ingest-json")
	public ResponseEntity<Void> ingestJson(
		@RequestHeader HttpHeaders headers,
		@RequestBody String json) {
		log.info("raw json: {}", json);
		Charset charset = Optional.ofNullable(headers.getContentType())
			.flatMap(mediaType -> Optional.ofNullable(mediaType.getCharset()))
			.orElse(StandardCharsets.UTF_8);
		JsonReader jsonReader = new JsonReader(new ByteArrayResource(json.getBytes(charset)));
		List<Document> documents = jsonReader.read();
		log.info("json documents count after split: {}", documents.size());
		IntStream.range(0, documents.size())
			.boxed()
			.forEachOrdered(index -> {
				Document document = documents.get(index);
				log.info("Ingesting json document index {} with content {}", index, document.getText());
				vectorStore.accept(
					tokenTextSplitter.apply(
						summaryMetadataEnricher.apply(
							keywordMetadataEnricher.apply(
								List.of(document)))));
			});
		return ResponseEntity.noContent().build();
	}
}
