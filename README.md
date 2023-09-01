# 지하철 노선 관리 애플리케이션

[![Project use](https://skillicons.dev/icons?i=java,gradle,spring&theme=dark)](#)

> [NextStep ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션   
> 강의 및 과제 기간 : 2023.06 ~ 2023.08

## 프로젝트 목표

- [x] 요구사항에 기반하여 시나리오를 작성하고 이를  통해 테스트 주도 개발로 애플리케이션을 구현한다.
- [x] 테스트 더블의 차이를 경험하고 적절한 기법을 사용한다.
- [x] Stub, Mocking, Fake를 직접 작성해본다.
- [x] 변경되는 비즈니스 요건에 따라 사니라오를 작성하고 리팩터링 하며 테스트 작성한다.
- [x] Spring Rest Docs를 이용해 테스트 기반의 API 문서를 작성한다. 

🤫 **그 외 엄청 중요하거나 목표한 바는 아니지만..**

- 일급컬랙션 사용해보기
- 리치 도메인 모델로 설계하기
- JPA의 직접 참조와 간접 참조의 설계상 차이 이해하기
- 책임 연쇄 패턴과 전략 패턴의 활용
- ✨ 무엇보다 강의 기간 내 전 과제 완성제출 및 컨펌완료

## 프로젝트 소개

### 애플리케이션 주요 기능

- 지하철 구간 관리 기능
- 지하철 노선 관리 기능
- 경로 계산 기능
- 운임 계산 기능
- 개인화 기능

### 프로젝트 패키지 안내

<details>
<summary>
지하철 노선 관리 어플리케이션 구조
</summary>
<pre>
├── SubwayApplication.java
├── auth : 인증/인가 관리 
│   ├── AuthConfig.java
│   ├── principal
│   │   ├── AuthenticationPrincipal.java
│   │   ├── AuthenticationPrincipalArgumentResolver.java
│   │   └── UserPrincipal.java
│   ├── token : JWT 토큰 관리
│   │   ├── JwtTokenProvider.java
│   │   ├── TokenRequest.java
│   │   ├── TokenResponse.java
│   │   ├── TokenService.java
│   │   └── oauth2
│   │       ├── OAuth2User.java
│   │       ├── OAuth2UserRequest.java
│   │       ├── OAuth2UserService.java
│   │       └── github
│   │           ├── GithubAccessTokenRequest.java
│   │           ├── GithubAccessTokenResponse.java
│   │           ├── GithubClient.java
│   │           ├── GithubProfileResponse.java
│   │           └── GithubTokenRequest.java
│   ├── ui
│   │   └── LoginController.java
│   └── userdetails
│       ├── UserDetails.java
│       └── UserDetailsService.java
├── constant
│   └── SubwayMessage.java
├── exception
│   ├── AuthenticationException.java
│   ├── ErrorResponse.java
│   ├── ExceptionController.java
│   ├── SubwayBadRequestException.java
│   ├── SubwayException.java
│   └── SubwayNotFoundException.java
├── line : 노선/구간 도메인
│   ├── application
│   │   ├── LineService.java
│   │   ├── SectionService.java
│   │   └── dto
│   │       ├── LineCreateRequest.java
│   │       ├── LineModifyRequest.java
│   │       ├── LineRetrieveResponse.java
│   │       ├── SectionCreateRequest.java
│   │       └── SectionDeleteRequest.java
│   ├── domain
│   │   ├── Line.java : 노선
│   │   ├── LineRepository.java
│   │   ├── LineSections.java
│   │   ├── Section.java : 구간
│   │   └── SectionRepository.java
│   └── ui
│       └── LineController.java
├── member : 회원 도메인 
│   ├── application
│   │   ├── CustomOAuth2UserService.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── FavoriteService.java
│   │   ├── MemberService.java
│   │   └── dto
│   │       ├── FavoriteCreateRequest.java
│   │       ├── FavoriteCreateResponse.java
│   │       ├── FavoriteRetrieveResponse.java
│   │       ├── MemberRequest.java
│   │       └── MemberRetrieveResponse.java
│   ├── domain
│   │   ├── CustomOAuth2User.java
│   │   ├── CustomUserDetails.java
│   │   ├── Favorite.java
│   │   ├── FavoriteRepository.java
│   │   ├── Member.java
│   │   ├── MemberFavorites.java
│   │   ├── MemberRepository.java
│   │   └── RoleType.java
│   └── ui
│       ├── FavoriteController.java
│       └── MemberController.java
├── path : 경로 도메인 
│   ├── application
│   │   ├── PathService.java
│   │   └── dto
│   │       ├── PathFareCalculationInfo.java
│   │       ├── PathFinderRequest.java
│   │       ├── PathRetrieveRequest.java
│   │       └── PathRetrieveResponse.java
│   ├── domain
│   │   ├── Path.java : 경로
│   │   ├── PathFare.java : 운임 메소드 체인 생성자 및 실행  
│   │   ├── PathFinder.java : 경로 및 운임 오케스트레이션 
│   │   ├── PathFinderFactory.java : 경로 검색 팩토리 
│   │   ├── PathRetrieveType.java
│   │   ├── graph : 가중치 그래프와 다익스트라 라이브러리 관련 클래스 
│   │   │   ├── GraphBuilder.java
│   │   │   └── SectionEdge.java
│   │   ├── handler : 운임 관련 책임 연쇄 패턴 클래스  
│   │   │   ├── DistancePathFareHandler.java
│   │   │   ├── GraphPathFareHandler.java
│   │   │   ├── LineSurchargePathFareHandler.java
│   │   │   ├── MemberAgePathFareHandler.java
│   │   │   └── PathFareChain.java
│   │   └── strategy : 경로 검색 관련 전략 패턴 클래스
│   │       ├── MinimumTimePathFinderStrategy.java
│   │       ├── PathFinderStrategy.java
│   │       └── ShortestDistancePathFinderStrategy.java
│   └── ui
│       └── PathController.java
└── station : 지하철 역 도메인
    ├── application
    │   ├── StationService.java
    │   └── dto
    │       ├── StationRequest.java
    │       └── StationResponse.java
    ├── domain
    │   ├── Station.java
    │   └── StationRepository.java
    └── ui
        └── StationController.java
</pre>
</details>



## 지하철 노선 관리 기능 소개

### 지하철 노선 관리 기능

### 경로 찾기 기능

### 지하철 운임 기능

## 테스트

