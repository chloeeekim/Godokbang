package chloe.godokbang.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.pagination")
@Getter
@Setter
public class PaginationProperties {

    private int pageSize;
}
