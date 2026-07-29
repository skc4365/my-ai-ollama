package com.skc.orderai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Spring AI의 ChatClient를 활용하여 로컬 Ollama AI 엔진(기본 모델: qwen2.5:3b)과 통신하고,
 * 고객 후기에 대한 감정 분석 및 핵심 요약을 수행하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ReviewAiService {

	private final ChatClient chatClient;



    /**
     * 입력된 고객의 리뷰 텍스트를 로컬 AI 모델에 전송하여 감정 분석(긍정/부정 등) 및 요약 결과가 포함된 JSON 형태의 문자열을 반환합니다.
     *
     * @param review 분석 대상 고객 후기 원문
     * @return AI가 도출한 감정(sentiment)과 요약(summary) 정보를 담고 있는 JSON 포맷의 문자열
     */
	public String analyze(String review) {

        // 1. Ollama AI 모델이 파싱 가능한 표준 JSON 포맷으로 일관성 있게 응답하도록 지침(Prompt)과 리뷰 텍스트 결합
		String prompt = """
				아래 고객후기를 분석하세요.

				JSON으로만 답하세요.

				{
				  "sentiment":"긍정",
				  "summary":"..."
				}

				후기

				%s
				""".formatted(review);

        // 2. ChatClient 플루언트 API 체인을 이용하여 프롬프트를 설정(prompt), AI 모델 호출(call), 그리고 응답 결과 본문(content)을 String으로 반환
		return chatClient.prompt().user(prompt).call().content();
	}

}

