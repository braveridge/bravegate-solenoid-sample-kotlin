# How to specify BraveGATE webhook URL

ref. [BraveGATE CORE user's guide](https://www.braveridge.com/files/uploads/BraveGATE_CORE_User__039;sGuide.pdf)

## Prepare

### Determine your working directory
```bash
cd tools/bravegate/api
```

### Edit your auth key id and secret of BraveGATE
```bash
vi json/auth.json
```

## Get api key and token
```bash
curl https://api.braveridge.io/v1/auth -X POST -d @json/auth.json`
```

### response example
```json
{
  "apikey":"AKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
  "token":"TKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
  "expire_at":"2021-01-01T00:00:00+09:00"
}
```

## Get group id
```bash
curl https://api.braveridge.io/v1/groups -H 'X-Braveridge-API-Key:AKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx' -H 'X-Braveridge-Token:TKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'
```

### response example
```json
{
    "groups":[{
        "group_id":"GRxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "name":"your group name",
        "is_default":true,
        "created_at":"2021-01-01T00:00:00+09:00",
        "updated_at":"2021-01-01T00:00:00+09:00"
    }],
    "total":1,
    "pages":1,
    "limit":10,
    "current_page":1,
    "next_page":null
}
```


## Specify webhook URL

### Edit your group id and webhook URL
```bash
vi json/post_applications.json
```

### create application
```bash
curl https://api.braveridge.io/v1/applications -X POST -d @json/post_applications.json -H 'X-Braveridge-API-Key:AKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx' -H 'X-Braveridge-Token:TKxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'
```

### response example
```json
{
    "application_id":"APxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "name":"my webhook",
    "group_id":"GRxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "application_type":"webhook",
    "settings":{
        "url":"https://your.domain/path-to-webhook"
    },
    "created_at":"2021-01-01T00:00:00+09:00",
    "updated_at":"2021-01-01T00:00:00+09:00"
}
```


# Receive solenoid state
BraveGATE sends an HTTP POST request with a webhook object the webhook URL specified above.

## webhook object example
```json
{
    "application": {
        "application_id": "APxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "name": "my webhook"
    },
    "router": {
        "router_id": "00000000",
        "imsi": "000000000000000",
        "rssi": -50,
        "battery": 75,
        "fw_version": "0.0.1"
    },
    "device": {
        "device_id": "2468800fff000000",
        "sensor_id": "00f0",
        "sensor_name": "General sensor01",
        "rssi": -50,
        "data": {
            "data": "AA=="
        }
    },
    "uplink_id": "1111aaaa-4222-bbbb-3333-cccc4444dddd",
    "date": "2021-01-01T00:00:00+09:00"
}
```

`device.data.data` is base64 encoded value.

- AA== : 0x00
  - state is On
- AQ== : 0x01
  - state is Off
