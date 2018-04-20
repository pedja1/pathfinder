# pathfinder

## MODULES

### core
Contains core classes for solving path and reading input and output data  
used in all other modules
### app
Android application demonstrating pathfinder functionality using both local calculation and rest api
### cli
java command line interface
### rest
REST API

## How to run
### app
1. Import project in Android Studio and click 'run'  
or  
2. `./gradlew assembledebug` then install apk from `{project_root}/app/build/outputs/apk/debug/app-debug.apk`

### cli
1. To compile 'fat' jar (jar that includes all dependencies) execute the following in terminal  
`./gradlew cli:shadowjar`
2. Jar file is located in: `{project_root}/cli/build/lib/cli-1.0-SNAPSHOT-all.jar`
3. Execute jar file using `java -jar` command

Supporter command line arguments (flags)

`-i {path}` - input file - **required if -d is not present**  
`-o {path}` - output file  
`-s` - display graphic representation of solved path (image)  
`-d` - start as REST API daemon. `-i`, `-o` and `-s` are ignored if this flag is used  
`-a` - daemon host (eg. localhost), default is `localhost`  
`-p` - daemon port (eg. 8080), default is `8080`  

### rest
Check cli section on how to start REST API Server

Api is using JSON for communication with clients

#### Endpoints

- `POST /pathfinder/json` - returns result a json  
- `POST /pathfinder/image` - returns image with graphic representation of solved path  

Request format  
```json
{
	"cells": [{
		"col": 0,
		"row": 0
	}, {
		"col": 1,
		"row": 0
	}, {
		"col": 2,
		"row": 0
	}, {
		"col": 3,
		"row": 0
	}, {
		"col": 4,
		"row": 0
	}, {
		"col": 0,
		"row": 1
	}, {
		"col": 1,
		"row": 1
	}, {
		"col": 2,
		"row": 1
	}, {
		"col": 3,
		"row": 1
	}, {
		"col": 4,
		"row": 1
	}, {
		"col": 0,
		"row": 2
	}, {
		"col": 1,
		"row": 2
	}, {
		"col": 2,
		"row": 2
	}, {
		"col": 3,
		"row": 2
	}, {
		"col": 4,
		"row": 2
	}, {
		"col": 0,
		"row": 3
	}, {
		"col": 1,
		"row": 3
	}, {
		"col": 2,
		"row": 3
	}, {
		"col": 3,
		"row": 3
	}, {
		"col": 4,
		"row": 3
	}, {
		"col": 0,
		"row": 4
	}, {
		"col": 1,
		"row": 4
	}, {
		"col": 2,
		"row": 4
	}, {
		"col": 3,
		"row": 4
	}, {
		"col": 4,
		"row": 4
	}],
	"endPoint": {
		"col": 2,
		"row": 2
	},
	"startPoint": {
		"col": 0,
		"row": 0
	}
}
```

Response format  
```json
{
    "message": null,
    "data": {
        "executonTimeInMs": 1,
        "path": {
            "points": [
                {
                    "row": 0,
                    "col": 0
                },
                {
                    "row": 0,
                    "col": 1
                },
                {
                    "row": 0,
                    "col": 2
                },
                {
                    "row": 1,
                    "col": 2
                },
                {
                    "row": 2,
                    "col": 2
                }
            ]
        }
    }
}
```

curl request example  
```bash
curl -X POST \
  http://skynetsoftware.org:8088/pathfinder/json \
  -H 'Content-Type: application/json' \
  -d '{
	"cells": [{
		"col": 0,
		"row": 0
	}, {
		"col": 1,
		"row": 0
	}, {
		"col": 2,
		"row": 0
	}, {
		"col": 3,
		"row": 0
	}, {
		"col": 4,
		"row": 0
	}, {
		"col": 0,
		"row": 1
	}, {
		"col": 1,
		"row": 1
	}, {
		"col": 2,
		"row": 1
	}, {
		"col": 3,
		"row": 1
	}, {
		"col": 4,
		"row": 1
	}, {
		"col": 0,
		"row": 2
	}, {
		"col": 1,
		"row": 2
	}, {
		"col": 2,
		"row": 2
	}, {
		"col": 3,
		"row": 2
	}, {
		"col": 4,
		"row": 2
	}, {
		"col": 0,
		"row": 3
	}, {
		"col": 1,
		"row": 3
	}, {
		"col": 2,
		"row": 3
	}, {
		"col": 3,
		"row": 3
	}, {
		"col": 4,
		"row": 3
	}, {
		"col": 0,
		"row": 4
	}, {
		"col": 1,
		"row": 4
	}, {
		"col": 2,
		"row": 4
	}, {
		"col": 3,
		"row": 4
	}, {
		"col": 4,
		"row": 4
	}],
	"endPoint": {
		"col": 2,
		"row": 2
	},
	"startPoint": {
		"col": 0,
		"row": 0
	}
}'
```

Demo server can be fond on the address:  
`http://skynetsoftware.org:8088/`
