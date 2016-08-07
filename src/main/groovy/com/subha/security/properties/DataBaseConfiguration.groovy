package com.subha.security.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Created by user on 8/4/2016.
 */

@Component
@ConfigurationProperties("spring.datasource")
class DataBaseConfiguration {
}
