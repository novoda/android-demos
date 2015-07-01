#Demo OptionalDependencies [![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](LICENSE.txt)

OptionalDependencies shows how to create two product flavours that have different implementations of one method.

The demo uses a base class `BaseOptionalDependencies` that provides no-op methods and is used in the different flavours.
Each flavour only has to overwrite relevant methods for their flavour. This ensures that the main class `OptionalDependencies`
has the same signature in all flavours.
