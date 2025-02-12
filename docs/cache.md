# 캐시 사용 전략

[[REDIS] 📚 캐시(Cache) 설계 전략 지침 💯 총정리](https://inpa.tistory.com/entry/REDIS-%F0%9F%93%9A-%EC%BA%90%EC%8B%9CCache-%EC%84%A4%EA%B3%84-%EC%A0%84%EB%9E%B5-%EC%A7%80%EC%B9%A8-%EC%B4%9D%EC%A0%95%EB%A6%AC)

### Cache aside

- 데이터를 찾을 때 우선 캐시에 저장된 데이터가 있는지 우선적으로 확인하고 없으면 DB를 확인하는 전략
- 반복적인 읽기가 많은 호출에 적합
- 만일 캐시 서비스가 다운되더라도 DB에서 데이터를 가져올 수 있어 서비스 자체는 문제가 없음
- 대신 캐시를 사용하는 커넥션이 많았다면, 캐시 서비스가 다운된 순간 순간적으로 DB로 몰려서 부하 발생

### Read through

- 캐시에서만 데이터를 읽어오는 전략
- 데이터 동기화를 라이브러리 또는 캐시 서비스가 위임하는 것에서 차이가 있음
- 데이터를 조회하는데 있어 전체적으로 속도가 느림
- 데이터 조회를 캐시에 전적으로 의존하기에 캐시 서비스가 다운되면 서비스에 문제가 발생할 수 있음
- 캐시와 DB의 데이터 동기화가 항상 이루어져 데이터 정합성 문제가 발생하지 않음

### Write behind

- 데이터를 저장할 때 DB로 바로 쿼리하지 않고, 캐시에 모아서 배치작업을 통해 DB에 반영
- 캐시에 모아놨다가 DB에 쓰기 때문에 쓰기 쿼리 회수 비용과 부하를 줄일 수 있음
- Write가 빈번하면서 Read를 하는데 많은 양의 리소스가 소모되는 서비스에 적합
- 캐시 서비스에서 에러가 발생하면 데이터는 영구석으로 손실됨
- 캐시가 일종의 Queue처럼 역할

### Write Through

- DB와 캐시에 동시에 데이터를 저장하는 전략
- Read Through 와 마찬가지로 DB 동기화 캐시에게 위임
- DB와 캐시가 항상 동기화
- 데이터 유실이 발생하면 안되는 상황에 적합

# 캐시 스탬피드

- 캐시가 만료되었을 때 동시에 여러 요청이 오면 DB에 접근하여 데이터를 가져오려고 시도하게 되고 DB의 과부하가 발생하는 현상

### 해결방안

**Jitter**

- 캐시 만료 시간을 무작위로 조금 지연시켜 데이터베이스의 부하를 분산시키는 방법

**PER 알고리즘**

- Probabilistic Early Recomputation
- 캐시 유효기간이 만료되기 전 일정 확률로 캐시를 재연산하는 방식

1. 캐시 값 조회
2. 캐시 생성 소요 시간을 바탕으로 가중치를 부여한 뒤 랜덤한 x 생성
3. x 와 캐시 만료 시간을 비교하여 캐시 갱신 여부 결정

# 카페인 캐시

> Caffeine is a [high performance](https://github.com/ben-manes/caffeine/wiki/Benchmarks),[near optimal](https://github.com/ben-manes/caffeine/wiki/Efficiency) caching library.
> For more details, see our [user's guide](https://github.com/ben-manes/caffeine/wiki) and browse the[API docs](http://www.javadoc.io/doc/com.github.ben-manes.caffeine/caffeine)for the latest release.

고성능 캐싱 라이브러리

### 로컬 캐시

- 해당 기기에서만 사용되는 캐시
- Map 같은 것들을 이용해서 값을 저장 해놓고 사용하는 것과 유사함
  - 캐시와 맵의 차이점은 캐시는 항목이 제거된다는 것이다. (eviction)

**eviction 정책**

- 주어진 시간에 어떤 객체를 삭제해야하는지 결정
- 이 정책은 캐시의 적중률에 직접적인 영향을 미침
- Caffeine은 거의 최적의 적중률을 제공하는 Window TinyLfu 정책을 사용

https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.caffeine

> [Caffeine](https://github.com/ben-manes/caffeine) is a Java 8 rewrite of Guava’s cache that supersedes support for Guava. If Caffeine is present, a [`CaffeineCacheManager`](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/cache/caffeine/CaffeineCacheManager.html) (provided by the `spring-boot-starter-cache` starter) is auto-configured. Caches can be created on startup by setting the `spring.cache.cache-names` property and can be customized by one of the following (in the indicated order)
>
- spring-boot-start-cache를 사용하고 Caffeine 이 있으면 auto-configure 
