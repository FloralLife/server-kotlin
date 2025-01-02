# API Spec

## Common
* 유저는 1번만 존재.
* 쿠폰 발급시 1번은 정상, 2번은 재고 없음
* Product도 1번만 존재하고 인기 상품 조회는 다른 클래스 사용

#### 400
* ID에 해당하는 필드나 값에 0 이하의 수가 들어간 경우

#### 404
* ID에 해당하는 객체가 없는 경우

## User

### `GET /api/users/{userId}`

**Response**
#### 200
```json
{
  "id": 1,
  "point": 50000000
}
```

### `POST /api/users/{userId}/coupons/{couponId}`
Request
```json
{ }
```
**Response**
#### 200
```json
{
    "id": 1,
    "couponId": 1,
    "expirationDate": "+999999999-12-31",
    "userId": 1,
    "orderId": null
}
```
#### 400
- 쿠폰의 수량이 다 떨어진 경우


### `GET /api/users/{userId}/point`

**Response**
#### 200
```json
50000000
```


### `PUT /api/users/{userId}/point`

**Request**
```json
{
  "point": 100000000
}
```

**Response**
#### 200
```json
{
  "id": 1,
  "point" : 150000000
}
```

#### 400
* 포인트 충전 결과 잔액이 10억을 넘은 경우

## 상품

### `GET /api/products/{productId}`

**Response**
#### 200
```json
{
  "id": 1,
  "name": "맥북",
  "stock": 50,
  "price": 2000000,
  "createdAt": "2025-01-03T04:23:08.807252"
}
```

### `GET /api/products/top`
**Response**
```json
[
  {
    "id": 1,
    "name": "맥북 m1",
    "stock": 5,
    "price": 2000000,
    "createdAt": "2025-01-03T04:46:32.404383"
  },
  {
    "id": 2,
    "name": "맥북 m2",
    "stock": 2,
    "price": 2000000,
    "createdAt": "2025-01-03T04:46:32.404695"
  },
  {
    "id": 3,
    "name": "맥북 m3",
    "stock": 4,
    "price": 2000000,
    "createdAt": "2025-01-03T04:46:32.404708"
  },
  {
    "id": 4,
    "name": "맥북 m4",
    "stock": 7,
    "price": 2000000,
    "createdAt": "2025-01-03T04:46:32.404711"
  },
  {
    "id": 5,
    "name": "맥북 m5",
    "stock": 4,
    "price": 2000000,
    "createdAt": "2025-01-03T04:46:32.404719"
  }
]
```

## 주문 결제

### `POST /api/orders`
**Request**
```json
{
  "address": "선릉역",
  "products": [
    {
      "productId": 1,
      "amount": 25
    }
  ],
  "userId": 1,
  "userCouponId": 1
}
```

**Response**
#### 200
```json
{
  "id": 1,
  "status": "PRODUCT_PREPARING",
  "products": [
    {
      "id": 769418,
      "productId": 1,
      "orderId": 1,
      "amount": 5
    }
  ],
  "userId": 1,
  "address": "선릉역",
  "totalPrice": 49500000,
  "userCouponId": 1,
  "discountPrice": 500000
}
```

### 400
* 쿠폰이 만료된 경우 (MockAPI에서는 2번 쿠폰)
* 상품의 재고보다 많이 주문한 경우 (MockAPI에서는 26개 이상)
* 쿠폰까지 계산한 총 금액이 잔액보다 큰 경우
