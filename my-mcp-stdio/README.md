
## 问题描述
```shell
java.lang.UnsupportedClassVersionError:

org/springframework/boot/loader/launch/JarLauncher
has been compiled by a more recent version of the Java Runtime

(class file version 61.0)

this version only recognizes class file versions up to 52.0
```

## 问题原因

my-mcp-server-stdio.jar 是 JDK 17 编译的。my-mcp-client-stdio 启动的时候执行 `java -jar` 命令使用的是 JDK 8。
从而导致错误。
修改 command，指定执行命令时使用的JDK版本。

```json
{
    "mcpServers": {
        "weather": {
            "command": "/Users/liguoqing/Library/Java/JavaVirtualMachines/corretto-17.0.10/Contents/Home/bin/java",
            "args": [
                "-Dspring.ai.mcp.server.stdio=true",
                "-Dspring.main.web-application-type=none",
                "-Dlogging.pattern.console=",
                "-jar",
                "/Users/liguoqing/myworkspace2026/java-ai-study/spring-ai-alibaba-showcase/my-mcp-stdio/my-mcp-server-stdio/target/my-mcp-server-stdio-1.0.0-SNAPSHOT.jar"
            ],
            "env": {}
        }
    }
}


```
