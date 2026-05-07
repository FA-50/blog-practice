INSERT INTO category (title, status, created_at, updated_at) VALUES
                                                                 ('한식', 'ACTIVATED', NOW(), NOW()),
                                                                 ('중식', 'ACTIVATED', NOW(), NOW()),
                                                                 ('일식', 'ACTIVATED', NOW(), NOW()),
                                                                 ('양식', 'ACTIVATED', NOW(), NOW()),
                                                                 ('디저트', 'ACTIVATED', NOW(), NOW()),
                                                                 ('베이킹', 'ACTIVATED', NOW(), NOW()),
                                                                 ('샐러드', 'ACTIVATED', NOW(), NOW()),
                                                                 ('스프', 'ACTIVATED', NOW(), NOW()),
                                                                 ('면요리', 'ACTIVATED', NOW(), NOW()),
                                                                 ('밥요리', 'ACTIVATED', NOW(), NOW()),
                                                                 ('고기요리', 'ACTIVATED', NOW(), NOW()),
                                                                 ('해산물', 'ACTIVATED', NOW(), NOW());


INSERT INTO post (
    title,
    content,
    created_at,
    updated_at,
    viewed_cnt,
    visibility,
    status,
    category_id
) VALUES


-- category 5 (디저트)
('맛잘알이 알려주는 식빵 먹는법', '딸기잼, 땅콩잼 바르고 계란후라이 하나 구워서 올려드세요.', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', NULL),
('요거트 맛있게 먹는법', '딸기잼을 넣어 먹는다.', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 5),

-- category 4 (양식)
('파스타 만들기', '토마토 파스타 레시피', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 4),
('스테이크 굽기', '완벽한 스테이크 굽는 법', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 4),


-- category 3 (일식)
('초밥 만들기', '초밥 밥 짓는 방법', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 3),
('라멘 레시피', '집에서 만드는 라멘', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 3),

-- category 2 (중식)
('짜장면 만들기', '집에서 만드는 짜장면', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 2),
('볶음밥 레시피', '중식 스타일 볶음밥', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 2),

-- category 1 (한식)
('김치찌개 레시피', '맛있는 김치찌개 끓이는 방법', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 1),
('된장찌개 레시피', '구수한 된장찌개 만드는 법', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 1),

-- category 8 (스프)
('호박죽 대량으로 쉽게 끓이는 꿀팁', '그런건 없다. 정성으로 끓여라.', NOW(), NOW(), 0, 'PUBLIC', 'ACTIVATED', 8);


INSERT INTO comment (content, status, created_at, parent_id, post_id) VALUES

-- =========================
-- post 9 (김치찌개)
-- =========================
('김치찌개 진짜 맛있어요!', 'ACTIVATED', NOW(), NULL, 9),
('레시피대로 하니 성공했어요', 'ACTIVATED', NOW(), NULL, 9),
('고기 추가하면 더 맛있어요', 'ACTIVATED', NOW(), NULL, 9),
('국물이 너무 좋아요', 'ACTIVATED', NOW(), NULL, 9),
('밥도둑이네요', 'ACTIVATED', NOW(), NULL, 9),
('신김치로 해야 하나요?', 'ACTIVATED', NOW(), NULL, 9),
('두부 넣어도 되나요?', 'ACTIVATED', NOW(), NULL, 9),
('생각보다 간단하네요', 'ACTIVATED', NOW(), NULL, 9),
('자주 해먹을 것 같아요', 'ACTIVATED', NOW(), NULL, 9),
('레시피 감사합니다', 'ACTIVATED', NOW(), NULL, 9),
('조금 맵게 먹어도 좋네요', 'ACTIVATED', NOW(), NULL, 9),
('아이들도 잘 먹어요', 'ACTIVATED', NOW(), NULL, 9),
('고춧가루 조절이 핵심', 'ACTIVATED', NOW(), NULL, 9),
('다음엔 돼지고기로 해볼게요', 'ACTIVATED', NOW(), NULL, 9),
('최고의 집밥 메뉴입니다', 'ACTIVATED', NOW(), NULL, 9),

-- =========================
-- post 10 (된장찌개)
-- =========================
('된장 향이 너무 좋아요', 'ACTIVATED', NOW(), NULL, 10),
('집된장으로 해야 맛있네요', 'ACTIVATED', NOW(), NULL, 10),
('애호박 넣으면 최고', 'ACTIVATED', NOW(), NULL, 10),
('구수해서 계속 먹게 돼요', 'ACTIVATED', NOW(), NULL, 10),
('두부는 필수네요', 'ACTIVATED', NOW(), NULL, 10),
('레시피 간단해서 좋아요', 'ACTIVATED', NOW(), NULL, 10),
('밥이랑 잘 어울려요', 'ACTIVATED', NOW(), NULL, 10),
('멸치육수 쓰셨나요?', 'ACTIVATED', NOW(), NULL, 10),
('자취생 필수 레시피', 'ACTIVATED', NOW(), NULL, 10),
('건강한 느낌이에요', 'ACTIVATED', NOW(), NULL, 10),
('간 맞추는 게 중요하네요', 'ACTIVATED', NOW(), NULL, 10),
('다시마 넣어도 좋을 듯', 'ACTIVATED', NOW(), NULL, 10),
('국물이 진짜 좋아요', 'ACTIVATED', NOW(), NULL, 10),
('매일 먹어도 질리지 않음', 'ACTIVATED', NOW(), NULL, 10),
('완벽한 한식입니다', 'ACTIVATED', NOW(), NULL, 10),

-- =========================
-- post 7 (짜장면)
-- =========================
('집에서 짜장면 가능하네요', 'ACTIVATED', NOW(), NULL, 7),
('생각보다 쉬워요', 'ACTIVATED', NOW(), NULL, 7),
('춘장 볶는 게 핵심', 'ACTIVATED', NOW(), NULL, 7),
('고기 많이 넣으면 좋아요', 'ACTIVATED', NOW(), NULL, 7),
('아이들이 좋아해요', 'ACTIVATED', NOW(), NULL, 7),
('중식 느낌 제대로네요', 'ACTIVATED', NOW(), NULL, 7),
('양파가 중요하네요', 'ACTIVATED', NOW(), NULL, 7),
('배달 안 시켜도 될 듯', 'ACTIVATED', NOW(), NULL, 7),
('소스가 진짜 맛있어요', 'ACTIVATED', NOW(), NULL, 7),
('면 삶기 중요합니다', 'ACTIVATED', NOW(), NULL, 7),
('생각보다 간단함', 'ACTIVATED', NOW(), NULL, 7),
('집에서 이게 되네요', 'ACTIVATED', NOW(), NULL, 7),
('진짜 중국집 느낌', 'ACTIVATED', NOW(), NULL, 7),
('다음엔 짬뽕 도전', 'ACTIVATED', NOW(), NULL, 7),
('완전 성공 레시피', 'ACTIVATED', NOW(), NULL, 7),

-- =========================
-- post 8 (볶음밥)
-- =========================
('볶음밥 진짜 쉽네요', 'ACTIVATED', NOW(), NULL, 8),
('계란이 핵심', 'ACTIVATED', NOW(), NULL, 8),
('간단하지만 맛있어요', 'ACTIVATED', NOW(), NULL, 8),
('남은 밥 활용 최고', 'ACTIVATED', NOW(), NULL, 8),
('불맛 내는 게 포인트', 'ACTIVATED', NOW(), NULL, 8),
('야채 넣으면 더 좋아요', 'ACTIVATED', NOW(), NULL, 8),
('간장 살짝 넣으면 굿', 'ACTIVATED', NOW(), NULL, 8),
('자취 필수 메뉴', 'ACTIVATED', NOW(), NULL, 8),
('진짜 맛있게 됐어요', 'ACTIVATED', NOW(), NULL, 8),
('간단해서 좋아요', 'ACTIVATED', NOW(), NULL, 8),
('계란 두 개 추천', 'ACTIVATED', NOW(), NULL, 8),
('후추 꼭 넣으세요', 'ACTIVATED', NOW(), NULL, 8),
('중식 느낌 제대로', 'ACTIVATED', NOW(), NULL, 8),
('집에서도 가능하네요', 'ACTIVATED', NOW(), NULL, 8),
('최고의 한 끼', 'ACTIVATED', NOW(), NULL, 8),

-- =========================
-- post 11 (호박죽)
-- =========================
('제 자신을 반성하게 됩니다..', 'ACTIVATED', NOW(), NULL, 11),
('헉', 'ACTIVATED', NOW(), NULL, 11),
('역시 뷔페 goat', 'ACTIVATED', NOW(), NULL, 11);