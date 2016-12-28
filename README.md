# web-filter

## 项目描述：在开发当中，有可能有些应用某些页面或者整个页面并不想让外网访问到,所以要根据相应的ip去限制访问的页面，以后可能会更新其它的过滤器

## 项目功能列表：

1 - ip以及访问页面的过滤限制

## 项目环境：

* Tomcat -8.5.9
* jdk-1.8

## 项目配置以及项目部署：

1 - 在项目的web.xml里面加入以下过滤器：
```
<!--访问权限控制-->
    <filter>
        <filter-name>ViewFilter</filter-name>
        <filter-class>com.hearain.web.filter.ViewFilter</filter-class>
        <init-param>
            <param-name>excludedPages</param-name>
            <param-value>/</param-value>
        </init-param>
        <init-param>
            <param-name>allowIp</param-name>
            <param-value>*</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>ViewFilter</filter-name>
        <url-pattern>*</url-pattern>
    </filter-mapping>
```
