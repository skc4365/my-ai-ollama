-- =====================================
-- Order AI Review Project
-- PostgreSQL Query (Optimized & Error-Free)
-- =====================================

-- 1. 테이블 초기화 (TRUNCATE만 사용해도 데이터가 완전히 삭제되며, CASCADE로 연관 테이블도 함께 정리합니다)
TRUNCATE TABLE qwen_review_analysis CASCADE;
TRUNCATE TABLE qwen_review CASCADE;
TRUNCATE TABLE qwen_order CASCADE;


-- =====================================
-- 2. 테스트 데이터 삽입 예시
-- =====================================
-- 필요시 아래 주석을 해제하여 샘플 데이터를 수동으로 입력할 수 있습니다.

-- INSERT INTO qwen_order (id, customer, product) VALUES
-- (1, '홍길동', '무선 키보드'),
-- (2, '김철수', '블루투스 이어폰'),
-- (3, '이영희', '노트북 거치대');

-- INSERT INTO qwen_review (id, order_id, content) VALUES
-- (1, 1, '배송이 매우 빠르고 제품 품질도 좋아서 만족합니다.'),
-- (2, 2, '음질은 괜찮지만 배터리가 빨리 닳아서 아쉽습니다.'),
-- (3, 3, '가격 대비 성능이 좋고 사용하기 편합니다.');

-- qwen이 리뷰를 평가하여 저장하는 테이블( insert구문은 실행하지 마세요~)
-- INSERT INTO qwen_review_analysis (id, review_id, sentiment, summary) VALUES
-- (1, 1, '긍정', '배송 속도와 제품 품질에 만족함'),
-- (2, 2, '부정', '배터리 사용 시간이 짧아 불만 있음'),
-- (3, 3, '긍정', '가격과 사용성에 만족함');

select * from qwen_order;
select * from qwen_review;
select * from qwen_review_analysis;


-- =====================================
-- 3. 시퀀스(Sequence) 동기화 및 초기화
-- =====================================
-- 명시적으로 ID 값을 넣은 후 시퀀스를 재설정해주지 않으면,
-- 애플리케이션(JPA/Hibernate)에서 데이터 등록 시 Primary Key 중복(Unique Constraint Violation) 오류가 발생합니다.
--
-- 아래 PL/pgSQL 블록은:
-- 1) 데이터베이스 컬럼에 직접 연결된 SERIAL/IDENTITY 시퀀스 (예: qwen_xxx_id_seq)
-- 2) JPA/Hibernate가 생성하는 독립 시퀀스 (예: qwen_xxx_seq)
-- 두 가지 경우를 모두 자동으로 감지하여 데이터 최대값 + 1로 안전하게 시퀀스를 갱신합니다.

-- *** Hibernate가 다음 ID를 요청할 때(Database Sequence에서), 
--현재 데이터(id=3)를 기준으로 id=4를 받을 수 있도록 Sequence를 동기화하는 작업 ***.

DO $$
DECLARE
    seq_name text;
BEGIN
    -- [qwen_order] 시퀀스 동기화
    seq_name := pg_get_serial_sequence('qwen_order', 'id');
    IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', COALESCE((SELECT MAX(id) FROM qwen_order), 0) + 1, false)';
    ELSIF EXISTS (SELECT 1 FROM pg_class WHERE relname = 'qwen_order_seq') THEN
        EXECUTE 'SELECT setval(''qwen_order_seq'', COALESCE((SELECT MAX(id) FROM qwen_order), 0) + 1, false)';
    END IF;

    -- [qwen_review] 시퀀스 동기화
    seq_name := pg_get_serial_sequence('qwen_review', 'id');
    IF seq_name IS NOT NULL THEN
        EXECUTE 'SELECT setval(''' || seq_name || ''', COALESCE((SELECT MAX(id) FROM qwen_review), 0) + 1, false)';
    ELSIF EXISTS (SELECT 1 FROM pg_class WHERE relname = 'qwen_review_seq') THEN
        EXECUTE 'SELECT setval(''qwen_review_seq'', COALESCE((SELECT MAX(id) FROM qwen_review), 0) + 1, false)';
    END IF;

    -- [qwen_review_analysis] 시퀀스 동기화
--    seq_name := pg_get_serial_sequence('qwen_review_analysis', 'id');
--    IF seq_name IS NOT NULL THEN
--        EXECUTE 'SELECT setval(''' || seq_name || ''', COALESCE((SELECT MAX(id) FROM qwen_review_analysis), 0) + 1, false)';
--    ELSIF EXISTS (SELECT 1 FROM pg_class WHERE relname = 'qwen_review_analysis_seq') THEN
--        EXECUTE 'SELECT setval(''qwen_review_analysis_seq'', COALESCE((SELECT MAX(id) FROM qwen_review_analysis), 0) + 1, false)';
--    END IF;
END $$;
