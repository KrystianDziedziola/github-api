package com.empik.github.domain

import spock.lang.Specification

class UserTest extends Specification {

    def "Should make correct calculations"() {
        expect: "Calculations are correct"
        def user = User.builder()
                       .followersNumber(followers)
                       .publicRepositoriesNumber(repositories)
                       .build()
        user.makeCalculations() == expectedResult

        where:
        followers | repositories | expectedResult
        1         | 1            | 18.0
        6         | 5            | 7.0
        5         | 10           | 14.399999999999999
    }

    def "Should throw exception when trying to divide by zero"() {
        given: "User with 0 followers"
        def user = User.builder()
                       .followersNumber(0)
                       .publicRepositoriesNumber(16)
                       .build()

        when: "Making calculations"
        user.makeCalculations()

        then: "Exception is thrown"
        thrown(CalculationsException)
    }
}
