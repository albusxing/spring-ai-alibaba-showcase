

```shell
java.lang.UnsupportedClassVersionError:

org/springframework/boot/loader/launch/JarLauncher
has been compiled by a more recent version of the Java Runtime

(class file version 61.0)

this version only recognizes class file versions up to 52.0
```

问题原因

my-mcp-server-stdio 是 JDK 17 编译的
但是运行java -jar 使用的是 JDK 8
