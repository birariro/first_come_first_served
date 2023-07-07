

## 선착순 이벤트 요구사항
- 선착순 n명 에게 쿠폰을 발행한다.
- 쿠폰의 수는 유동적으로 변경할수있어야한다.
- n개 이상의 쿠폰이 발행되면 안된다.



## commit

- 48795473648c03db4b7250ce156bd6aed1f64609
  - 1개 발행 정상. 10개 멀티스레드 발행 데드락
- 8fddf06e1a1f9458a5e965545cbbe4c5a6b59eec
  - synchronized 10개 멀티 스레드 발행 성공
  - event count 적합성 깨짐
- 23620f13b792a946c89b6644d429edd7b2d3892d
  - 락 으로 변경
  - 10개 멀티 스레드 발행 성공
  - event count 적합성 해결
- b45c8c8db403c8473da0ce1315db2f4eb3c4ef36
  - 100개 멀티 스레드 발행 성공
  - for update 쿼리 2초 소요