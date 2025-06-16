package chloe.godokbang.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.pagination")
@Getter
public class PaginationProperties {

    private int pageSize;
}
