import React from 'react'
import PropTypes from 'prop-types'


const SampleReactComponent = props => {
    return <b>Hello {props.name}</b>
}

SampleReactComponent.PropTypes = {
    name: PropTypes.string.isRequired
}
export default SampleReactComponent
