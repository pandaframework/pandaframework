package io.polymorphicpanda.faux.blueprint

import io.polymorphicpanda.faux.runtime.Descriptor

interface Blueprint
interface BlueprintDescriptor<T: Blueprint>: Descriptor<T>
