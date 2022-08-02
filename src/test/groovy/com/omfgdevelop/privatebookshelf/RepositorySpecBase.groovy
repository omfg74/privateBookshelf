package com.omfgdevelop.privatebookshelf

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

/**
 * abstract root test class
 */
@ContextConfiguration(classes = [DbConfigGroovy.class])
@TestPropertySource("classpath:application-test.properties")
@WebAppConfiguration
abstract class RepositorySpecBase extends Specification {


}
