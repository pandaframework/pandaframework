package io.polymorphicpanda.faux.sample

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.bootstrap.Bootstrap

class SampleBootstrap: Bootstrap {
    override fun getApplication(): Application = Sample()

}
