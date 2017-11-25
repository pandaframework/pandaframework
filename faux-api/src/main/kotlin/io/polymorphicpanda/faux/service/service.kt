package io.polymorphicpanda.faux.service

import io.polymorphicpanda.faux.runtime.Descriptor

interface Service
interface ServiceDescriptor<T: Service>: Descriptor<T>
