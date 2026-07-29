package com.skc.orderai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewAiService {

	private final ChatClient chatClient;

	public String analyze(String review) {

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

		return chatClient.prompt().user(prompt).call().content();
	}

}

//@Service
//@RequiredArgsConstructor
//public class ReviewAiService {
//
//	private final ChatClient chatClient;
//
//    public String analyze(String review){
//
//        String prompt = """
//        아래 고객후기를 분석하세요.
//
//        결과는 다음 형식으로만 답하세요.
//
//        감정 :
//        요약 :
//
//        후기:
//        %s
//        """.formatted(review);
//
//        return chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();
//    }
//}
