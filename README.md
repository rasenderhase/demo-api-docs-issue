# demo-api-docs-issue
Try to reproduce springdoc-openapi issue [2332](https://github.com/springdoc/springdoc-openapi/issues/2332)

⚠️ This is a demo to show an error. If you copy this code 1:1 it won't work.

The error is, that it is not possible to use an empty group name in `SwaggerConfig`.

### this is not allowed! -->

```
@Bean
public GroupedOpenApi all() {
    return createDefaultGroupedOpenApi("", "/**");
}                                       ^
empty group name not allowed! ----------'
```
