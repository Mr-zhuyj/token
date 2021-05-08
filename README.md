# token
token令牌封装，支持redis、memcache两种可选存储。

使用：
1、在application.xml中引入tokenConfig.xml；
2、在controller接口上添加@Token注解，系统就会对该接口进行token拦截。

配置：
1、springboot.token.type
  String，token存储方式，redis->redis存储，memcache->memcache存储，默认值为memcache
2、springboot.token.client
  String，缓存操作的bean名称，默认值为tokenMemcacheClient
3、springboot.token.timeout
  int，token超时时间，默认为15分钟，单位秒
4、springboot.token.isRefresh
  boolean，是否刷新token有效时间，值为true则调用被@Token修饰的接口时会刷新token有效时间，为false则不刷新。默认为true
5、springboot.token.system
  String，系统名称，即token缓存key值的前缀
