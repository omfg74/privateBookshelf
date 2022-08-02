package com.omfgdevelop.privatebookshelf

class SampleTest extends RepositorySpecBase {

    def 'sample test'() {

        given:

        when:
        def i = 1 + 1

        then:
        i == 2
    }
}
