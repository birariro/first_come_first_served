

## method

- v1 
  - FK 공유락 으로 인한 데드락 발생
- v2
  - synchronized 통한 데드락 해결
  - 적합성 깨짐
- v3
  - 비관 락 을 사용하여 데드락 해결
- v4
  - redis 로 순차적 수 발행
- v5
  - redis 로 순차적 쿠폰 발행