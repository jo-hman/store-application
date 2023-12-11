# store-application

http://localhost:7081/actuator/health
```json
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP",
            "details": {
                "database": "H2",
                "validationQuery": "isValid()"
            }
        },
        "discoveryComposite": {
            "status": "UP",
            "components": {
                "discoveryClient": {
                    "status": "UP",
                    "details": {
                        "services": [
                            "gateway",
                            "order",
                            "account",
                            "product"
                        ]
                    }
                },
                "eureka": {
                    "description": "Remote status from Eureka server",
                    "status": "UP",
                    "details": {
                        "applications": {
                            "GATEWAY": 1,
                            "ORDER": 1,
                            "ACCOUNT": 1,
                            "PRODUCT": 1
                        }
                    }
                }
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 123253534720,
                "free": 67350663168,
                "threshold": 10485760,
                "path": "/home/ochman/IdeaProjects/store/.",
                "exists": true
            }
        },
        "ping": {
            "status": "UP"
        },
        "refreshScope": {
            "status": "UP"
        }
    }
}
```

http://localhost:7081/actuator/metrics/accountsService
```json
{
    "name": "accountsService",
    "baseUnit": "seconds",
    "measurements": [
        {
            "statistic": "COUNT",
            "value": 1.0
        },
        {
            "statistic": "TOTAL_TIME",
            "value": 0.130304479
        },
        {
            "statistic": "MAX",
            "value": 0.0
        }
    ],
    "availableTags": [
        {
            "tag": "method",
            "values": [
                "createAccount"
            ]
        },
        {
            "tag": "error",
            "values": [
                "none"
            ]
        },
        {
            "tag": "class",
            "values": [
                "com.jochmen.account.service.AccountsService"
            ]
        }
    ]
}
```

