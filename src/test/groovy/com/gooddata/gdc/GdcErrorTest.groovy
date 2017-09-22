package com.gooddata.gdc

import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource

class GdcErrorTest extends Specification {

    def "should deserialize"() {
        when:
        def err = readObjectFromResource('/gdc/gdcError.json', GdcError)

        then:
        with(err) {
            errorClass == 'CLASS'
            trace == 'TRACE'
            message == 'MSG %s %s'
            component == 'COMPONENT'
            errorId == 'ID'
            errorCode == 'CODE'
            requestId == 'REQ'
            parameters != null
            parameters.length == Integer.valueOf(2)
            parameters*.toString() == ['PARAM1', 'PARAM2']
        }
    }

}
