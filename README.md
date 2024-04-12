

## method

- v1 
  - FK 공유락 으로 인한 데드락 발생
- v2
  - synchronized 통한 데드락 해결
  - 적합성 깨짐
- v3
  - 비관 락 을 사용하여 데드락 해결
- v4
  - redis 스핀락으로 해결