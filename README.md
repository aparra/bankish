# Bankish

## Testing

```shell script
sbt clean test
```

## Executing a transfer 

### Run

```shell script
sbt run
```

### Create an account for `Darth Vader`
 
```shell script
curl \
  --request POST \
  --header 'Content-Type: application/json' \
  --data '{"holder": "Darth Vader", "firstDepositAmount": 100}' \
  http://localhost:9000/api/v1/account
```

```json
{"id":"babeb3c4-e057-43b1-ad6f-e6a22cc01d89","holder":"Darth Vader","balance":"100.00"}
```

### Create an account for `Luke Skywalker` 

```shell script
curl \
  --request POST \
  --header 'Content-Type: application/json' \
  --data '{"holder": "Luke Skywalker", "firstDepositAmount": 200}' \
  http://localhost:9000/api/v1/account
```

```json
{"id":"aa282b75-e7e3-4bd7-82bd-14de74d38887","holder":"Luke Skywalker","balance":"200.00"}
```

### Transfer funds from `Luke Skywalker` to `Darth Vader`

```shell script
curl \
  --request POST \
  --header 'Content-Type: application/json' \
  --data '{"from": "aa282b75-e7e3-4bd7-82bd-14de74d38887", "to": "babeb3c4-e057-43b1-ad6f-e6a22cc01d89", "amount": 50}' \
  http://localhost:9000/api/v1/account/transfer
```

```json
{"at":"2019-08-23 00:16:13","from":"aa282b75-e7e3-4bd7-82bd-14de74d38887","to":"babeb3c4-e057-43b1-ad6f-e6a22cc01d89","amount":"50.00"}
```
