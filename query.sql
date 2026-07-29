-- =====================================
-- Order AI Review Project
-- PostgreSQL Query
-- =====================================
TRUNCATE TABLE qwen_review_analysis RESTART IDENTITY CASCADE;
TRUNCATE TABLE qwen_review RESTART IDENTITY CASCADE;
TRUNCATE TABLE qwen_order RESTART IDENTITY CASCADE;

-- DELETE FROM qwen_review_analysis;
-- DELETE FROM qwen_review;
-- DELETE FROM qwen_order;

-- ALTER SEQUENCE qwen_order_id_seq RESTART WITH 1;
-- ALTER SEQUENCE qwen_review_id_seq RESTART WITH 1;
-- ALTER SEQUENCE qwen_review_analysis_id_seq RESTART WITH 1;


-- =====================================
-- 1. 주문 데이터
-- =====================================

INSERT INTO qwen_order
(
    id,
    customer,
    product
)
VALUES
(
    1,
    '홍길동',
    '무선 키보드'
),
(
    2,
    '김철수',
    '블루투스 이어폰'
),
(
    3,
    '이영희',
    '노트북 거치대'
);



-- =====================================
-- 2. 고객 후기 데이터
-- =====================================

INSERT INTO qwen_review
(
    id,
    order_id,
    content
)
VALUES
(
    1,
    1,
    '배송이 매우 빠르고 제품 품질도 좋아서 만족합니다.'
),
(
    2,
    2,
    '음질은 괜찮지만 배터리가 빨리 닳아서 아쉽습니다.'
),
(
    3,
    3,
    '가격 대비 성능이 좋고 사용하기 편합니다.'
);



-- =====================================
-- 3. AI 분석 결과
-- =====================================

INSERT INTO qwen_review_analysis
(
    id,
    review_id,
    sentiment,
    summary
)
VALUES
(
    1,
    1,
    '긍정',
    '배송 속도와 제품 품질에 만족함'
),
(
    2,
    2,
    '부정',
    '배터리 사용 시간이 짧아 불만 있음'
),
(
    3,
    3,
    '긍정',
    '가격과 사용성에 만족함'
);



-- =====================================
-- 4. Sequence 초기화
-- PostgreSQL
-- =====================================

SELECT setval(
    pg_get_serial_sequence('qwen_order','id'),
    COALESCE((SELECT MAX(id) FROM qwen_order),1),
    false
);

SELECT setval(
    pg_get_serial_sequence('qwen_review','id'),
    COALESCE((SELECT MAX(id) FROM qwen_review),1),
    false
);

SELECT setval(
    pg_get_serial_sequence('qwen_review_analysis','id'),
    COALESCE((SELECT MAX(id) FROM qwen_review_analysis),1),
    false
);


--SELECT setval(
--    pg_get_serial_sequence('qwen_order','id'),
--    (SELECT MAX(id) FROM qwen_order)
--);
--
--SELECT setval(
--    pg_get_serial_sequence('qwen_review','id'),
--    (SELECT MAX(id) FROM qwen_review)
--);
--
--
--SELECT setval(
--    pg_get_serial_sequence('qwen_review_analysis','id'),
--    (SELECT MAX(id) FROM qwen_review_analysis)
--);