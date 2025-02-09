# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1단계

**최소시간 경로 Spring Rest Docs 작성**
- [x] `minimumTimePath` 추가

**최소시간 경로 인수테스트 작성**
- [x] 같은 노선의 구간이 최소시간인 경로를 조회한다
- [x] 서로 다른 노선에 있는 구간이 최소시간인 경로를 조회한다

**최소시간 경로 단위 테스트 작성**
- [x] 최소시간 경로 조회
- [x] 최소시간 경로 조회 : 요청 구간 동일
- [x] 최소시간 경로 조회 : 연결되지 않은 구간
- [x] 최소시간 경로 조회 : 존재하지 않는 역

**기능 반영**
- [x] 최소시간 경로 조회 기능

## 2단계

**요금 인수 테스트 작성**

- [x] 기본운임을 넘지 않는 경로의 운임을 조회한다
- [x] 기본운임이 넘고 50km 미만인 경로의 운임을 조회한다
- [x] 기본운임이 넘고 50km 초과인 경로의 운임을 조회한다

**요금 관련 기능 구현**

- [x] 경로 조회 시 구간 길이에 따른 요금 계산 체계 추가 (PathFare)

**1차 피드백 반영**

- [x] PathFinder strategy 빈 등록 제거
- [x] PathFinder 반환 Path 도메인 추가
- [x] Documentation 스니펫 추가

## 3단계

**문서화**

- [x] Authorization 헤더 추가

**운임 인수 테스트 작성**

- [x] 기본운임이 넘고 50km 미만인 경로의 최소시간 경로와 최단거리 경로가 다른 운임을 조회한다
- [x] 노선의 추가운임이 포함된 경로의 운임을 조회한다.
- [x] 여러 노선이 포함된 경로의 가장 높은 기본요금을 가진 운임을 계산한다.
- [x] 어린이가 경로 조회를 하면 운임이 할인 된다.
- [x] 청소년이 경로 조회를 하면 운임이 할인 된다.
- [x] 어린이도, 청소년도 아닌 회원이 경로 조회를 하면 운임이 할인 된다.

**운임 단위 테스트**

- [x] 초과운임이 발생하지 않는 경로의 계산된 운임 계산
- [x] 첫번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임 계산
- [x] 두번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임 계산
- [x] 최단거리경로를 기준으로 하는 경로의 계산된 운임 계산
- [x] 노선의 추가요금 운임 계산
- [x] 가장 높은 기본요금을 가진 노선의 추가요금 운임 계산
- [x] 비회원 운임 계산
- [x] 어린이 운임 계산
- [x] 청소년 운임 계산
- [x] 일반회원 운임 계산
